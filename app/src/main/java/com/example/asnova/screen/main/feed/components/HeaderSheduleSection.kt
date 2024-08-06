package com.example.asnova.screen.main.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.asnova.model.User
import com.example.asnova.navigation.bottomBarHeight
import com.example.asnova.screen.main.schedule.components.ScheduleHeader

@Composable
fun HeaderScheduleSection(
    userData: User?,
    pictureBackgroundId: Int,
    onSegmentSelected: (Boolean) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val linear = Brush.linearGradient(
        listOf(
            Color.Black.copy(alpha = 1f),
            Color.Black.copy(alpha = 0.9f),
            Color.Black.copy(alpha = 0.8f),
            Color.Black.copy(alpha = 0.7f),
            Color.Black.copy(alpha = 0.5f)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomEnd = 16.dp,
                    bottomStart = 16.dp
                )
            )
            .height(
                screenHeight.minus(bottomBarHeight).div(4)
            )
            .paint(
                painterResource(id = pictureBackgroundId),
                contentScale = ContentScale.Crop,
            )
            .background(linear),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScheduleHeader(userData = userData)

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            SwitchWithIcon(onSegmentSelected = onSegmentSelected)
        }
    }
}

@Composable
fun SwitchWithIcon(onSegmentSelected: (Boolean) -> Unit) {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
            onSegmentSelected(it)
        },
        thumbContent = {
            Icon(
                imageVector = if (checked) Icons.Filled.CalendarMonth else Icons.Filled.People,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize)
            )
        }
    )
}
