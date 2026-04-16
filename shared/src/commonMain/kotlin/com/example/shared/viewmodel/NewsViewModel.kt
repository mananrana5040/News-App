package com.example.shared.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.helper.CategoryMapper
import com.example.shared.model.News
import com.example.shared.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _articles = MutableStateFlow<List<News>>(emptyList())
    val articles: StateFlow<List<News>> = _articles.asStateFlow()
    private val _breakingNews = MutableStateFlow<News?>(null)
    val breakingNews: StateFlow<News?> = _breakingNews.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    var currentPage = 1

    init {
        fetchNews("general")
    }


    fun loadNextPage() {
        currentPage++
        fetchNews(_selectedCategory.value, currentPage)
    }

    fun onCategoryChanged(newCategory: String) {
        _selectedCategory.value = newCategory
        currentPage = 1
        _articles.value = emptyList()

        val apiCategory = CategoryMapper.mapToApiCategory(newCategory)
        fetchNews(apiCategory)
    }

    private fun fetchNews(category: String, page: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _articles.value += repository.getNewsByCategory(category, page)
                _breakingNews.value = _articles.value[0]
                println("KMP_DEBUG: Successfully fetched ${_articles.value.size} articles")
            } catch (e: Exception) {
                println("KMP_DEBUG: Error fetching news: ${e.message}")
                _error.value = e.message ?: "An unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

}