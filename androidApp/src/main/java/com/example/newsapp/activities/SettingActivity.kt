package com.example.newsapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.shared.preference.ThemeManager
import com.example.shared.views.SettingScreen
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeManager: ThemeManager by inject()
        setContent {

            val isDarkThemePref by themeManager.isDarkMode.collectAsState(initial = null)
            val systemTheme = isSystemInDarkTheme()
            val finalThemeValue = isDarkThemePref ?: systemTheme

            NewsAppTheme(darkTheme = finalThemeValue) {
                SettingScreen (
                    onBackClick = {
                        finish()
                    },
                    currentTheme = finalThemeValue,
                    onThemeChange = { newValue ->
                        lifecycleScope.launch {
                            themeManager.setDarkMode(newValue)
                        }
                    })
            }
        }
    }
}