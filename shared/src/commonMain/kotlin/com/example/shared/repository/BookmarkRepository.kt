package com.example.shared.repository

import com.example.shared.database.BookmarkDao
import com.example.shared.database.BookmarkEntity


class BookmarkRepository(private val bookmarkDao: BookmarkDao) {

    fun getBookmarks() = bookmarkDao.getAllBookmarks()

    suspend fun toggleBookmark(article: BookmarkEntity, isBookmarked: Boolean) {
        if (isBookmarked) {
            bookmarkDao.deleteBookmark(article)
        } else {
            bookmarkDao.insertBookmark(article)
        }
    }

    fun isBookmarked(url: String) = bookmarkDao.isBookmarked(url)

}