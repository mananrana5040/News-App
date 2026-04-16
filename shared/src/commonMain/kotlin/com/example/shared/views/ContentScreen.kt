package com.example.shared.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.example.shared.database.BookmarkEntity
import com.example.shared.helper.formatDate
import com.example.shared.model.News
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import newsapp.shared.generated.resources.Res
import newsapp.shared.generated.resources.ic_launcher_background
import newsapp.shared.generated.resources.read_full_news
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@Composable
fun ContentScreen(
    onBackClick: () -> Unit,
    news: BookmarkEntity?,
    onReadMoreClick: () -> Unit,
    onShareItem: () -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .statusBarsPadding(),
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


        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
                .clickable(onClick = onShareItem),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(28.dp)
            )
        }
    }

}


@Composable
fun ContentMainCard(news: BookmarkEntity?, onReadMoreClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            news?.urlToImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Crop,
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
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },

            )

        Text(
            text = news?.title ?: "n/a",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
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

            if (news?.author != null) {
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
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .width(100.dp),
                    maxLines = 1
                )
            }
        }

        if (news?.content != null) {
            Text(
                news.content.cleanNewsContent(),
                Modifier.padding(horizontal = 12.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.surface,
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Justify
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onReadMoreClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(Res.string.read_full_news), color = Color.White)
        }


    }

}

fun String.cleanNewsContent(): String {
    val regex = Regex("""\s*\[\+\d+\s+chars]$""")
    return this.replace(regex, "").trim()
}

