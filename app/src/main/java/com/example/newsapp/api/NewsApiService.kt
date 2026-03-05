package com.example.newsapp.api

import com.example.newsapp.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("apiKey") apiKey: String = "d2a75ad5b0a8488f9e8330c73661a2ca",
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1,
        @Query("category") category: String
    ): NewsResponse
}