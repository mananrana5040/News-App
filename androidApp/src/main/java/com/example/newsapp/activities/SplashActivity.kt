package com.example.newsapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.R
import com.example.newsapp.preferences.ThemeManager
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.shared.views.SplashScreen
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeManager = ThemeManager(this)
        setContent {

            val isDarkThemePref by themeManager.isDarkMode.collectAsState(initial = null)
            val systemTheme = isSystemInDarkTheme()
            val finalThemeValue = isDarkThemePref ?: systemTheme

            NewsAppTheme(darkTheme = finalThemeValue) {
                SplashScreen {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }

        }


    }
}
//
//@Composable
//fun SplashScreen() {
//
//    Column(
//        Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Image(
//            painter = painterResource(R.drawable.splash_icon),
//            contentDescription = null,
//            modifier = Modifier
//                .size(120.dp)
//                .clip(RoundedCornerShape(24.dp)),
//            contentScale = ContentScale.Crop
//        )
//        Spacer(Modifier.padding(top = 16.dp))
//        Text(
//            text = "News App",
//            style = TextStyle(
//                fontSize = 22.sp,
//                fontWeight = FontWeight.ExtraBold,
//                color = MaterialTheme.colorScheme.onBackground
//            )
//        )
//    }
//
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview3() {
//    NewsAppTheme {
//        SplashScreen()
//    }
//}