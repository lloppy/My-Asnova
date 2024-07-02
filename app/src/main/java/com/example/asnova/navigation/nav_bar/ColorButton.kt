package com.example.asnova.navigation.nav_bar

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset

data class ButtonBackground(
    @DrawableRes val icon: Int,
    val offset: DpOffset = DpOffset.Zero
)

@Stable
abstract class ColorButtonAnimation(
    open val animationSpec: FiniteAnimationSpec<Float> = tween(10000),
    open val background: ButtonBackground,
) {
    @Composable
    abstract fun AnimatingIcon(
        modifier: Modifier,
        isSelected: Boolean,
        isFromLeft: Boolean,
        icon: Int,
    )
}
