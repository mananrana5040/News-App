package com.example.shared.helper

object CategoryMapper {
    fun mapToApiCategory(uiCategory: String): String {
        return when (uiCategory) {
            "All" -> "general"
            "Business" -> "business"
            "Media" -> "entertainment"
            "Sports" -> "sports"
            "Tech" -> "technology"
            else -> "general"
        }
    }
}