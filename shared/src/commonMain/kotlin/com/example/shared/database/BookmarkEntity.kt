package com.example.shared.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shared.PlatformSerializable
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "bookmarks",
    indices = [Index(value = ["url"], unique = true)]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String,
    val title: String,
    val author: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
): PlatformSerializable