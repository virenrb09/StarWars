package com.viren.ui.utils

/**
 * Generic class to handle api call state
 * Display loading
 * Display error
 * Display results
 */
sealed class Response<T>(
        val data: T? = null,
        val message: String? = null
) {
    /**
     * When we get the response successfully
     */
    class Success<T>(data: T) : Response<T>(data)

    /**
     * When we want to show the loading
     */
    class Loading<T>(data: T? = null) : Response<T>(data)

    /**
     * When there is an error while getting the data
     */
    class Error<T>(message: String, data: T? = null) : Response<T>(data, message)
}
