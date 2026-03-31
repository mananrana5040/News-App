package com.example.shared.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shared.PlatformSerializable
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val url: String,
    val title: String,
    val author: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
): PlatformSerializable