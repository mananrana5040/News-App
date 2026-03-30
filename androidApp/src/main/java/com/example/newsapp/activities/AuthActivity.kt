package com.example.newsapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.shared.helper.Screen
import com.example.shared.preference.ThemeManager
import com.example.shared.viewmodel.AuthViewModel
import com.example.shared.viewmodel.BookmarkViewModel
import com.example.shared.views.LoginScreen
import com.example.shared.views.SignUpScreen
import dev.gitlive.firebase.auth.auth
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val themeManager: ThemeManager by inject()
        val viewModel: AuthViewModel by inject()
        val bookmarkViewModel: BookmarkViewModel by inject()


        setContent {

            val isDarkThemePref by themeManager.isDarkMode.collectAsState(initial = null)
            val systemTheme = isSystemInDarkTheme()
            val finalThemeValue = isDarkThemePref ?: systemTheme

            val navController = rememberNavController()
            NewsAppTheme(darkTheme = finalThemeValue) {
                NavHost(navController = navController, startDestination = Screen.Login.route) {
                    composable(Screen.Login.route) {
                        LoginScreen(
                            onNavigateToSignup = { navController.navigate(Screen.Signup.route) },
                            onLogin = {
                                bookmarkViewModel.syncFromCloud()
                                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                                finish()
                            },
                            viewModel = viewModel
                        )
                    }
                    composable(Screen.Signup.route) {
                        SignUpScreen(
                            onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                            onSignUp = {
                                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                                finish()
                            },
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}