package com.example.asnova.screen.main.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.asnova.R
import com.example.asnova.data.UserManager
import com.example.asnova.screen.main.feed.components.NewsArticleCardTop
import com.example.asnova.screen.main.feed.components.PostListDivider
import com.example.asnova.utils.SkeletonScreen
import com.example.asnova.utils.navigation.Router
import com.example.asnova.utils.shimmerEffect

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
    externalRouter: Router,
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
    viewModel: FeedScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    // Refresh
    val isRefreshing by remember { mutableStateOf(false) }
    val stateRefresh = rememberPullRefreshState(isRefreshing, { viewModel.pullToRefresh() })

    // FAB
    val listState = rememberLazyListState()
    val isExpanded by remember { derivedStateOf { listState.firstVisibleItemScrollOffset == 0 } }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.feed),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    /*IconButton(onClick = {  }) {
                        Icon(painter = painterResource(id = R.drawable.baseline_help_outline_24), contentDescription = null)
                    }*/
                })
        },
        floatingActionButton = {
            if (UserManager.status) {
                ExtendedFloatingActionButton(
                    onClick = {
                        //externalRouter.routeTo(Screen.CreateNewArticle.route)
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Edit,
                            stringResource(id = R.string.write_article)
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.write_article)) },
                    expanded = isExpanded
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(top = it.calculateTopPadding(), bottom = 90.dp)
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
                    items(state.value) { item ->
                        NewsArticleCardTop(
                            newsItem = item,
                            modifier = Modifier.clickable(onClick = {
                                //  externalRouter.routeTo("${Screen.NewsArticle.route}/${item.id}")
                            })
                        ) {
                            /*Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = {
                                    viewModel.addNewItemToFavorites(item.id) { callback ->
                                        if(callback.data == true)
                                        {
                                            isClicked = !isClicked
                                        }
                                    }
                                }) {
                                    if(isClicked)
                                    {
                                        isClicked = false
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_bookmark_24),
                                            contentDescription = null
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }*/
                        }
                        PostListDivider()
                    }
                    item {
                        Spacer(modifier = Modifier.padding(24.dp))
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
}