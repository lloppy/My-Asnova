package com.example.asnova.screen.main.feed.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import com.example.asnova.data.UserData
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.unit.dp

@Composable
fun HeaderSection(
    userData: UserData?,
    threeSegments: List<String>,
    selectedSegment: String,
    onSegmentSelected: (String) -> Unit
) {
    Column {
        NewsHeader(userData = userData)
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            SegmentedControl(
                segments = threeSegments,
                selectedSegment = selectedSegment,
                onSegmentSelected = onSegmentSelected,
                modifier = Modifier.height(50.dp)
            ) { SegmentText(it, selectedSegment == it) }
        }
        Spacer(modifier = Modifier.padding(12.dp))
    }
}