package com.example.asnova.navigation

import android.content.Context
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.asnova.MainActivity
import com.example.asnova.screen.log_in.GoogleAuthUiClient
import com.example.asnova.screen.main.MainScreenViewModel
import com.example.asnova.screen.main.feed.FeedScreen
import com.example.asnova.screen.main.profile_settings.ProfileSettingsScreen
import com.example.asnova.screen.main.schedule.ScheduleScreen
import com.example.asnova.ui.theme.orangeMaterial
import com.example.asnova.utils.navigation.Router
import com.example.asnova.utils.toastMessage
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import kotlinx.coroutines.launch

@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
    context: Context,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: LifecycleCoroutineScope,
    lifecycleOwner: LifecycleOwner,
    router: Router,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Feed.route
    ) {
        composable(Screen.Feed.route) {
            FeedScreen(
                externalRouter = router,
                navController = navHostController,
                lifecycleOwner = lifecycleOwner
            )
        }
        composable(Screen.Schedule.route) {
            ScheduleScreen(
                externalRouter = router,
                context = context,
                lifecycleOwner = lifecycleOwner
            )
        }
        composable(Screen.ProfileSettings.route) {
            ProfileSettingsScreen(
                externalRouter = router,
                context = context,
                lifecycleScope = lifecycleScope,
                lifecycleOwner = lifecycleOwner,
                userData = googleAuthUiClient.getSignedInUser(),
                onSignOut = {
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                        toastMessage(
                            context, "Выход из аккаунта завершен"
                        )
                    }
                    if (context is MainActivity) {
                        context.restartApp()
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Feed,
        Screen.Schedule,
        Screen.ProfileSettings
    )
    var selectedItem by remember { mutableIntStateOf(1) } // это расписание)

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
        items.forEachIndexed { index, item ->
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
                    navController.navigate(item.route) {
                        popUpTo(Screen.LogIn.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


const val Duration = 500
const val DoubleDuration = 1000