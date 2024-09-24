package com.example.asnova.screen.main.schedule

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.asnova.model.Resource
import com.asnova.model.User
import com.example.asnova.screen.main.schedule.components.DateBox
import com.example.asnova.screen.main.schedule.components.GroupScheduleItem
import com.example.asnova.screen.main.schedule.components.ScheduleHeader
import com.example.asnova.screen.main.schedule.components.SiteScheduleItem
import com.example.asnova.ui.theme.BottomBarHeight
import com.example.asnova.utils.Router
import com.example.asnova.utils.ScheduleScreenSkeleton
import com.example.asnova.utils.SkeletonScreen
import java.time.LocalDate


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleScreen(
    externalRouter: Router,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    viewModel: ScheduleScreenViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val context = LocalContext.current
    var userData by remember { mutableStateOf<User?>(null) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // Refresh
    val isRefreshing by remember { mutableStateOf(false) }
    val stateRefresh = rememberPullRefreshState(isRefreshing, { viewModel.pullToRefresh() })

    val currentDate = LocalDate.now()
    val dateList = List(7) { index -> currentDate.plusDays(index.toLong()) }

    var checked by remember { mutableStateOf(true) } // MY_GROUP

    LaunchedEffect(Unit) {
        viewModel.getUserData { resource ->
            when (resource) {
                is Resource.Success -> {
                    userData = resource.data
                }

                is Resource.Error -> {
                    // Handle error
                }

                else -> {}
            }
        }
    }

    Box(modifier = Modifier
        .padding(bottom = BottomBarHeight)
        .fillMaxSize()) {
        SkeletonScreen(
            isLoading = state.loading,
            skeleton = { ScheduleScreenSkeleton(userData, screenHeight) }
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .pullRefresh(stateRefresh)
            )
            {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        ScheduleHeader(userData, screenHeight)
                    }
                }

                if (viewModel.canLoadPrivateSchedule()) {
                    item {
                        LazyRow(Modifier.padding(horizontal = 24.dp)) {
                            items(dateList) { date ->
                                DateBox(
                                    date = date,
                                    currentDate = currentDate,
                                    onDateSelected = { selectedDate ->
                                        viewModel.saveDate(selectedDate)
                                        viewModel.loadScheduleForGroup()
                                    }
                                )
                            }
                        }
                    }
                    items(state.privateSchedule) { item ->
                        GroupScheduleItem(item, context)
                    }
                } else if (!viewModel.canLoadPrivateSchedule()) {
                    items(state.siteSchedule) { item ->
                        SiteScheduleItem(item) {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(item.newsLink)
                                )
                            )
                        }
                    }
                }

                if (state.error.isNotBlank()) {
                    item {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
        PullRefreshIndicator(isRefreshing, stateRefresh, Modifier.align(Alignment.TopCenter))
    }
}