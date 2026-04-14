package com.example.shared.api

import com.example.newsapp.BuildKonfig
import com.example.shared.config.ApiKeyManager
import com.example.shared.model.NewsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class NewsApiService(private val client: HttpClient, private val keyManager: ApiKeyManager) {
    suspend fun getNews(category: String, page: Int): NewsResponse {
        while (true) {
            val currentKey = keyManager.getActiveKey()
            try {
                print("\napikey: $currentKey")
                return client.get("${BuildKonfig.BASE_URL}/v2/top-headlines") {
                    parameter("apiKey", currentKey)
                    parameter("category", category)
                    parameter("page", page)
                    parameter("country", "us")
                }.body()
            } catch (e: Exception) {
                print("\napikey: $e")
                if (e.message?.contains("429") == true || e.message?.contains("401") == true) {
                    val canRotate = keyManager.rotateKey()
                    if (canRotate) {
                        print("\napikey: key rotated!")
                        continue
                    } else {
                        print("\napikey: Limit Exceed")
                        throw Exception("All API keys exhausted")
                    }
                } else {
                    print("\napikey: Unknown Error")
                    throw Exception("Unknown Error")
                }
            }
        }
    }
}

//            parameter("apiKey", "d2a75ad5b0a8488f9e8330c73661a2ca")
