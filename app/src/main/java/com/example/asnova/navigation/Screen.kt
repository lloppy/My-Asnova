package com.example.asnova.navigation

import com.example.asnova.R

sealed class Screen(val route: String, val iconId: Int) {
    object Splash : Screen(route = "splash_screen", iconId = R.drawable.ic_launcher_foreground)
    object LogIn : Screen(route = "log_in_screen", iconId = R.drawable.ic_launcher_foreground)
    object Feed : Screen(route = "feed_screen", iconId = R.drawable.ic_feed)
    object Schedule : Screen(route = "schedule_screen", iconId = R.drawable.ic_schedule)
    object ProfileSettings : Screen(route = "profile_settings_screen", iconId = R.drawable.ic_person)
}
