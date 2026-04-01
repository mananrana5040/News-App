package com.example.shared.helper

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Splash : Screen("splash")
    object Main : Screen("main")
    object Setting : Screen("setting")
}