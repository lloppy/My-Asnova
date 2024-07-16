package com.example.asnova.screen.main.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.asnova.screen.main.feed.api.WallItem
import com.example.asnova.ui.theme.grayAsnova
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date

@Composable
fun FeedItemView(
    feedItem: WallItem,
    index: Int,
    onFeedItemClick: (FeedItemUrlInfo) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 28.dp)
            .padding(vertical = 12.dp)
            .clickable(
                onClick = {
                    onFeedItemClick(
                        FeedItemUrlInfo(
                            id = feedItem.id.toString(),
                            url = feedItem.images.first().url,
                            title = feedItem.title,
                        )
                    )
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            FeedItemImage(
                modifier = Modifier.weight(0.5f),
                newsItem = feedItem,
                width = 96.dp,
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = feedItem.title,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = formatRelativeDate(feedItem.date),
                    modifier = Modifier.padding(top = 4.dp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                )
                HashtagsRow(feedItem)
            }
        }
    }
}

@Composable
private fun HashtagsRow(
    feedItem: WallItem
) {
    if (feedItem.hashtags.isNotEmpty()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            feedItem.hashtags.forEachIndexed { index, hashtag ->
                if (feedItem.hashtags.size == 1 || index != 0) {
                    Text(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(
                                color = grayAsnova,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        text = hashtag,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun FeedItemImage(
    newsItem: WallItem,
    width: Dp,
    modifier: Modifier = Modifier,
) {
    if (LocalInspectionMode.current) {
        Box(
            modifier = modifier
                .size(width)
                .background(Color.Green),
        )
    } else {
        AsyncImage(
            model = newsItem.images.first().url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(width)
                .clip(RoundedCornerShape(8.dp)),
        )
    }
}

data class FeedItemUrlInfo(
    val id: String,
    val url: String,
    val title: String?,
    val openOnlyOnBrowser: Boolean = false,
)

private fun formatRelativeDate(date: Date): String {
    val now = LocalDate.now()
    val givenDate = Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDate()

    val daysBetween = ChronoUnit.DAYS.between(givenDate, now).toInt()

    return when (daysBetween) {
        0 -> "Сегодня"
        1 -> "Вчера"
        2 -> "Позавчера"
        3, 4 -> "$daysBetween дня назад"
        else -> "$daysBetween дней назад"
    }
}