package com.example.newsapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.News
import com.example.newsapp.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository): ViewModel() {
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
                Log.e("TAG", "Breaking News Error: ${e.message}")
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

            } catch (e: Exception) {
                Log.d("ITEMS", "${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }
}