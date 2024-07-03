package com.example.asnova.utils.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable

@ExperimentalAnimationApi
@Composable
fun HorizontalAnimatedVisibility(content: @Composable () -> Unit)
{
    AnimatedVisibility(visible = true) {
        content()
    }
    /*AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(initialOffsetX = { 50 }) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutHorizontally (targetOffsetX = { 0 }) + fadeOut(targetAlpha = 0.3f),
        content = content,
        initiallyVisible = false
    )*/
}

@ExperimentalAnimationApi
@Composable
fun PresentModal(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + fadeOut(),
        content = content,
        initiallyVisible = false
    )
}

@ExperimentalAnimationApi
@Composable
fun PresentNested(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutHorizontally() + fadeOut(),
        content = content,
        initiallyVisible = false
    )
}