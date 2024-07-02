package com.example.asnova.screen.feed.components

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
import com.example.asnova.data.NewsItem
import com.example.asnova.data.UserManager
import com.example.asnova.utils.shimmerEffect
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SimpleDateFormat")
@Composable
fun NewsArticleCardTop(
    newsItem: NewsItem,
    modifier: Modifier,
    onClickAddToFavorites: @Composable () -> Unit
) {
    val typography = MaterialTheme.typography

    val sdf = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())
    val formattedDate = sdf.format(Date(newsItem.published * 1000))

    var onSuccess by remember { mutableStateOf(false) }
    val isClicked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SubcomposeAsyncImage(
            model = newsItem.image,
            contentDescription = null,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop,
            loading = {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect())
            }
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = newsItem.title,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row {
            Text(
                text = "$formattedDate.",
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