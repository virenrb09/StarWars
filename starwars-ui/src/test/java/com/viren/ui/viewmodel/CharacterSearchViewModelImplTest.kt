package com.viren.ui.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.viren.starwars_api_bridge.model.CharacterDetail
import com.viren.starwars_api_bridge.repository.StarWarsRepository
import com.viren.starwars_api_bridge.testutil.characterSearchResponse
import com.viren.ui.scheduler.SchedulerProvider
import com.viren.ui.scheduler.TestSchedulerProvider
import com.viren.ui.usecase.NavigationUseCase
import com.viren.ui.utils.Response
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class CharacterSearchViewModelImplTest {
    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var starWarsRepository: StarWarsRepository

    @Mock
    private lateinit var navigationUseCase: NavigationUseCase

    @Mock
    private lateinit var fragmentManager: FragmentManager

    private val schedulerProvider: SchedulerProvider = TestSchedulerProvider()

    private lateinit var characterSearchViewModel: CharacterSearchViewModelImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        characterSearchViewModel = CharacterSearchViewModelImpl(
            context,
            starWarsRepository,
            navigationUseCase,
            fragmentManager,
            schedulerProvider
        )
    }

    @Test
    fun searchCharacter() {
        val characterSearchResp = characterSearchResponse().copy(
            next = "nextPage",
            count = 2,
            previous = "",
            results = listOf(details())
        )
        Mockito.`when`(starWarsRepository.searchCharacterName("name")).thenReturn(
            Single.just(characterSearchResp)
        )
        val obs = characterSearchViewModel.characterSearchResult().test()

        characterSearchViewModel.searchCharacter("name")

        obs
            .assertSubscribed()
            .assertNoErrors()
            .assertNotComplete()
            .assertValueAt(0) {
                it is Response.Loading
            }
            .assertValueAt(1) {
                it is Response.Success && it.data?.size == 1
            }
            .dispose()
        assert(characterSearchViewModel.nextPage == "nextPage")
    }

    @Test
    fun `searchCharacter - when api throws error`() {
        Mockito.`when`(starWarsRepository.searchCharacterName("name")).thenReturn(
            Single.error(Throwable())
        )
        val obs = characterSearchViewModel.characterSearchResult().test()

        characterSearchViewModel.searchCharacter("name")

        obs
            .assertSubscribed()
            .assertNoErrors()
            .assertNotComplete()
            .assertValueAt(0) {
                it is Response.Loading
            }
            .assertValueAt(1) {
                it is Response.Error
            }
            .dispose()
    }
    @Test
    fun loadNextPage() {
        characterSearchViewModel.result.onNext(Response.Success(listOf(details())))
        characterSearchViewModel.nextPage = "nextPage"
        val characterSearchResp = characterSearchResponse().copy(
            next = "",
            count = 2,
            previous = "page",
            results = listOf(details())
        )
        Mockito.`when`(starWarsRepository.searchCharacterPage("nextPage")).thenReturn(
            Single.just(characterSearchResp)
        )
        val obs = characterSearchViewModel.characterSearchResult().test()

        characterSearchViewModel.loadNextPage()

        obs
            .assertSubscribed()
            .assertNoErrors()
            .assertNotComplete()
            .assertValueAt(0) {
                it is Response.Success && it.data?.size == 1
            }
            .dispose()
    }

    private fun details() = CharacterDetail(
        "birth_year",
        "created",
        "edited",
        "eye_color",
        listOf(),
        "gender",
        "hair_color",
        "height",
        "homeworld",
        "mass",
        "name",
        "skin_color",
        listOf(),
        listOf(),
        "url",
        listOf()
    )
}