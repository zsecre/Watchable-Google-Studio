package com.watchable.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyanBrand = Color(0xFF00E5FF)
private val DarkGrey = Color(0xFF121212)
private val DarkerGrey = Color(0xFF0A0A0A)

private val DarkColorScheme = darkColorScheme(
    primary = CyanBrand,
    secondary = CyanBrand,
    tertiary = CyanBrand,
    background = Color.Black,
    surface = DarkGrey,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CyanBrand,
    secondary = CyanBrand,
    tertiary = CyanBrand,
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun WatchableTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
