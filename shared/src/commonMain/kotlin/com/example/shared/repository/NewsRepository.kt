package com.example.shared.repository

import com.example.shared.api.NewsApiService
import com.example.shared.model.News

class NewsRepository(private val api: NewsApiService) {
    suspend fun getNewsByCategory(category: String, page: Int = 1): List<News> {
        return api.getNews(category = category, page = page).articles
    }
}