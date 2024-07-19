package com.example.asnova.screen.main.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.asnova.data.UserData
import com.example.asnova.navigation.bottomBarHeight

@Composable
fun HeaderSection(
    userData: UserData?,
    threeSegments: List<String>,
    selectedSegment: String,
    onSegmentSelected: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .height(screenHeight.minus(bottomBarHeight).minus(FeedItemHeight))
            .background(Color.Green),
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