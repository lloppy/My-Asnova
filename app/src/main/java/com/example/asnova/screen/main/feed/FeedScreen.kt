package com.example.asnova.screen.main.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.asnova.data.UserData
import com.example.asnova.screen.main.feed.components.FeedItemView
import com.example.asnova.screen.main.feed.components.NewsArticleCardTop
import com.example.asnova.screen.main.feed.components.NewsHeader
import com.example.asnova.screen.main.feed.components.SegmentText
import com.example.asnova.screen.main.feed.components.SegmentedControl
import com.example.asnova.ui.theme.backgroundAsnova
import com.example.asnova.utils.SkeletonScreen
import com.example.asnova.utils.navigation.Router
import com.example.asnova.utils.shimmerEffect

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
    userData: UserData?,
    externalRouter: Router,
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
    viewModel: FeedScreenViewModel = hiltViewModel()
) {
    // FAB
    val listState = rememberLazyListState()

    val state by viewModel.state
    val asnovaNews by viewModel.wallItems.observeAsState()
    val ohranaNews by viewModel.ohranaWallItems.observeAsState(emptyList())

    // Refresh
    val isRefreshing by remember { mutableStateOf(false) }
    val stateRefresh = rememberPullRefreshState(isRefreshing, { viewModel.pullToRefresh() })

    val threeSegments = remember { listOf("Моя группа", "Asnovapro", "Охрана труда") }
    var selectedThreeSegment by remember { mutableStateOf(threeSegments[1]) }

    Box(
        modifier = Modifier
            .background(backgroundAsnova)
            .padding(bottom = 90.dp)
    ) {

        SkeletonScreen(
            isLoading = state.loading,
            skeleton = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(2) {
                        Box(
                            modifier = Modifier
                                .height(180.dp)
                                .fillMaxSize()
                                .clip(shape = MaterialTheme.shapes.medium)
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .height(16.dp)
                                .width(150.dp)
                                .clip(shape = MaterialTheme.shapes.medium)
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .height(16.dp)
                                .width(50.dp)
                                .clip(shape = MaterialTheme.shapes.medium)
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(stateRefresh),
                state = listState
            ) {
                val currentNews = when (selectedThreeSegment) {
                    "Asnovapro" -> asnovaNews
                    "Охрана труда" -> ohranaNews
                    else -> emptyList()
                }

                currentNews?.let { newsList ->
                    items(newsList.size) { index ->
                        if (index != 0) {
                            FeedItemView(
                                feedItem = newsList[index],
                                index = index
                            ) {}
                        } else {
                            NewsHeader(
                                userData = userData,
                            )

                            Column(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp
                                )
                            ) {
                                SegmentedControl(
                                    threeSegments,
                                    selectedThreeSegment,
                                    onSegmentSelected = { selectedThreeSegment = it },
                                    modifier = Modifier.height(50.dp)
                                ) {
                                    SegmentText(it, selectedThreeSegment == it)
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            NewsArticleCardTop(
                                newsItem = newsList[index],
                                modifier = Modifier.clickable(onClick = {
                                    //  externalRouter.routeTo("${Screen.NewsArticle.route}/${item.id}")
                                })
                            ) {}
                            Spacer(modifier = Modifier.padding(24.dp))
                        }
                        // PostListDivider()
                    }
                    item {
                        Spacer(modifier = Modifier.padding(24.dp))
                    }
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
        PullRefreshIndicator(isRefreshing, stateRefresh, Modifier.align(Alignment.TopCenter))
    }
}