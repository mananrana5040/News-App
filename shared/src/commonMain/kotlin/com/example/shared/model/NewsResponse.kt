package com.example.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val articles: List<News>
)
