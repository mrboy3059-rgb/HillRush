package com.maxfun.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MaxColors = darkColorScheme(
    primary = Color(0xFF8B5CF6),
    secondary = Color(0xFFEC4899),
    tertiary = Color(0xFF06B6D4),
    background = Color(0xFF080A12),
    surface = Color(0xFF11131D)
)

@Composable
fun MaxFunTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = MaxColors, content = content)
}
