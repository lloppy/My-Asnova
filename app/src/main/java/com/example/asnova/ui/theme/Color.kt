package com.example.asnova.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val orangeMaterial = Color(0xFFF5982A)
val greenAsnova = Color(0xFF4dbd38) // 4dbd38
val lightGreenAsnova = Color(0x1A4DBD38)
val grayAsnova = Color(0xFFf0f2f4) // f2f3f4
val backgroundAsnova = Color(0xFFf6f8fa) // f6f8fa
val darkAsnova = Color(0xFF1A1C1A) // 1A1C1A


val blackShadesLinear = Brush.linearGradient(
    listOf(
        Color.Black.copy(alpha = 1f),
        Color.Black.copy(alpha = 0.9f),
        Color.Black.copy(alpha = 0.8f),
        Color.Black.copy(alpha = 0.7f),
        Color.Black.copy(alpha = 0.5f)
    )
)

val darkLinear = listOf(
    Color(0xFF282C27),
    Color(0xFF181A18),
    Color(0xFF1A1C1A),
    Color(0xFF1D1F1E),
    Color(0xFF18231B)
)