package com.example.asnova.navigation

import android.content.Context
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.asnova.screen.chat.ChatScreen
import com.example.asnova.screen.feed.FeedScreen
import com.example.asnova.screen.schedule.ScheduleScreen
import com.example.asnova.screen.settings.ProfileSettingsScreen
import com.example.asnova.screen.settings.components.ChangeGroupScreen
import com.example.asnova.screen.settings.components.EnterPromocodeScreen
import com.example.asnova.screen.settings.components.admin_classes.SelectClassScreen
import com.example.asnova.ui.theme.BottomBarHeight
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.example.bottombar.model.ItemStyle
import com.example.bottombar.model.VisibleItem

@Composable
fun SetupNavGraph(
    context: Context,
    lifecycleScope: LifecycleCoroutineScope,
    navHostController: NavHostController,
    onRestartApp: () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Feed.route
    ) {
        composable(Screen.Feed.route) {
            FeedScreen()
        }
        composable(Screen.Schedule.route) {
            ScheduleScreen(
                context = context
            )
        }
        composable(Screen.ProfileSettings.route) {
            ProfileSettingsScreen(
                context = context,
                lifecycleScope = lifecycleScope,
                navController = navHostController,
                navigateToChats = {
                    navHostController.navigate(Screen.Chats.route)
                },
                navigateToSelectClass = {
                    navHostController.navigate(Screen.ChooseClass.route)
                },
                navigateToChangeGroup = {
                    navHostController.navigate(Screen.ChangeGroup.route)
                },
                navigateToEnterPromocode = {
                    navHostController.navigate(Screen.EnterPromocode.route)
                },
                onRestartApp = onRestartApp
            )
        }
        composable(Screen.Chats.route) {
            ChatScreen()
        }
        composable(Screen.ChooseClass.route) {
            SelectClassScreen(
                context = context
            )
        }
        composable(Screen.EnterPromocode.route) {
            EnterPromocodeScreen(context, navHostController)
        }
        composable(Screen.ChangeGroup.route) {
            ChangeGroupScreen(context, navHostController)
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
        bottomBarHeight = BottomBarHeight,
        modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)),
        selectedItem = selectedItem,
        itemSize = items.size,
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
                imageVector = navigationItem.icon!!,
                label = navigationItem.route,
                visibleItem = VisibleItem.ICON,
                itemStyle = ItemStyle.STYLE4,
                iconColor = Color.Black
            )
        }
    }
}
