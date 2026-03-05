package com.example.newsapp.repository

import com.example.newsapp.api.NewsApiService
import com.example.newsapp.model.News
import javax.inject.Inject

class NewsRepository@Inject constructor(private val api: NewsApiService) {
    suspend fun getNewsByCategory(category: String, page: Int = 1): List<News> {
        return api.getNews(category = category, page = page).articles
    }
}