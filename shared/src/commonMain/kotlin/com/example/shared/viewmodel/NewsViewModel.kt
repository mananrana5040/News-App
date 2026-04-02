package com.example.shared.viewmodel

import kotlin.collections.plus
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.model.News
import com.example.shared.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.log

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    var articles = mutableStateOf<List<News>>(emptyList())

    var breakingNews = mutableStateOf<News?>(null)
    var isLoading = mutableStateOf(false)
    var selectedCategory = mutableStateOf("All")

    var currentPage = 1

    init {
        fetchBreakingNews()
        fetchNews("general")
    }

    private fun fetchBreakingNews() {
        viewModelScope.launch {
            try {
                val response = repository.getNewsByCategory("general")
                if (response.isNotEmpty()) {
                    breakingNews.value = response[0]
                }
            } catch (e: Exception) {
            }
        }
    }

    fun loadNextPage() {
        currentPage++
        fetchNews(selectedCategory.value, currentPage)
    }

    fun onCategoryChanged(newCategory: String) {
        selectedCategory.value = newCategory
        currentPage = 1
        articles.value = emptyList()

        val apiCategory = when (newCategory) {
            "All" -> "general"
            "Business" -> "business"
            "Media" -> "entertainment"
            "Sports" -> "sports"
            "Tech" -> "technology"
            else -> "general"
        }
        fetchNews(apiCategory)
    }

    private fun fetchNews(category: String, page: Int = 1) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                articles.value += repository.getNewsByCategory(category, page)
                println("KMP_DEBUG: Successfully fetched ${articles.value.size} articles")
            } catch (e: Exception) {
                println("KMP_DEBUG: Error fetching news: ${e.message}")
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

}