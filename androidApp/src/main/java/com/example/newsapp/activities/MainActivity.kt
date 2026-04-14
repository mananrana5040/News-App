package com.example.newsapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.extensions.isInternetAvailable
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.shared.config.ApiKeyManager
import com.example.shared.helper.Screen
import com.example.shared.preference.ThemeManager
import com.example.shared.viewmodel.AuthViewModel
import com.example.shared.viewmodel.BookmarkViewModel
import com.example.shared.viewmodel.NewsViewModel
import com.example.shared.views.BookmarkScreen
import com.example.shared.views.LoginScreen
import com.example.shared.views.MainScreen
import com.example.shared.views.SettingScreen
import com.example.shared.views.SignUpScreen
import com.example.shared.views.SplashScreen
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.compose.viewmodel.koinViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeManager: ThemeManager by inject()
        val viewModel: AuthViewModel by inject()
        val bookmarkViewModel: BookmarkViewModel by inject()
        val apiKeyManager: ApiKeyManager by inject()

        lifecycleScope.launch {
            apiKeyManager.initializeRemoteKeys()
        }

        if (!isInternetAvailable()){
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show()
        }

        setContent {
            val isDarkThemePref by themeManager.isDarkMode.collectAsState(initial = null)
            val systemTheme = isSystemInDarkTheme()
            val finalThemeValue = isDarkThemePref ?: systemTheme
            SideEffect {
                enableEdgeToEdge(
                    statusBarStyle = if (finalThemeValue) {
                        SystemBarStyle.dark(Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                    }
                )
            }

            BackHandler(enabled = true) {
                finish()
            }

            val navController = rememberNavController()
            NewsAppTheme(darkTheme = finalThemeValue) {
                NavHost(navController = navController, startDestination = Screen.Splash.route) {
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            onSplashFinished = {
                                val auth = Firebase.auth
                                val currentUser = auth.currentUser
                                if (currentUser != null) {
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                } else {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                }

                            }
                        )
                    }

                    composable(Screen.Login.route) {
                        LoginScreen(
                            onNavigateToSignup = {
                                navController.navigate(Screen.Signup.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            },
                            onLogin = {
                                bookmarkViewModel.syncFromCloud()
                                navController.navigate(Screen.Main.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            },
                            viewModel = viewModel
                        )
                    }

                    composable(Screen.Signup.route) {
                        SignUpScreen(
                            onNavigateToLogin = {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Signup.route) { inclusive = true }

                                }
                            },
                            onSignUp = {
                                navController.navigate(Screen.Main.route) {
                                    popUpTo(Screen.Signup.route) { inclusive = true }
                                }
                            },
                            viewModel = viewModel
                        )
                    }

                    composable(Screen.Main.route) {
                        val viewModel = koinViewModel<NewsViewModel>()
                        val bookmarkViewModel = koinViewModel<BookmarkViewModel>()
                        MainScreen(
                            viewModel,
                            bookmarkViewModel,
                            onBreakingCardClick = {
                                if (viewModel.breakingNews.value != null){
                                    val intent = Intent(this@MainActivity, ContentActivity::class.java)
                                    intent.putExtra("article_data", viewModel.breakingNews.value)
                                    startActivity(intent)
                                }else{
                                    Toast.makeText(this@MainActivity, "News not loaded. Check Internet Connection.", Toast.LENGTH_SHORT).show()
                                }

                            },
                            onSettingClick = {
                                navController.navigate(Screen.Setting.route)
                            },
                            onNewsItemClick = { news ->
                                val intent = Intent(this@MainActivity, ContentActivity::class.java)
                                intent.putExtra("article_data", news)
                                startActivity(intent)
                            },
                            onBookMarkClick = {
                                navController.navigate(Screen.Bookmark.route)
                            })

                    }

                    composable(Screen.Setting.route) {
                        SettingScreen(
                            onBackClick = {
                                navController.popBackStack()
                            },
                            currentTheme = finalThemeValue,
                            onThemeChange = { newValue ->
                                lifecycleScope.launch {
                                    themeManager.setDarkMode(newValue)
                                }
                            },
                            email = Firebase.auth.currentUser?.email ?: "",
                            onSignOut = {
                                viewModel.logout()
                                finish()
                                val intent =
                                    Intent(this@MainActivity, MainActivity::class.java).apply {
                                        flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                startActivity(intent)
                            }
                        )
                    }

                    composable(Screen.Bookmark.route) {
//                        bookmarkViewModel.syncFromCloud()
                        BookmarkScreen(
                            onBackClick = {
                                navController.popBackStack()
                            },
                            bookmarkViewModel,
                            onBookmarkItemClick = {bookmarkEntity ->
                                val intent = Intent(this@MainActivity, ContentActivity::class.java)
                                intent.putExtra("bookmark_data", bookmarkEntity)
                                startActivity(intent)
                            }
                        )
                    }
                }

            }
        }
    }

}
