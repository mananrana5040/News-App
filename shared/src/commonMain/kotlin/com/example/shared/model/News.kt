package com.example.shared.model

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