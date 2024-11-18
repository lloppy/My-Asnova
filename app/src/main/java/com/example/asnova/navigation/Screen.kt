package com.example.asnova.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.asnova.R

sealed class Screen(val route: String, val iconId: Int, val icon: ImageVector? = null) {
    object Splash : Screen(route = "splash_screen", iconId = R.drawable.ic_launcher_foreground)
    object Main : Screen(route = "main_screen", iconId = R.drawable.ic_launcher_foreground)
    object LogIn : Screen(route = "log_in_screen", iconId = R.drawable.ic_launcher_foreground)
    object Chats : Screen(route = "chats_screen", iconId = R.drawable.ic_launcher_foreground)
    object Greeting : Screen(route = "greeting_screen", iconId = R.drawable.ic_launcher_foreground)
    object ChooseClass : Screen(route = "choose_class_screen", iconId = R.drawable.ic_launcher_foreground)
    object EnterPromocode : Screen(route = "enter_promocode_screen", iconId = R.drawable.ic_launcher_foreground)
    object ChangeGroup : Screen(route = "change_group_screen", iconId = R.drawable.ic_launcher_foreground)
    object EmailSignIn : Screen(route = "email_sign_in_screen", iconId = R.drawable.ic_launcher_foreground)

    object Feed : Screen(route = "feed_screen", iconId = R.drawable.ic_home, icon = Icons.Filled.Home)

    object Schedule : Screen(
        route = "schedule_screen",
        iconId = R.drawable.ic_calendar_simple,
        icon = Icons.Filled.CalendarToday
    )

    object ProfileSettings : Screen(
        route = "profile_settings_screen",
        iconId = R.drawable.ic_person,
        icon = Icons.Filled.Person
    )
}
