package com.viren.ui.viewmodel

import com.viren.starwars_api_bridge.repository.StarWarsRepository
import com.viren.starwars_api_bridge.testutil.characterDetailsResponse
import com.viren.starwars_api_bridge.testutil.planetDetailsResponse
import com.viren.ui.scheduler.SchedulerProvider
import com.viren.ui.scheduler.TestSchedulerProvider
import com.viren.ui.utils.ResponseTypes
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CharacterDetailsViewModelImplTest {

    private lateinit var characterDetailsViewModel: CharacterDetailsViewModelImpl

    @Mock
    private lateinit var starWarsRepository: StarWarsRepository
    private val schedularProvider: SchedulerProvider = TestSchedulerProvider()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        characterDetailsViewModel =
            CharacterDetailsViewModelImpl(starWarsRepository, schedularProvider)
    }

    @Test
    fun cleanup() {
        characterDetailsViewModel.cleanup()
        assert(characterDetailsViewModel.disposable.isDisposed)
    }

    @Test
    fun setupDetails() {
        val charDetails = characterDetailsResponse()
        val planetDetails = planetDetailsResponse()
        Mockito.`when`(starWarsRepository.getCharacterDetail("url"))
            .thenReturn(Single.just(charDetails))
        Mockito.`when`(starWarsRepository.getPlanetDetails(charDetails.homeworld)).thenReturn(
            Single.just(planetDetails)
        )

        val obs = characterDetailsViewModel.observeData().test()

        characterDetailsViewModel.setupDetails("url")

        obs.assertSubscribed()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueAt(0) {
                it.data?.first == ResponseTypes.CHARACTER_DETAILS && it.data?.second == charDetails
            }
            .assertValueAt(1) {
                it.data?.first == ResponseTypes.PLANET_DETAILS && it.data?.second == planetDetails
            }
            .dispose()
    }
}