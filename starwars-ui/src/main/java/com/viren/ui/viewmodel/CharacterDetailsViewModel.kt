package com.viren.ui.viewmodel

import androidx.annotation.VisibleForTesting
import com.viren.starwars_api_bridge.repository.StarWarsRepository
import com.viren.ui.scheduler.SchedulerProvider
import com.viren.ui.utils.Response
import com.viren.ui.utils.ResponseTypes
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

interface CharacterDetailsViewModel {
    fun setupDetails(url: String)
    fun cleanup()
    fun observeData(): Observable<Response<Pair<ResponseTypes, Any>>>
}

class CharacterDetailsViewModelImpl(
    private val starWarsRepository: StarWarsRepository,
    private val schedulerProvider: SchedulerProvider
) : CharacterDetailsViewModel {

    @VisibleForTesting
    val disposable = CompositeDisposable()

    @VisibleForTesting
    val detailsResponseData = BehaviorSubject.create<Response<Pair<ResponseTypes, Any>>>()

    override fun setupDetails(url: String) {
        getDetails(url)
    }

    override fun cleanup() {
        disposable.dispose()
    }

    override fun observeData(): Observable<Response<Pair<ResponseTypes, Any>>> =
        detailsResponseData.hide()

    /**
     * Fetches details about character
     * Containing all above urls to fetch data of planets, species, films, etc.
     */
    private fun getDetails(url: String) {
        disposable.add(
            starWarsRepository.getCharacterDetail(url)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    // emitting the characterDetails to activity
                    detailsResponseData.onNext(Response.Success(ResponseTypes.CHARACTER_DETAILS to it))

                    // fetching the species details
                    getSpeciesData(it.species)

                    // fetching the films details
                    getFilmsData(it.films)

                    // fetching the planet details
                    getPlanetDetails(it.homeworld)
                }, {
                    detailsResponseData.onNext(
                        Response.Error(
                            url,
                            ResponseTypes.CHARACTER_DETAILS to Any()
                        )
                    )
                })
        )
    }

    private fun getPlanetDetails(homeWorld: String) {
        disposable.add(
            starWarsRepository.getPlanetDetails(homeWorld)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    detailsResponseData.onNext(Response.Success(ResponseTypes.PLANET_DETAILS to it))
                }, {
                    detailsResponseData.onNext(
                        Response.Error(
                            homeWorld,
                            ResponseTypes.PLANET_DETAILS to Any()
                        )
                    )
                })
        )
    }

    private fun getFilmsData(films: List<String>) {
        films.forEach { film ->
            disposable.add(
                starWarsRepository.getFilmsDetails(film)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({
                        detailsResponseData.onNext(Response.Success(ResponseTypes.MOVIE_DETAILS to it))
                    }, {
                        detailsResponseData.onNext(
                            Response.Error(
                                film,
                                ResponseTypes.MOVIE_DETAILS to Any()
                            )
                        )
                    })
            )
        }
    }

    private fun getSpeciesData(species: List<String>) {
        species.forEach { specie ->
            disposable.add(
                starWarsRepository.getSpeciesDetails(specie)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({
                        detailsResponseData.onNext(Response.Success(ResponseTypes.SPECIE_DETAILS to it))
                    }, {
                        detailsResponseData.onNext(
                            Response.Error(
                                specie,
                                ResponseTypes.SPECIE_DETAILS to Any()
                            )
                        )
                    })
            )
        }
    }

}