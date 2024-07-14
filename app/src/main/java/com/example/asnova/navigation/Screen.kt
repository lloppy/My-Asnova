package com.example.asnova.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.asnova.R

sealed class Screen(val route: String, val iconId: Int, val icon: ImageVector? = null) {
    object Splash : Screen(route = "splash_screen", iconId = R.drawable.ic_launcher_foreground)
    object Main : Screen(route = "main_screen", iconId = R.drawable.ic_launcher_foreground)
    object LogIn : Screen(route = "log_in_screen", iconId = R.drawable.ic_launcher_foreground)
    object Feed :
        Screen(route = "feed_screen", iconId = R.drawable.ic_home, icon = Icons.Filled.Home)

    object Schedule : Screen(
        route = "schedule_screen",
        iconId = R.drawable.ic_schedule,
        icon = Icons.Filled.AccessTime
    )

    object ProfileSettings : Screen(
        route = "profile_settings_screen",
        iconId = R.drawable.ic_person,
        icon = Icons.Filled.Person
    )
}
