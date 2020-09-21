package com.viren.starwars_api.util

fun String.secureUrl(): String {
    return if (this.contains("http://")) {
        this.replace("http://", "https://", false)
    } else this
}