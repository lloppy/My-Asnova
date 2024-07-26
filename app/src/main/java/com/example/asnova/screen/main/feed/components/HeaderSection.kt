package com.example.asnova.screen.main.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.asnova.model.User
import com.example.asnova.navigation.bottomBarHeight

@Composable
fun HeaderSection(
    userData: User?,
    threeSegments: List<String>,
    selectedSegment: String,
    pictureBackgroundId: Int,
    onSegmentSelected: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val linear = Brush.linearGradient(
        listOf(
            Color.Black.copy(alpha = 1f),
            Color.Black.copy(alpha = 0.8f),
            Color.Black.copy(alpha = 0.5f),
            Color.Black.copy(alpha = 0.3f),
            Color.Transparent
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .paint(
                painterResource(id = pictureBackgroundId),
                contentScale = ContentScale.Crop
            )
            .background(linear)
            .height(
                screenHeight
                    .minus(bottomBarHeight)
                    .minus(FeedItemHeight)
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NewsHeader(userData = userData)

        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
            SegmentedControl(
                segments = threeSegments,
                selectedSegment = selectedSegment,
                onSegmentSelected = onSegmentSelected,
                modifier = Modifier.height(50.dp)
            ) { SegmentText(it, selectedSegment == it) }
        }
    }
}