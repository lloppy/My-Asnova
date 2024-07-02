package com.example.asnova.navigation

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.asnova.SharedViewModel
import com.example.asnova.screen.feed.FeedScreen
import com.example.asnova.screen.log_in.LogInScreen
import com.example.asnova.screen.profile_settings.ProfileSettingsScreen
import com.example.asnova.screen.schedule.ScheduleScreen
import com.example.asnova.screen.splash.SplashScreen
import com.example.asnova.ui.theme.orangeMaterial
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.items.dropletbutton.DropletButton

@Composable
fun SetupNavGraph(navHostController: NavHostController, viewModel: SharedViewModel) {
    val dropletButtons = listOf(
        Route.Feed,
        Route.Schedule,
        Route.ProfileSettings
    )

    var selectedItem by remember { mutableStateOf(0) }

    AnimatedNavigationBar(
        modifier = Modifier
            .padding(bottom = 8.dp, start = 10.dp, end = 10.dp)
            .height(80.dp),
        selectedIndex = selectedItem,
        ballColor = Color.Black,
        barColor = Color.Black,
        cornerRadius = shapeCornerRadius(25.dp),
        ballAnimation = Parabolic(tween(Duration, easing = LinearOutSlowInEasing)),
        indentAnimation = Height(
            indentWidth = 56.dp,
            indentHeight = 15.dp,
            animationSpec = tween(
                DoubleDuration,
                easing = { OvershootInterpolator().getInterpolation(it) })
        )
    ) {
        dropletButtons.forEachIndexed { index, item ->
            DropletButton(
                modifier = Modifier.fillMaxSize(),
                isSelected = selectedItem == index,
                icon = item.iconId,
                dropletColor = orangeMaterial,
                animationSpec = tween(
                    durationMillis = Duration,
                    easing = LinearEasing
                ),
                onClick = {
                    selectedItem = index
                    navHostController.navigate(item.route) {
                        popUpTo(Route.LogIn.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }

    NavHost(
        navController = navHostController,
        startDestination = Route.Feed.route
    ) {
        composable(Route.Splash.route) {
            SplashScreen(navHostController = navHostController)
        }
        composable(Route.LogIn.route) {
            LogInScreen(viewModel = viewModel)
        }
        composable(Route.Feed.route) {
            FeedScreen(viewModel = viewModel)
        }
        composable(Route.Schedule.route) {
            ScheduleScreen(viewModel = viewModel)
        }
        composable(Route.ProfileSettings.route) {
            ProfileSettingsScreen(viewModel = viewModel)
        }
    }
}

const val Duration = 500
const val DoubleDuration = 1000