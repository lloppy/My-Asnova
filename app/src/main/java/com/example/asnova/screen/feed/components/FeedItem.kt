package com.example.asnova.screen.feed.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.asnova.model.FeedItemUrlInfo
import com.asnova.model.WallItem
import com.example.asnova.ui.theme.FeedItemBoxHeight
import com.example.asnova.ui.theme.FeedItemHeight
import com.example.asnova.ui.theme.FeedItemImageHeight
import com.example.asnova.ui.theme.grayAsnova
import com.example.asnova.utils.formatRelativeDate
import com.example.asnova.utils.formatTime

@Composable
fun FeedItemView(
    feedItem: WallItem,
    index: Int,
    onFeedItemClick: (FeedItemUrlInfo) -> Unit
) {
    Column(
        modifier = Modifier
            .height(FeedItemBoxHeight)
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .clip(RoundedCornerShape(8.dp))
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
            modifier = Modifier
                .fillMaxWidth()
                .height(FeedItemHeight),
            verticalAlignment = Alignment.Top
        ) {
            FeedItemImage(
                imageUrl = feedItem.images.first().url,
                width = FeedItemImageHeight,
                modifier = Modifier
                    .weight(1.1f)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 10.dp, end = 10.dp, top = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Column {
                    Text(
                        text = feedItem.title,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HashtagsRow(feedItem)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatRelativeDate(feedItem.date) + "  ·  ${formatTime(feedItem.date)}",
                    modifier = Modifier.padding(start = 4.dp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                )
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
    imageUrl: String,
    width: Dp,
    modifier: Modifier = Modifier
) {
    if (LocalInspectionMode.current) {
        Box(
            modifier = modifier
                .size(width)
                .background(Color.Green)
        )
    } else {
        val context = LocalContext.current

        if (imageUrl.startsWith("resource://")) {
            val resourceId = context.resources.getIdentifier(
                imageUrl.removePrefix("resource://"),
                "drawable",
                context.packageName
            )
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = "news image",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .height(width)
                    .width(width)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = "news image",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .height(width)
                    .width(width)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}
