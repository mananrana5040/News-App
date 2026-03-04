package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: NewsViewModel = hiltViewModel()
            NewsAppTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: NewsViewModel) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FD))
    ) {
        TopBar()
        BreakingNewsCard()


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
fun TopBar() {
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
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(15.dp))
        Text(
            "March 4, 2026", style = TextStyle(
                fontSize = 14.sp,
                color = Color(0xFF9A98A5),
                fontWeight = FontWeight.Medium
            )
        )
        Spacer(Modifier.weight(1f))

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF1E293B),
                modifier = Modifier.size(28.dp)
            )
        }
    }

}

@Composable
fun BreakingNewsCard() {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {

        Text(
            "Breaking News", style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF444D70)
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Breaking News is placed in this text with proper image to show bold",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF444D70),
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

                Text(
                    "March 4, 2026", style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF9A98A5),
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Search",
                    tint = Color(0xFF9A98A5),
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    "10 min read", style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF9A98A5),
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(start = 5.dp)
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

    if (loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(news) { items ->
                NewsItem(
                    items.title,
                    items.urlToImage ?: "Null",
                    items.publishedAt,
                    items.author ?: "Author"
                )
            }
        }
    }


}

@Composable
fun NewsItem(
    title: String,
    imageUrl: String,
    date: String,
    author: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            imageUrl,
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
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF444D70),
                ),
                maxLines = 2,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Search",
                    tint = Color(0xFF9A98A5),
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    date, style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF9A98A5),
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Search",
                    tint = Color(0xFF9A98A5),
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    author, style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF9A98A5),
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsAppTheme {
        val viewModel: NewsViewModel = hiltViewModel()
        MainScreen(viewModel)
    }
}

//@Preview
//@Composable
//private fun NewsItemPreview() {
//    NewsItem()
//}