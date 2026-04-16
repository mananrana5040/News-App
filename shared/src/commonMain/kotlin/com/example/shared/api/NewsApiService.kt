package com.example.shared.api

import com.example.newsapp.BuildKonfig
import com.example.shared.config.ApiKeyManager
import com.example.shared.model.NewsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

class NewsApiService(private val client: HttpClient, private val keyManager: ApiKeyManager) {
    suspend fun getNews(category: String, page: Int): NewsResponse {
        while (true) {
            val currentKey = keyManager.getActiveKey()
            try {
                return client.get("${BuildKonfig.BASE_URL}/v2/top-headlines") {
                    parameter("apiKey", currentKey)
                    parameter("category", category)
                    parameter("page", page)
                    parameter("country", "us")
                }.body()
            } catch (e: ClientRequestException) {
                val networkError = when (e.response.status) {
                    HttpStatusCode.Unauthorized -> NetworkException.Unauthorized
                    HttpStatusCode.TooManyRequests -> NetworkException.RateLimitReached
                    else -> NetworkException.Unknown
                }
                if (networkError is NetworkException.Unauthorized || networkError is NetworkException.RateLimitReached) {
                    val canRotate = keyManager.rotateKey()
                    if (canRotate) {
                        continue
                    } else {
                        throw Exception("All API keys exhausted")
                    }
                } else {
                    throw NetworkException.Unknown
                }
            }catch (e: Exception) {
                throw NetworkException.Unknown
            }
        }
    }
}

//            parameter("apiKey", "d2a75ad5b0a8488f9e8330c73661a2ca")
