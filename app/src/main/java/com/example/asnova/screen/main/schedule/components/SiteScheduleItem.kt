package com.example.asnova.screen.main.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.asnova.model.AsnovaSiteSchedule
import com.example.asnova.screen.main.feed.components.FeedItemHeight

@Composable
fun SiteScheduleItem(
    item: AsnovaSiteSchedule,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .height(FeedItemHeight.div(3).times(2))
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onItemClick(item.newsLink) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(FeedItemHeight.div(2).plus(12.dp)),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = "schedule site image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.6f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.2f))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 10.dp, end = 10.dp, top = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = item.description,
                    color = Color.Black,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${item.dateRange} · ${item.timeRange}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}
