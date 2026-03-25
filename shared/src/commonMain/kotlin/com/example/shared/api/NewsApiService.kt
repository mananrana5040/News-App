package com.example.shared.api

import com.example.shared.model.NewsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class NewsApiService(private val client: HttpClient) {
    suspend fun getNews(category: String, page: Int): NewsResponse {
        return client.get("https://newsapi.org/v2/top-headlines") {
            parameter("apiKey", "c53210620ab940dfaf0d88428026d707")
            parameter("category", category)
            parameter("page", page)
            parameter("country", "us")
        }.body()
    }
}

//            parameter("apiKey", "d2a75ad5b0a8488f9e8330c73661a2ca")
