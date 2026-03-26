package com.example.newsapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.net.toUri
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.shared.model.News
import com.example.shared.preference.ThemeManager
import com.example.shared.views.ContentScreen
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject
import kotlin.getValue

class ContentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val jsonString = intent.getStringExtra("article_data")
        val article = jsonString?.let { Json.decodeFromString<News>(it) }

        val themeManager: ThemeManager by inject()

        setContent {
            val isDarkThemePref by themeManager.isDarkMode.collectAsState(initial = null)
            val systemTheme = isSystemInDarkTheme()
            val finalThemeValue = isDarkThemePref ?: systemTheme
            NewsAppTheme(darkTheme = finalThemeValue) {
                ContentScreen (
                    onBackClick = {
                    finish()
                }, article, onReadMoreClick = {
                    val intent = Intent(Intent.ACTION_VIEW, article?.url?.toUri())
                    startActivity(intent)
                },
                    onShareItem = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Check out this news: ${article?.title}\n\nRead more at: ${article?.url}")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share News via")
                        startActivity(shareIntent)
                    })
            }
        }
    }
}
