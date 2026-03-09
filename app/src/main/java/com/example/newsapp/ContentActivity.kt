package com.example.newsapp

import android.R.attr.onClick
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsapp.model.News
import com.example.newsapp.ui.theme.NewsAppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import androidx.core.net.toUri

class ContentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val article = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("article_data", News::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<News>("article_data")
        }

        setContent {
            NewsAppTheme {
                ContentScreen(
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

@Composable
fun ContentScreen(
    onBackClick: () -> Unit,
    news: News?,
    onReadMoreClick: () -> Unit,
    onShareItem: () -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FD))
            .statusBarsPadding()
    ) {

        ContentTopBar(onBackClick = onBackClick, onShareItem = onShareItem)
        ContentMainCard(news = news, onReadMoreClick = onReadMoreClick)
    }

}

@Composable
fun ContentTopBar(onBackClick: () -> Unit, onShareItem: () -> Unit) {
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
                .background(Color.White)
                .clickable(true, onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color(0xFF1E293B),
                modifier = Modifier.size(28.dp)
            )
        }


        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(onClick = onShareItem),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "",
                tint = Color(0xFF1E293B),
                modifier = Modifier.size(28.dp)
            )
        }
    }

}


@Composable
fun ContentMainCard(news: News?, onReadMoreClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            news?.urlToImage ?: R.drawable.ic_launcher_background,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = news?.title ?: "Breaking News Loading",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF444D70),
            ),
            maxLines = 2,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
        )

        Row(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Search",
                tint = Color(0xFF9A98A5),
                modifier = Modifier.size(18.dp)
            )

            val formatDate = formatDate(news?.publishedAt ?: "2026-03-05T04:43:00Z")

            Text(
                formatDate.toString(), style = TextStyle(
                    fontSize = 14.sp,
                    color = Color(0xFF9A98A5),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(start = 5.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "",
                tint = Color(0xFF9A98A5),
                modifier = Modifier.size(18.dp)
            )

            Text(
                news?.author ?: "Author", style = TextStyle(
                    fontSize = 14.sp,
                    color = Color(0xFF9A98A5),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .padding(start = 5.dp)
                    .width(100.dp),
                maxLines = 1
            )
        }

        val lorumIpsum =
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
        Text(
            news?.content ?: lorumIpsum, Modifier.padding(horizontal = 12.dp), style = TextStyle(
                fontSize = 18.sp,
                color = Color(0xFF9A98A5),
                lineHeight = 26.sp,
                textAlign = TextAlign.Justify
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onReadMoreClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) {
            Text("Read Full News")
        }


    }

}

@Composable
private fun formatDate(date: String): Any? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val date = inputFormat.parse(date)
    val formatDate = date?.let { outputFormat.format(it) } ?: date
    return formatDate
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    NewsAppTheme {
        ContentScreen(onBackClick = {}, news = null, onReadMoreClick = {}, onShareItem = {})
    }
}