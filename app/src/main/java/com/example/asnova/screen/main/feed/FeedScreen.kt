package com.example.asnova.screen.main.feed

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.asnova.model.Resource
import com.asnova.model.Role
import com.asnova.model.User
import com.example.asnova.R
import com.example.asnova.data.UserManager
import com.example.asnova.navigation.bottomBarHeight
import com.example.asnova.screen.main.feed.components.FeedItemHeight
import com.example.asnova.screen.main.feed.components.FeedItemView
import com.example.asnova.screen.main.feed.components.HeaderSection
import com.example.asnova.screen.main.feed.components.Segments
import com.example.asnova.ui.theme.backgroundAsnova
import com.example.asnova.utils.SkeletonScreen
import com.example.asnova.utils.navigation.Router
import com.example.asnova.utils.shimmerEffect

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
    externalRouter: Router,
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
    viewModel: FeedScreenViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val state by viewModel.state

    val isRefreshing by remember { mutableStateOf(false) }
    val stateRefresh = rememberPullRefreshState(isRefreshing, { viewModel.pullToRefresh() })

    var selectedThreeSegment by remember { mutableStateOf(Segments.WORK_PROFESSIONS) }

    val context = LocalContext.current
    var userData by remember { mutableStateOf<User?>(null) }

    val segmentsForCurrentUser =
        if (UserManager.isStudentOrWorker()) {
            Segments.all
        } else {
            Segments.forVisitor
        }

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

    Box(
        modifier = Modifier
            .background(backgroundAsnova)
            .padding(bottom = bottomBarHeight)
    ) {
        SkeletonScreen(
            isLoading = state.loading,
            skeleton = {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        HeaderSection(
                            userData = userData,
                            segments = segmentsForCurrentUser,
                            selectedSegment = selectedThreeSegment,
                            pictureBackgroundId = R.drawable.asnova_future_gen,
                            onSegmentSelected = {
                                selectedThreeSegment = it
                                viewModel.onSegmentChange(it)
                            }
                        )
                    }
                    items(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .padding(vertical = 12.dp)
                                .height(FeedItemHeight)
                                .clip(shape = MaterialTheme.shapes.medium)
                                .shimmerEffect()
                        )
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
                item {
                    HeaderSection(
                        userData = userData,
                        segments = segmentsForCurrentUser,
                        selectedSegment = selectedThreeSegment,
                        pictureBackgroundId = R.drawable.asnova_future_gen,
                        onSegmentSelected = {
                            selectedThreeSegment = it
                            viewModel.onSegmentChange(it)
                        }
                    )
                }
                items(state.news.size) { index ->
                    FeedItemView(
                        feedItem = state.news[index],
                        index = index
                    ) {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(state.news[index].postUrl))
                        context.startActivity(intent)
                    }

                    if (index == state.news.size - 1) {
                        viewModel.onDownloadMore()
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(22.dp))
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