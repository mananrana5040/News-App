package com.example.shared.views

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.BrokenImage
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.example.shared.helper.currentDateDisplay
import com.example.shared.helper.formatDate
import com.example.shared.model.News
import com.example.shared.model.toBookmarkEntity
import com.example.shared.viewmodel.BookmarkViewModel
import com.example.shared.viewmodel.NewsViewModel
import newsapp.shared.generated.resources.Res
import newsapp.shared.generated.resources.breaking_news
import newsapp.shared.generated.resources.img_user
import newsapp.shared.generated.resources.load_more_news
import newsapp.shared.generated.resources.retry
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun MainScreen(
    viewModel: NewsViewModel,
    bookmarkViewModel: BookmarkViewModel,
    onBreakingCardClick: () -> Unit,
    onSettingClick: () -> Unit,
    onNewsItemClick: (News) -> Unit,
    onBookMarkClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        TopBar(onSettingClick = onSettingClick, onBookMarkClick = onBookMarkClick)

        val breakingNews by viewModel.breakingNews.collectAsState()
        BreakingNewsCard(breakingNews, onBreakingCardClick = onBreakingCardClick, bookmarkViewModel)


        val currentCategory by viewModel.selectedCategory.collectAsState()
        Categories(
            currentCategory,
            onCategoryClick = { clickedCategory ->
                viewModel.onCategoryChanged(clickedCategory)
            })


        NewsList(viewModel, bookmarkViewModel, onNewsItemClick = onNewsItemClick)
    }
}

@Composable
fun TopBar(onSettingClick: () -> Unit, onBookMarkClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.img_user),
            contentDescription = null,
            modifier = Modifier
                .size(45.dp)
                .clickable(enabled = true, onClick = onSettingClick)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(15.dp))

        val currentDate = currentDateDisplay()

        Text(
            currentDate, style = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.surface,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.clickable(enabled = true, onClick = onSettingClick)
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
                .clickable(true, onClick = onBookMarkClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Bookmarks,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(28.dp)
            )
        }
    }

}

@Composable
fun BreakingNewsCard(
    news: News?,
    onBreakingCardClick: () -> Unit,
    bookmarkViewModel: BookmarkViewModel
) {
    val isCurrentItemBookmarked by bookmarkViewModel.isBookmarked(news?.url ?: "")
        .collectAsState(initial = false)

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {

        Text(
            stringResource(Res.string.breaking_news), style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onBreakingCardClick)
            ) {

                SubcomposeAsyncImage(
                    news?.urlToImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    error = {
                        Box(
                            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.BrokenImage,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    },

                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize().background(color = Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(30.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },


                    contentScale = ContentScale.Crop
                )

                Text(
                    text = news?.title ?: "n/a",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                )

                Row(
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(18.dp)
                    )

                    val formatDate = formatDate(news?.publishedAt ?: "n/a")

                    Text(
                        formatDate, style = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.surface,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(start = 5.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "",
                        tint = if (isCurrentItemBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(18.dp).clickable {
                            news?.let { bookmarkViewModel.onBookmarkClicked(it) }
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(18.dp)
                    )

                    Text(
                        news?.author ?: "n/a", style = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.surface,
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
            .padding(horizontal = 24.dp, vertical = 16.dp),
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
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    )
                )
            }

        }
    }

}

@Composable
fun NewsList(
    viewModel: NewsViewModel,
    bookmarkViewModel: BookmarkViewModel,
    onNewsItemClick: (News) -> Unit
) {

    val news by viewModel.articles.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(
            news.drop(1),
            key = { article -> article.urlToImage ?: article.title }) { items ->
            val isCurrentItemBookmarked by bookmarkViewModel.isBookmarked(items.url)
                .collectAsState(initial = false)
            NewsItem(
                items,
                isBookmarked = isCurrentItemBookmarked,
                onNewsItemClick = onNewsItemClick,
                onBookmarkToggle = {
                    bookmarkViewModel.onBookmarkClicked(items)
                }
            )
        }

        item {
            if (loading) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (error != null){
                        Text(
                            text = "$error\n" + stringResource(Res.string.retry),
                            textAlign = TextAlign.Center,
                            style = TextStyle(color = MaterialTheme.colorScheme.onError, fontWeight = FontWeight.Bold),
                            modifier = Modifier.clickable {
                                viewModel.loadNextPage()
                            }
                        )
                    }else{
                        Text(
                            text = stringResource(Res.string.load_more_news),
                            style = TextStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold),
                            modifier = Modifier.clickable {
                                viewModel.loadNextPage()
                            }
                        )
                    }

                }
            }

        }
    }


}

@Composable
fun NewsItem(
    news: News,
    isBookmarked: Boolean,
    onNewsItemClick: (News) -> Unit,
    onBookmarkToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable(onClick = { onNewsItemClick(news) }),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SubcomposeAsyncImage(
            news.urlToImage,
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(16.dp)),
            error = {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.BrokenImage, contentDescription = null, tint = Color.Gray)
                }
            },
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize().background(color = Color.LightGray),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
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
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(18.dp)
                )

                val formatDate = formatDate(news.publishedAt)

                Text(
                    formatDate, style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "",
                    tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(18.dp).clickable {
                        onBookmarkToggle()
                    }
                )
                Spacer(modifier = Modifier.weight(1f))

                if (news.author != null) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(18.dp)
                    )

                    Text(
                        news.author, style = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.surface,
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
}

