package com.viren.starwars_api_bridge.testutil

import com.viren.starwars_api_bridge.model.*

/**
 * Mock Util for providing dummy responses
 */

fun planetDetailsResponse() = PlanetDetailsResponse(
    "climate",
    "created",
    "diameter",
    "edited",
    listOf(),
    "gravity",
    "name",
    "orbital_period",
    "population",
    listOf(),
    "rotation_period",
    "surface_water",
    "terrain",
    "url"
)

fun characterDetailsResponse() = CharacterDetailsResponse(
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

fun characterSearchResponse() = CharacterSearchResponse(
    4,
    "",
    "",
    listOf()
)

fun speciesDetailsResponse() = SpeciesDetailsResponse(
    "average_height",
    "average_lifespan",
    "classification",
    "created",
    "designation",
    "edited",
    "eye_colors",
    listOf(),
    "hair_colors",
    "homeworld",
    "language",
    "name",
    listOf(),
    "skin_colors",
    "url"
)

fun moviesDetails() = MoviesDetailsResponse(
    listOf(),
    "created",
    "director",
    "edited",
    0,
    "opening_crawl",
    listOf(),
    "producer",
    "release_date",
    listOf(),
    listOf(),
    "title",
    "url",
    listOf()
)
