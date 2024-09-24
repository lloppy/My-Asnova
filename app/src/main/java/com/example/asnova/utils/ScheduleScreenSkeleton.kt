package com.example.asnova.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.asnova.utils.shimmerEffect

@Composable
fun ScheduleScreenSkeleton() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(3)
        {
            Box(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .height(16.dp)
                    .width(50.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .height(120.dp)
                    .fillMaxWidth()
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}