package com.example.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.database.BookmarkEntity
import com.example.shared.repository.BookmarkRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookmarkViewModel(private val repository: BookmarkRepository) : ViewModel() {
    val bookmarks = repository.getBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeBookmark(article: BookmarkEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(article, true)
        }
    }

    fun addBookmark(article: BookmarkEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(article, false)
        }
    }

    fun isBookmarked(url: String) = repository.isBookmarked(url)

    fun syncFromCloud() {
        val userId = Firebase.auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val snapshot = Firebase.firestore
                    .collection("users")
                    .document(userId)
                    .collection("bookmarks")
                    .get()

                val cloudBookmarks = snapshot.documents.map { doc ->
                    doc.data<BookmarkEntity>()
                }

                repository.insertFromCloud(cloudBookmarks)
            } catch (e: Exception) {

            }
        }
    }

}