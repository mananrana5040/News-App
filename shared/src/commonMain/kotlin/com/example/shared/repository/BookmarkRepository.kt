package com.example.shared.repository

import com.example.shared.database.BookmarkDao
import com.example.shared.database.BookmarkEntity
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore


class BookmarkRepository(private val bookmarkDao: BookmarkDao) {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    fun getBookmarks() = bookmarkDao.getAllBookmarks()

    suspend fun toggleBookmark(article: BookmarkEntity, isBookmarked: Boolean) {

        val userId = auth.currentUser?.uid ?: return
        val docRef = firestore.collection("users")
            .document(userId)
            .collection("bookmarks")
            .document(article.url.hashCode().toString())

        if (isBookmarked) {
            bookmarkDao.deleteBookmark(article.url)
            try { docRef.delete() } catch (e: Exception) { println("Error: $e") }
        } else {
            bookmarkDao.insertBookmark(article)
            try { docRef.set(article) } catch (e: Exception) { println("Error: $e") }
        }
    }

    fun isBookmarked(url: String) = bookmarkDao.isBookmarked(url)

    suspend fun insertFromCloud(bookmarks: List<BookmarkEntity>) {
        bookmarks.forEach { bookmarkDao.insertBookmark(it) }
    }

    suspend fun clearLocalData() {
        bookmarkDao.clearAllBookmarks()
    }

}