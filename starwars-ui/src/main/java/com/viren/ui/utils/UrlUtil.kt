package com.viren.ui.utils

fun String.secureUrl(): String {
    return if (this.contains("http://")) {
        this.replace("http://", "https://", false)
    } else ""
}