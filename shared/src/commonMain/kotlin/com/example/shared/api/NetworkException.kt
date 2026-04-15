package com.example.shared.api

sealed class NetworkException : Exception() {
    object Unauthorized : NetworkException()
    object RateLimitReached : NetworkException()
    object Unknown : NetworkException()
}