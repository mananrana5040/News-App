package com.example.shared.helper

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Splash : Screen("splash")
    object Main : Screen("main")
    object Setting : Screen("setting")
    object Bookmark : Screen("bookmark")
}

object FirestorePaths {
    const val COLLECTION_USERS= "users"
    const val COLLECTION_BOOKMARKS = "bookmarks"
}