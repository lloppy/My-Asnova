package com.example.asnova.screen.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.asnova.model.User
import com.example.asnova.R
import com.example.asnova.screen.schedule.ScheduleScreenViewModel
import com.example.asnova.ui.theme.BottomBarHeight
import com.example.asnova.ui.theme.blackShadesLinear

@Composable
fun ScheduleHeaderSegment(
    userData: User?,
    screenHeight: Dp,
    onScheduleChange: (Boolean) -> Unit
) {
    val words = listOf("Расписание Аснова", "Расписание сайта")
    val pagerState = rememberPagerState(pageCount = { words.size })

    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            0 -> onScheduleChange(true)
            1 -> onScheduleChange(false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(0.dp))
            .height(screenHeight.minus(BottomBarHeight).div(4).plus(20.dp))
            .paint(painterResource(id = R.drawable.asnova_future_gen), contentScale = ContentScale.Crop)
            .background(blackShadesLinear),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp, top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth(0.8f)) { page ->
                    Text(
                        text = words[page],
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
                if (userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = stringResource(R.string.profile_picture),
                        modifier = Modifier.size(40.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}