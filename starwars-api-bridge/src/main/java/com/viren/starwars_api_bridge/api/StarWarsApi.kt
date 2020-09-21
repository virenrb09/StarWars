package com.viren.starwars_api_bridge.api

import com.viren.starwars_api_bridge.model.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

private const val CHARACTER_SEARCH_URL = "people/?"

interface StarWarsApi {

    @GET(CHARACTER_SEARCH_URL)
    fun searchCharacterName(@Query("search") characterName: String): Single<CharacterSearchResponse>

    @GET
    fun searchCharacterPage(@Url url: String): Single<CharacterSearchResponse>

    @GET
    fun getCharacterDetail(@Url url: String): Single<CharacterDetailsResponse>

    @GET
    fun getPlanetDetails(@Url url: String): Single<PlanetDetailsResponse>

    @GET
    fun getSpeciesDetails(@Url url: String): Single<SpeciesDetailsResponse>

    @GET
    fun getFilmsDetails(@Url url: String): Single<MoviesDetailsResponse>

}