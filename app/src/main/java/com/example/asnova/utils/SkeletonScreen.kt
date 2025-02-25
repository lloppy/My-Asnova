package com.example.asnova.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.example.asnova.ui.theme.grayAsnova
import com.example.asnova.ui.theme.greenAsnova
import com.example.asnova.ui.theme.lightGreenAsnova

// Паттерн Decorator
@Composable
fun SkeletonScreen(
    isLoading: Boolean,
    skeleton: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (isLoading)
        skeleton()
    else
        content()
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                // lightGreenAsnova,
                grayAsnova,
                Color.White,
                grayAsnova
                //  lightGreenAsnova,
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width, size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}