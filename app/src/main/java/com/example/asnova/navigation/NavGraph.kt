package com.example.asnova.navigation

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.asnova.MainActivity
import com.example.asnova.screen.log_in.services.GoogleAuthUiClient
import com.example.asnova.screen.main.feed.FeedScreen
import com.example.asnova.screen.main.profile_settings.ProfileSettingsScreen
import com.example.asnova.screen.main.schedule.ScheduleScreen
import com.example.asnova.utils.navigation.Router
import com.example.asnova.utils.toastMessage
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.example.bottombar.model.ItemStyle
import com.example.bottombar.model.VisibleItem
import kotlinx.coroutines.launch

@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
    context: Context,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: LifecycleCoroutineScope,
    lifecycleOwner: LifecycleOwner,
    router: Router
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Feed.route
    ) {
        composable(Screen.Feed.route) {
            FeedScreen(
                userData = googleAuthUiClient.getSignedInUser(),
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
    var selectedItem by remember { mutableIntStateOf(0) }

    AnimatedBottomBar(
        bottomBarHeight = 90.dp,
        modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)),
        selectedItem = selectedItem,
        itemSize = items.take(3).size,
        contentColor = Color.White,
        indicatorColor = Color.Black,
        indicatorStyle = IndicatorStyle.LINE,
        containerShape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        indicatorDirection = IndicatorDirection.BOTTOM,
        containerColor = Color.White,
    ) {
        items.forEachIndexed { index, navigationItem ->
            val selected = index == selectedItem
            BottomBarItem(
                activeIndicatorColor = Color.Transparent,
                modifier = Modifier.padding(22.dp)
                    .padding(start = if (index == 0) 30.dp else 0.dp)
                    .padding(end = if (index == 2) 30.dp else 0.dp),
                selected = selected,
                onClick = {
                    if (items[selectedItem].route != navigationItem.route) {
                        selectedItem = index
                        navController.navigate(items[selectedItem].route) {
                            popUpTo(Screen.LogIn.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                imageVector = navigationItem.icon!!, //ImageVector.vectorResource(id = navigationItem.iconId),
                label = navigationItem.route.toString(),
                visibleItem = VisibleItem.ICON,
                itemStyle = ItemStyle.STYLE4,
                iconColor = Color.Black
            )
        }
    }
}