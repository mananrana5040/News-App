package com.example.newsapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.preferences.ThemeManager
import com.example.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.launch

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeManager = ThemeManager(this)

        setContent {

            val isDarkThemePref by themeManager.isDarkMode.collectAsState(initial = null)
            val systemTheme = isSystemInDarkTheme()
            val finalThemeValue = isDarkThemePref ?: systemTheme

            NewsAppTheme(darkTheme = finalThemeValue) {
                SettingScreen(
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

@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
    currentTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {

        ContentTopBar(onBackClick = onBackClick, currentTheme, onThemeChange = onThemeChange)
    }
}

@Composable
fun ContentTopBar(
    onBackClick: () -> Unit,
    currentTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
                .clickable(true, onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            "Setting",
            Modifier.padding(start = 16.dp),
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "Dark Mode",
            Modifier.padding(start = 8.dp),
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Switch(
            checked = currentTheme, colors = SwitchDefaults.colors(
                checkedTrackColor = Color(0xFF3B82F6)
            ),
            onCheckedChange = { onThemeChange(it) }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    NewsAppTheme {
        SettingScreen(onBackClick = {}, false, onThemeChange = {})
    }
}