package com.example.asnova.screen.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.asnova.model.ScheduleAsnovaSite
import com.example.asnova.ui.theme.SiteScheduleItemHeight
import com.example.asnova.ui.theme.SiteSchedulePictureHeight

@Composable
fun SiteScheduleItem(
    item: ScheduleAsnovaSite,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .height(SiteScheduleItemHeight)
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onItemClick(item.newsLink) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(SiteSchedulePictureHeight),
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
                    .background(Color.Black.copy(alpha = 0.35f))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 10.dp, end = 10.dp, top = 6.dp),
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
