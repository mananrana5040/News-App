package com.example.newsapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.shared.preference.ThemeManager
import com.example.shared.viewmodel.NewsViewModel
import com.example.shared.views.MainScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeManager: ThemeManager by inject()

        setContent {
            val viewModel = koinViewModel<NewsViewModel>()

            val isDarkThemePref by themeManager.isDarkMode.collectAsState(initial = null)
            val systemTheme = isSystemInDarkTheme()
            val finalThemeValue = isDarkThemePref ?: systemTheme

            NewsAppTheme(darkTheme = finalThemeValue) {
                MainScreen(
                    viewModel,
                    onBreakingCardClick = {
                        val intent = Intent(this, ContentActivity::class.java)
                        val newsJson = Json.encodeToString(viewModel.breakingNews.value)
                        intent.putExtra("article_data", newsJson)
                        startActivity(intent)
                    },
                    onSettingClick = {
                        val intent = Intent(this, SettingActivity::class.java)
                        startActivity(intent)
                    },
                    onNewsItemClick = { news ->
                        val intent = Intent(this, ContentActivity::class.java)
                        val newsJson = Json.encodeToString(news)
                        intent.putExtra("article_data", newsJson)
                        startActivity(intent)
                    })
            }
        }
    }
}
