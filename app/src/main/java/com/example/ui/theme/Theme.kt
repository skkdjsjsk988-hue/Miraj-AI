package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = MirajSurfaceElevated,
    onPrimaryContainer = ElectricBlueGlow,
    secondary = PurpleAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF231C38),
    onSecondaryContainer = PurpleGlow,
    tertiary = CyanAccent,
    onTertiary = Color.Black,
    background = MirajDarkBg,
    onBackground = TextPrimary,
    surface = MirajSurface,
    onSurface = TextPrimary,
    surfaceVariant = MirajSurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = MirajBorder,
    outlineVariant = MirajBorderHighlight,
    error = RoseError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E40AF),
    secondary = PurpleAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE9FE),
    onSecondaryContainer = Color(0xFF5B21B6),
    tertiary = TealAccent,
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    error = RoseError,
    onError = Color.White
)

enum class ThemeMode {
    DARK, LIGHT, SYSTEM
}

@Composable
fun MirajAiTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Retain alias for template compatibility if needed
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MirajAiTheme(
        themeMode = if (darkTheme) ThemeMode.DARK else ThemeMode.LIGHT,
        content = content
    )
}
