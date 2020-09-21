package com.viren.starwars_api.impl

import com.viren.starwars_api.util.secureUrl
import com.viren.starwars_api_bridge.api.StarWarsApi
import com.viren.starwars_api_bridge.model.*
import com.viren.starwars_api_bridge.repository.StarWarsRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class StarWarsRepositoryImpl(
    private val starWarsApi: StarWarsApi
) : StarWarsRepository {

    override fun searchCharacterName(characterName: String): Single<CharacterSearchResponse> =
        starWarsApi.searchCharacterName(characterName)

    override fun searchCharacterPage(url: String): Single<CharacterSearchResponse> =
        starWarsApi.searchCharacterPage(url.secureUrl())

    override fun getCharacterDetail(url: String): Single<CharacterDetailsResponse> =
        starWarsApi.getCharacterDetail(url.secureUrl())

    override fun getPlanetDetails(url: String): Single<PlanetDetailsResponse> =
        starWarsApi.getPlanetDetails(url.secureUrl())

    override fun getSpeciesDetails(url: String): Single<SpeciesDetailsResponse> =
        starWarsApi.getSpeciesDetails(url.secureUrl())

    override fun getFilmsDetails(url: String): Single<MoviesDetailsResponse> =
        starWarsApi.getFilmsDetails(url.secureUrl())
}