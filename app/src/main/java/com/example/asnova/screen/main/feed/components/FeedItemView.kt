package com.example.asnova.screen.main.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import com.example.asnova.R
import com.example.asnova.screen.main.feed.api.WallItem
import com.example.asnova.ui.theme.Pink80
import com.example.asnova.ui.theme.Purple40
import com.example.asnova.ui.theme.Purple80
import com.example.asnova.ui.theme.PurpleGrey80
import com.example.asnova.ui.theme.fontFamilyInter
import com.example.asnova.ui.theme.grayAsnova


@Composable
fun FeedItemView(
    feedItem: WallItem,
    index: Int,
    onFeedItemClick: (FeedItemUrlInfo) -> Unit
) {
    var showItemMenu by remember {
        mutableStateOf(
            false
        )
    }

    Column(
        modifier = Modifier
            .clickable(
                enabled = true,
                onClick = {
                    onFeedItemClick(
                        FeedItemUrlInfo(
                            id = feedItem.id.toString(),
                            url = feedItem.images.first().url,
                            title = feedItem.title,
                        ),
                    )
                }
            )
//            .combinedClickable(
//
//                onLongClick = {
//                    showItemMenu = true
//                },
//            )
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
    ) {
        FeedSourceAndUnreadDotRow(feedItem, index)

        TitleSubtitleAndImageRow(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            feedItem = feedItem,
        )

        Text(
            text = feedItem.date.toString(),
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodySmall,
        )

        Divider(
            modifier = Modifier
                .padding(top = 16.dp),
            thickness = 0.2.dp,
            color = Color.Gray,
        )

        FeedItemContextMenu(
            showMenu = showItemMenu,
            closeMenu = {
                showItemMenu = false
            },
            feedItem = feedItem
        )
    }
}


@Composable
private fun FeedSourceAndUnreadDotRow(
    feedItem: WallItem,
    index: Int
) {
    fun randomColor(): Color {
        val colors = listOf(
            Purple80,
            PurpleGrey80,
            Pink80
        )
        return colors.random()
    }

    if (feedItem.hashtags.isNotEmpty()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            feedItem.hashtags.forEach { hashtag ->
                Text(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(
                            color = grayAsnova, //randomColor(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .wrapContentWidth()
                        .wrapContentHeight()
                        ,
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


@Composable
private fun TitleSubtitleAndImageRow(
    feedItem: WallItem,
    modifier: Modifier = Modifier,
) {
    val paddingTop = 8.dp

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = feedItem.title,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyInter,
                style = MaterialTheme.typography.titleMedium,
            )

//            Text(
//                modifier = Modifier
//                    .padding(top = paddingTop),
//                text = feedItem.withoutTitle,
//                maxLines = 3,
//                overflow = TextOverflow.Ellipsis,
//                style = MaterialTheme.typography.bodyMedium,
//            )
        }

        FeedItemImage(
            modifier = Modifier.padding(start = 16.dp),
            newsItem = feedItem,
            width = 96.dp,
        )
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

@Composable
private fun FeedItemContextMenu(
    showMenu: Boolean,
    feedItem: WallItem,
    closeMenu: () -> Unit,
) {
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = closeMenu,
        properties = PopupProperties(),
    ) {

        Text(text = "FeedItemContextMenu")
    }
}

data class FeedItemUrlInfo(
    val id: String,
    val url: String,
    val title: String?,
    val openOnlyOnBrowser: Boolean = false,
)