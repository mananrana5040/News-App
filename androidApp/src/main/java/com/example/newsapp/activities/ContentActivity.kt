package com.example.newsapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.shared.database.BookmarkEntity
import com.example.shared.model.News
import com.example.shared.model.toBookmarkEntity
import com.example.shared.preference.ThemeManager
import com.example.shared.views.ContentScreen
import com.example.shared.views.ContentTopBar
import org.koin.android.ext.android.inject

class ContentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val article: BookmarkEntity
        if (intent.hasExtra("article_data")) {
            val news = intent.getSerializableExtra("article_data") as News
            article = news.toBookmarkEntity()
        } else {
            article = intent.getSerializableExtra("bookmark_data") as BookmarkEntity
        }

        val themeManager: ThemeManager by inject()

        setContent {
            val isDarkThemePref by themeManager.isDarkMode.collectAsState(initial = null)
            val systemTheme = isSystemInDarkTheme()
            val finalThemeValue = isDarkThemePref ?: systemTheme

            var showWebView by remember { mutableStateOf(false) }

            NewsAppTheme(darkTheme = finalThemeValue) {
                if (showWebView) {
                    WebViewScreen(
                        url = article.url,
                        onBack = { showWebView = false },
                        shareItem = {
                            shareArticle(article)
                        }
                    )
                } else {
                    ContentScreen(
                        onBackClick = {
                            finish()
                        }, article,
                        onReadMoreClick = {
                            showWebView = true
                        },
                        onShareItem = {
                            shareArticle(article)
                        })
                }

            }
        }
    }

    @Composable
    fun WebViewScreen(url: String, onBack: () -> Unit, shareItem: () -> Unit) {
        var progress by remember { mutableIntStateOf(0) }
        BackHandler {
            onBack()
        }

        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            topBar = {
                ContentTopBar(onBackClick = onBack, onShareItem = shareItem)
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    progress = newProgress
                                }
                            }
                            loadUrl(url)
                        }
                    },
                    update = { webView ->
                        if (webView.url != url) {
                            webView.loadUrl(url)
                        }
                    }
                )

                if (progress < 100) {
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF3B82F6),
                        trackColor = Color.LightGray
                    )
                }
            }

        }
    }
}

fun Context.shareArticle(article: BookmarkEntity) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out this news: ${article.title}\n\nRead more at: ${article.url}"
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share News via")
    startActivity(shareIntent)
}
