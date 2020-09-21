package com.viren.ui.viewmodel

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentManager
import com.viren.starwars_api_bridge.model.CharacterDetail
import com.viren.starwars_api_bridge.repository.StarWarsRepository
import com.viren.starwars_ui.R
import com.viren.ui.scheduler.SchedulerProvider
import com.viren.ui.usecase.NavigationUseCase
import com.viren.ui.utils.Response
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

interface CharacterSearchViewModel {
    fun searchCharacter(name: String): Disposable
    fun characterSearchResult(): Observable<Response<List<CharacterDetail>>>
    fun showCharacterDetails(character: CharacterDetail)
    fun pageCount(): Observable<String>
    fun loadNextPage()
}

class CharacterSearchViewModelImpl(
    private val context: Context,
    private val starWarsRepository: StarWarsRepository,
    private val navigationUseCase: NavigationUseCase,
    private val fragmentManager: FragmentManager,
    private val schedulerProvider: SchedulerProvider
) : CharacterSearchViewModel {

    @VisibleForTesting
    val result = BehaviorSubject.create<Response<List<CharacterDetail>>>()
    private val pageCountString = BehaviorSubject.create<String>()

    @VisibleForTesting
    var nextPage: String? = null
    private var nextPageDisposable: Disposable? = null

    /**
     * Binding data with streams
     * you can bind the data to view using this method, where in you have a Observable field
     * and you subscribe to it to get update
     */
    override fun characterSearchResult(): Observable<Response<List<CharacterDetail>>> =
        result.hide()

    override fun searchCharacter(name: String): Disposable {
        nextPage = null
        pageCountString.onNext("")
        resetNextPageDisposable()
        result.onNext(Response.Loading())
        return starWarsRepository.searchCharacterName(name)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy({
                result.onNext(Response.Error(it.message.orEmpty()))
            }, {
                nextPage = it.next
                result.onNext(Response.Success(it.results))
                pageCountString.onNext("Showing ${it.results.count()} of ${it.count} characters")
            })
    }

    override fun showCharacterDetails(character: CharacterDetail) {
        navigationUseCase.launchCharacterDetails(
            context,
            character.name,
            character.url,
            fragmentManager,
            R.id.container
        )
    }

    override fun pageCount(): Observable<String> = pageCountString.hide()

    override fun loadNextPage() {
        if (result.hasValue() && result.value is Response.Success && nextPageDisposable == null) {
            nextPage?.let {
                nextPageDisposable = starWarsRepository.searchCharacterPage(it)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribeBy({
                        resetNextPageDisposable()
                        // Handle next page load error
                    }, { response ->
                        resetNextPageDisposable()
                        nextPage = response.next
                        val items = (result.value as Response.Success).data?.toMutableList()
                            ?: mutableListOf()
                        items.addAll(response.results)
                        result.onNext(Response.Success(items))
                        pageCountString.onNext("Showing ${items.count()} of ${response.count} characters")
                    })
            }
        }
    }

    private fun resetNextPageDisposable() {
        nextPageDisposable?.dispose()
        nextPageDisposable = null
    }

}