package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = PrimaryTeal,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainerTeal,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = SecondaryGray,
    onSecondary = OnPrimary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    background = SurfaceLight,
    onBackground = OnSurface,
    surface = SurfaceLight,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainerHigh,
    onSurfaceVariant = OnSurfaceVariant,
    outline = OutlineColor,
    outlineVariant = OutlineVariantColor,
    error = ErrorColor,
    errorContainer = ErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryContainerTeal,
    onPrimary = OnPrimaryContainer,
    primaryContainer = PrimaryTeal,
    onPrimaryContainer = OnPrimary,
    secondary = SecondaryContainer,
    onSecondary = SecondaryGray,
    secondaryContainer = DarkSurfaceContainerLow,
    onSecondaryContainer = DarkOnSurfaceVariant,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceContainerLow,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = OutlineColor,
    outlineVariant = OutlineVariantColor,
    error = ErrorColor,
    errorContainer = ErrorContainer
)

@Composable
fun CgpaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent branding colors by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
