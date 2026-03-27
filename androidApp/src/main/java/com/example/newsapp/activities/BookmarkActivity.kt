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
import com.example.shared.viewmodel.BookmarkViewModel
import com.example.shared.views.BookmarkScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject
import org.koin.compose.viewmodel.koinViewModel

class BookmarkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeManager: ThemeManager by inject()

        setContent {
            val isDarkThemePref by themeManager.isDarkMode.collectAsState(initial = null)
            val systemTheme = isSystemInDarkTheme()
            val finalThemeValue = isDarkThemePref ?: systemTheme

            val bookmarkViewModel = koinViewModel<BookmarkViewModel>()


            NewsAppTheme(darkTheme = finalThemeValue) {
                BookmarkScreen(
                    onBackClick = {
                        finish()
                    },
                    bookmarkViewModel,
                    onBookmarkItemClick = {bookmarkEntity ->
                        val intent = Intent(this, ContentActivity::class.java)
                        val newsJson = Json.encodeToString(bookmarkEntity)
                        intent.putExtra("article_data", newsJson)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

