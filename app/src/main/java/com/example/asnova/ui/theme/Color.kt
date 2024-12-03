package com.example.asnova.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val GreenGrey80 = Color(0xFFC2DCC3)
val GreenGrey40 = Color(0xFF5B715C)

val orangeMaterial = Color(0xFFF5982A)
val greenAsnova = Color(0xFF4dbd38) // 4dbd38
val darkGreenAsnova = Color(0xFF013222) // 013222
val lightGreenAsnova = Color(0x1A4DBD38)
val grayAsnova = Color(0xFFf0f2f4) // f2f3f4
val backgroundAsnova = Color(0xFFf6f8fa) // f6f8fa
val darkAsnova = Color(0xFF1A1C1A) // 1A1C1A
val neonGreenAsnova = Color(0xFF80F988)


val blackShadesLinear = Brush.linearGradient(
    listOf(
        Color.Black.copy(alpha = 1f),
        Color.Black.copy(alpha = 0.9f),
        Color.Black.copy(alpha = 0.8f),
        Color.Black.copy(alpha = 0.7f),
        Color.Black.copy(alpha = 0.5f)
    )
)

val blackShadesLinearMini = Brush.linearGradient(
    listOf(
        Color.Black.copy(alpha = 1f),
        Color.Black.copy(alpha = 0.8f),
        Color.Black.copy(alpha = 0.5f),
        Color.Black.copy(alpha = 0.3f),
        Color.Transparent
    )
)

val darkLinear = listOf(
    Color(0xFF282C27),
    Color(0xFF181A18),
    Color(0xFF1A1C1A),
    Color(0xFF1D1F1E),
    Color(0xFF18231B)
)