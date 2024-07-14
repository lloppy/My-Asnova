package com.example.asnova.screen.main.feed.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.asnova.data.UserManager
import com.example.asnova.screen.main.feed.api.WallItem
import com.example.asnova.utils.shimmerEffect
import java.text.SimpleDateFormat
import java.util.Locale

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

    var onSuccess by remember { mutableStateOf(false) }
    val isClicked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val defaultImageUrl = "https://sun9-78.userapi.com/impg/Ir5UOUAUw9qczne8EVGjGw_wWvEK_Dsv_awN9Q/qguEM4hhSLA.jpg?size=1953x989&quality=96&sign=86ca45843194e357c1ea8ba559dc6117&type=album"

        SubcomposeAsyncImage(
            model = if (newsItem.images.isNullOrEmpty()) defaultImageUrl else newsItem.images.first().url,
            contentDescription = null,
            modifier = Modifier
                .height(180.dp)
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
        Spacer(Modifier.height(16.dp))

        Text(
            text = newsItem.text,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row {
            Text(
                text = newsItem.date.toString(),
                style = typography.bodySmall
            )
            if (UserManager.status) {
                onClickAddToFavorites()
            }
        }
    }
    if (isClicked) {
        /*ModalBottomSheet(onDismissRequest = {
            isClicked = false
        }) {
            Box(modifier = Modifier.fillMaxSize())
            {
                Column {

                }
            }
        }*/

    }
}