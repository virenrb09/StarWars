package com.viren.starwars_api.impl

import com.viren.starwars_api_bridge.api.StarWarsApi
// Avoid star import - just added to show standards that ca be followed
import com.viren.starwars_api_bridge.model.*
import com.viren.starwars_api_bridge.repository.StarWarsRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class StarWarsRepositoryImplTest {

    private lateinit var starWarsRepository: StarWarsRepository

    @Mock
    private lateinit var starWarsApi: StarWarsApi

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        starWarsRepository = StarWarsRepositoryImpl(starWarsApi)
    }

    @Test
    fun searchCharacterName() {
        val response: CharacterSearchResponse = characterSearchResponse()
        Mockito.`when`(starWarsApi.searchCharacterName("name"))
            .thenReturn(Single.just(response))

        val obs = starWarsRepository.searchCharacterName("name").test()

        obs
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(response)
            .dispose()
        Mockito.verify(starWarsApi).searchCharacterName("name")
    }

    @Test
    fun searchCharacterPage() {
        val response: CharacterSearchResponse = characterSearchResponse()
        Mockito.`when`(starWarsApi.searchCharacterPage("url")).thenReturn(Single.just(response))

        val obs = starWarsRepository.searchCharacterPage("url").test()

        obs
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(response)
            .dispose()
        Mockito.verify(starWarsApi).searchCharacterPage("url")
    }

    @Test
    fun getCharacterDetail() {
        val charDetail: CharacterDetailsResponse = characterDetailsResponse()
        Mockito.`when`(starWarsApi.getCharacterDetail("url"))
            .thenReturn(Single.just(charDetail))

        val obs = starWarsRepository.getCharacterDetail("url").test()

        obs
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(charDetail)
            .dispose()
        Mockito.verify(starWarsApi).getCharacterDetail("url")
    }

    @Test
    fun getPlanetDetails() {
        val response: PlanetDetailsResponse = planetDetailsResponse()
        Mockito.`when`(starWarsApi.getPlanetDetails("url")).thenReturn(Single.just(response))

        val obs = starWarsRepository.getPlanetDetails("url").test()

        obs
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(response)
            .dispose()
        Mockito.verify(starWarsApi).getPlanetDetails("url")
    }

    @Test
    fun getSpeciesDetails() {
        // provide dummy object as response
        val response = speciesDetailsResponse()
        Mockito.`when`(starWarsApi.getSpeciesDetails("url")).thenReturn(Single.just(response))

        // response test stream
        val obs = starWarsRepository.getSpeciesDetails("url").test()

        // result
        obs
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(response)
            .dispose()
        Mockito.verify(starWarsApi).getSpeciesDetails("url")
    }

    @Test
    fun getFilmsDetails() {
        // provide dummy object as response
        val movieDetails = moviesDetails()
        Mockito.`when`(starWarsApi.getFilmsDetails("url")).thenReturn(Single.just(movieDetails))

        // response test stream
        val obs = starWarsRepository.getFilmsDetails("url").test()

        // result
        obs
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(movieDetails)
            .dispose()
        Mockito.verify(starWarsApi).getFilmsDetails("url")
    }

}