package com.example.asnova.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = darkGreenAsnova,
    secondary = GreenGrey80,
    tertiary = lightGreenAsnova
)

private val LightColorScheme = lightColorScheme(
    primary = darkGreenAsnova,
    secondary = GreenGrey40,
    tertiary = lightGreenAsnova
)

@Composable
fun AsnovaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
    )
}
