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

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository): ViewModel() {
    var articles = mutableStateOf<List<News>>(emptyList())
    var isLoading = mutableStateOf(false)
    var selectedCategory = mutableStateOf("All")

    init {
        fetchNews("general")
    }

    fun onCategoryChanged(newCategory: String) {
        selectedCategory.value = newCategory

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

    private fun fetchNews(category: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                articles.value = repository.getNewsByCategory(category)
            } catch (e: Exception) {
                Log.d("TAG", "${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }
}