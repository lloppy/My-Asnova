package com.example.asnova.screen.main.feed.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.asnova.screen.main.feed.api.WallItem
import com.example.asnova.ui.theme.grayAsnova
import com.example.asnova.utils.shimmerEffect
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.verticalScroll
import coil.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SimpleDateFormat")
@Composable
fun NewsArticleCardTop(
    newsItem: WallItem,
    modifier: Modifier,
    onClickAddToFavorites: @Composable () -> Unit
) {
    val typography = MaterialTheme.typography

    val sdf = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())
    //  val formattedDate = sdf.format(Date(newsItem.date * 1000))

    var isClicked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { isClicked = true }
    ) {
        val defaultImageUrl =
            "https://sun9-78.userapi.com/impg/Ir5UOUAUw9qczne8EVGjGw_wWvEK_Dsv_awN9Q/qguEM4hhSLA.jpg?size=1953x989&quality=96&sign=86ca45843194e357c1ea8ba559dc6117&type=album"

        SubcomposeAsyncImage(
            model = if (newsItem.images.isNullOrEmpty()) defaultImageUrl else newsItem.images.first().url,
            contentDescription = null,
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerEffect()
                )
            }
        )
//        Spacer(Modifier.height(16.dp))
//
//        Text(
//            text = newsItem.title,
//            maxLines = 2,
//            overflow = TextOverflow.Ellipsis,
//            style = typography.titleLarge,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Row {
//            Text(
//                text = newsItem.date.toString(),
//                style = typography.bodySmall
//            )
//            if (UserManager.status) {
//                onClickAddToFavorites()
//            }
//        }
    }
    if (isClicked) {
        ModalBottomSheet(
            onDismissRequest = {
            isClicked = false
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .padding(bottom = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            )
            {
                if (newsItem.hashtags.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        newsItem.hashtags.forEach { hashtag ->
                            Text(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(
                                        color = grayAsnova, //randomColor(),
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


                val gridSize = when(newsItem.images.size){
                    1 -> 1
                    2, 4 -> 2
                    3, 5, 6 -> 3
                    else -> 3
                }

                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(gridSize),
                    contentPadding = PaddingValues(6.dp),
                    content = {
                        items(newsItem.images.size) { index ->
                            SubcomposeAsyncImage(
                                model = newsItem.images[index].url,
                                contentDescription = null,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .shimmerEffect()
                                    )
                                }
                            )
                        }
                    }
                )
                Text(text = newsItem.withoutTitle)

            }
        }
    }
}