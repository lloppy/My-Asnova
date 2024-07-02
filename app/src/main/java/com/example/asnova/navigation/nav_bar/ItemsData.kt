package com.example.asnova.navigation.nav_bar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import com.example.asnova.R

@Stable
data class Item(
    @DrawableRes val icon: Int,
    var isSelected: Boolean,
    val showNewMessages: Boolean,
    val badgeCount: Int? = null,
    val route: String,
    @StringRes val description: Int,
    val animationType: ColorButtonAnimation = BellColorButton(
        tween(500),
        background = ButtonBackground(R.drawable.ic_launcher_foreground)
    )
)


