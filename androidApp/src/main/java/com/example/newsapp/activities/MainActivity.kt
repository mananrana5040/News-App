package com.example.newsapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.preferences.ThemeManager
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.shared.model.News
import com.example.shared.viewmodel.NewsViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeManager = ThemeManager(this)

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
                    })
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: NewsViewModel,
    onBreakingCardClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        TopBar(onSettingClick = onSettingClick)

        val breakingNews = viewModel.breakingNews.value
        BreakingNewsCard(breakingNews, onBreakingCardClick = onBreakingCardClick)


        val currentCategory = viewModel.selectedCategory.value
        Categories(
            currentCategory,
            onCategoryClick = { clickedCategory ->
                viewModel.onCategoryChanged(clickedCategory)
            })


        NewsList(viewModel)
    }
}

@Composable
fun TopBar(onSettingClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.img_user),
            contentDescription = null,
            modifier = Modifier
                .size(45.dp)
                .clickable(enabled = true, onClick = onSettingClick)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(15.dp))

        val currentDate = SimpleDateFormat(
            "MMM dd, yyyy",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)

        Text(
            currentDate, style = TextStyle(
                fontSize = 14.sp,
                color = Color(0xFF9A98A5),
                fontWeight = FontWeight.Medium
            )
        )
//        Spacer(Modifier.weight(1f))
//
//        IconButton(onClick = {}) {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Search",
//                tint = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier.size(28.dp)
//            )
//        }
    }

}

@Composable
fun BreakingNewsCard(news: News?, onBreakingCardClick: () -> Unit) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {

        Text(
            "Breaking News", style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onBreakingCardClick
                )
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
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
            )

            Row(
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 20.dp),
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
        }

    }

}

@Composable
fun Categories(
    selectedCategory: String,
    onCategoryClick: (String) -> Unit
) {

    val categoryList = listOf("All", "Business", "Sports", "Tech", "Media")

    LazyRow(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {

        items(categoryList) { category ->
            val isSelected = category == selectedCategory
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onCategoryClick(category)
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFF3B82F6) else Color.Transparent)
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color(0xFF3B82F6) else Color(0xFF9A98A5)
                    )
                )
            }

        }
    }

}

@Composable
fun NewsList(viewModel: NewsViewModel) {

    val news = viewModel.articles.value
    val loading = viewModel.isLoading.value


    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(
            news.drop(1),
            key = { article -> article.urlToImage ?: article.title }) { items ->
            NewsItem(
                items
            )
        }

        item {
            if (loading) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF3B82F6))
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Load More News",
                        style = TextStyle(color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold),
                        modifier = Modifier.clickable {
                            viewModel.loadNextPage()
                        }
                    )
                }
            }

        }
    }


}

@Composable
fun NewsItem(
    news: News

) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable(onClick = {
                val intent = Intent(context, ContentActivity::class.java)
                val newsJson = Json.encodeToString(news)
                intent.putExtra("article_data", newsJson)
                context.startActivity(intent)
            }),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            news.urlToImage ?: "null",
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = news.title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                maxLines = 2,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "",
                    tint = Color(0xFF9A98A5),
                    modifier = Modifier.size(18.dp)
                )

                val formatDate = formatDate(news.publishedAt)

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
                    news.author ?: "author", style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF9A98A5),
                        fontWeight = FontWeight.Medium,
                    ),
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .width(80.dp)
                )
            }

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
fun GreetingPreview() {
    NewsAppTheme {
        val viewModel = koinViewModel<NewsViewModel>()
        MainScreen(viewModel = viewModel, onBreakingCardClick = {}, onSettingClick = {})
    }
}

//@Preview
//@Composable
//private fun NewsItemPreview() {
//    NewsItem()
//}