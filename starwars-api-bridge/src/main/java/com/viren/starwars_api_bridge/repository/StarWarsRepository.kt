package com.viren.starwars_api_bridge.repository

import com.viren.starwars_api_bridge.model.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface StarWarsRepository {
    fun searchCharacterName(characterName: String): Single<CharacterSearchResponse>
    fun searchCharacterPage(url: String): Single<CharacterSearchResponse>
    fun getCharacterDetail(url: String): Single<CharacterDetailsResponse>
    fun getPlanetDetails(url: String): Single<PlanetDetailsResponse>
    fun getSpeciesDetails(url: String): Single<SpeciesDetailsResponse>
    fun getFilmsDetails(url: String): Single<MoviesDetailsResponse>
}
