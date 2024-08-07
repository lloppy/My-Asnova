package com.example.asnova.screen.main.schedule

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.People
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.asnova.model.Resource
import com.asnova.model.User
import com.example.asnova.R
import com.example.asnova.navigation.bottomBarHeight
import com.example.asnova.screen.main.schedule.components.DateBox
import com.example.asnova.screen.main.schedule.components.GroupScheduleItem
import com.example.asnova.screen.main.schedule.components.ScheduleHeader
import com.example.asnova.screen.main.schedule.components.ScheduleScreenSkeleton
import com.example.asnova.screen.main.schedule.components.SiteScheduleItem
import com.example.asnova.ui.theme.grayAsnova
import com.example.asnova.utils.SkeletonScreen
import com.example.asnova.utils.navigation.Router
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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

    val toastIdCopied =
        Toast.makeText(context, stringResource(id = R.string.id_copied), Toast.LENGTH_SHORT)
    val toastACCopied = Toast.makeText(
        context,
        stringResource(id = R.string.access_code_copied),
        Toast.LENGTH_SHORT
    )

    // Refresh
    val isRefreshing by remember { mutableStateOf(false) }
    val stateRefresh = rememberPullRefreshState(isRefreshing, { viewModel.pullToRefresh() })

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var clickedItemId by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

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

    Column(Modifier.padding(bottom = bottomBarHeight)) {
        Box(Modifier.fillMaxSize()) {
            SkeletonScreen(
                isLoading = state.loading,
                skeleton = { ScheduleScreenSkeleton() }
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .pullRefresh(stateRefresh)
                ) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(0.dp)) //topStart = 0.dp, topEnd = 0.dp, bottomEnd = 16.dp, bottomStart = 16.dp))
                                    .height(
                                        screenHeight
                                            .minus(bottomBarHeight)
                                            .div(4)
                                    )
                                    .paint(
                                        painterResource(id = R.drawable.asnova_future_gen),
                                        contentScale = ContentScale.Crop,
                                    )
                                    .background(linear),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ScheduleHeader(userData = userData)

                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp, bottom = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Switch(
                                        checked = checked,
                                        onCheckedChange = {
                                            checked = it
                                            if (it) viewModel.loadScheduleForGroup() else viewModel.loadScheduleFromSite()
                                        },
                                        thumbContent = {
                                            Icon(
                                                imageVector = if (checked) Icons.Filled.CalendarMonth else Icons.Filled.People,
                                                contentDescription = null,
                                                modifier = Modifier.size(SwitchDefaults.IconSize)
                                            )
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedIconColor = Color.Black,
                                            checkedTrackColor = Color.Black.copy(alpha = 0.5f),
                                            checkedThumbColor = Color.White.copy(alpha = 0.8f),
                                            uncheckedTrackColor = grayAsnova.copy(alpha = 0.3f),
                                            uncheckedBorderColor = Color.Transparent,
                                            uncheckedThumbColor = Color.Black.copy(alpha = 0.6f)
                                        )
                                    )
                                }
                            }
                        }
                    }
                    if (checked) {
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
                        items(state.value) { item ->
                            GroupScheduleItem(
                                item,
                                clipboardManager,
                                toastIdCopied,
                                toastACCopied,
                                context
                            )
                        }
                    } else {
                        items(state.valueFromSite) { item ->
                            SiteScheduleItem(
                                item = item,
                                onItemClick = {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(item.newsLink)
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
                if (state.error.isNotBlank()) {
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
                PullRefreshIndicator(
                    isRefreshing,
                    stateRefresh,
                    Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

val linear = Brush.linearGradient(
    listOf(
        Color.Black.copy(alpha = 1f),
        Color.Black.copy(alpha = 0.9f),
        Color.Black.copy(alpha = 0.8f),
        Color.Black.copy(alpha = 0.7f),
        Color.Black.copy(alpha = 0.5f)
    )
)

