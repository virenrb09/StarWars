package com.viren.starwars_api_bridge.model

import java.io.Serializable

/**
 * Response of character search by name
 */
data class CharacterSearchResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<CharacterDetail>
)

/**
 * Details of a character
 * Serializable - is impl to pass the object to details fragment
 */
data class CharacterDetail(
    val birth_year: String,
    val created: String,
    val edited: String,
    val eye_color: String,
    val films: List<String>,
    val gender: String,
    val hair_color: String,
    val height: String,
    val homeworld: String,
    val mass: String,
    val name: String,
    val skin_color: String,
    val species: List<String>,
    val starships: List<String>,
    val url: String,
    val vehicles: List<String>
) : Serializable
