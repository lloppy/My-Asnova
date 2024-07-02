package com.example.asnova.navigation

import com.example.asnova.R

sealed class Route(val route: String, val iconId: Int) {
    object Splash : Route(route = "splash_screen", iconId = R.drawable.ic_launcher_foreground)
    object LogIn : Route(route = "log_in_screen", iconId = R.drawable.ic_launcher_foreground)
    object Feed : Route(route = "feed_screen", iconId = R.drawable.ic_feed)
    object Schedule : Route(route = "schedule_screen", iconId = R.drawable.ic_schedule)
    object ProfileSettings : Route(route = "profile_settings_screen", iconId = R.drawable.ic_person)
}
