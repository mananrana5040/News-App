package com.example.newsapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.shared.preference.ThemeManager
import com.example.shared.views.AuthScreen
import com.example.shared.views.SignUpScreen
import org.koin.android.ext.android.inject
import kotlin.getValue

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeManager: ThemeManager by inject()

        setContent {

            val isDarkThemePref by themeManager.isDarkMode.collectAsState(initial = null)
            val systemTheme = isSystemInDarkTheme()
            val finalThemeValue = isDarkThemePref ?: systemTheme

            val navController = rememberNavController()
            NewsAppTheme(darkTheme = finalThemeValue) {
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        AuthScreen(onNavigateToSignup = { navController.navigate("signup") })
                    }
                    composable("signup") {
                        SignUpScreen(onNavigateToLogin = { navController.navigate("login") })
                    }
                }
            }
        }
    }
}

