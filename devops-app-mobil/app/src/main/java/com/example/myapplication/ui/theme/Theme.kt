package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Un solo esquema oscuro: el cristal solo se ve bien sobre fondo oscuro
private val GlassColorScheme = darkColorScheme(
    primary = Blue600,
    onPrimary = Color.White,
    secondary = Red600,
    background = Navy,
    onBackground = TextPrimary,
    surface = Navy,
    onSurface = TextPrimary,
    onSurfaceVariant = TextMuted,
    outline = Color(0xFF8499BF),
    error = Red300
)

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GlassColorScheme,
        typography = Typography(),
        content = content
    )
}
