package com.example.shared.model

import com.example.shared.database.BookmarkEntity
import kotlinx.serialization.Serializable


@Serializable
data class News(
    val title: String,
    val author: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    val url: String
)


fun News.toBookmarkEntity(): BookmarkEntity {
    return BookmarkEntity(
        url = this.url,
        title = this.title,
        author = this.author,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )
}