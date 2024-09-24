package com.example.asnova.screen.main.schedule.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asnova.model.AsnovaSchedule
import com.example.asnova.ui.theme.grayAsnova

@Composable
fun GroupScheduleItem(
    item: AsnovaSchedule,
    context: Context
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp, vertical = 12.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(color = MaterialTheme.colorScheme.onSecondary)
        .border(width = 2.dp, color = grayAsnova, shape = RoundedCornerShape(12.dp))
        .clickable {

        }) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${item.startTime} - ${item.endTime}",
                modifier = Modifier.padding(start = 4.dp),
                color = Color.Gray,
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Text(
                    text = item.trimmedSummary,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    modifier = Modifier
                        .background(
                            color = grayAsnova,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    text = "· Аудитория №" + item.classroomNumber,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}