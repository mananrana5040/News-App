package com.example.shared.views

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.shared.database.BookmarkEntity
import com.example.shared.helper.formatDate
import com.example.shared.viewmodel.BookmarkViewModel
import newsapp.shared.generated.resources.Res
import newsapp.shared.generated.resources.bookmarks
import newsapp.shared.generated.resources.no_bookmarks
import org.jetbrains.compose.resources.stringResource

@Preview
@Composable
fun Preview(){
    BookmarkTopBar(onBackClick = { /*TODO*/ })
}

@Composable
fun BookmarkScreen(
    onBackClick: () -> Unit,
    bookmarkViewModel: BookmarkViewModel,
    onBookmarkItemClick: (BookmarkEntity) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {

        BookmarkTopBar(onBackClick = onBackClick)
        BookmarkList(bookmarkViewModel = bookmarkViewModel, onBookmarkItemClick = onBookmarkItemClick)
    }
}

@Composable
fun BookmarkTopBar(onBackClick: () -> Unit) {
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
            text = stringResource(Res.string.bookmarks),
            Modifier.padding(start = 24.dp),
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

}

@Composable
fun BookmarkList(bookmarkViewModel: BookmarkViewModel, onBookmarkItemClick: (BookmarkEntity) -> Unit) {

    val bookmarkList by bookmarkViewModel.bookmarks.collectAsState(initial = emptyList())

    if (bookmarkList.isEmpty()){
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(text = stringResource(Res.string.no_bookmarks), style = TextStyle(color = Color(0xFF9A98A5), fontWeight = FontWeight.Bold, fontSize = 17.sp))
        }
    }

    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(items = bookmarkList, key = { it.url }) { bookmark ->
            BookmarkItem(
                bookmark,
                onBookmarkItemClick = onBookmarkItemClick,
                removeBookmark = {
                    bookmarkViewModel.toggleBookmark(bookmark, true)
                }
            )
        }
    }


}

@Composable
fun BookmarkItem(
    bookmark: BookmarkEntity,
    onBookmarkItemClick: (BookmarkEntity) -> Unit,
    removeBookmark: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable(onClick = { onBookmarkItemClick(bookmark) }),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            bookmark.urlToImage,
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = bookmark.title,
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

                val formatDate = formatDate(bookmark.publishedAt)

                Text(
                    formatDate, style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF9A98A5),
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "",
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(18.dp).clickable {
                        removeBookmark()
                    }
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "",
                    tint = Color(0xFF9A98A5),
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    bookmark.author ?: "author", style = TextStyle(
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
