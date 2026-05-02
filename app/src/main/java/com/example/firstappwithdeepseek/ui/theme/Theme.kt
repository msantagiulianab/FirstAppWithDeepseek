package com.example.firstappwithdeepseek.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NewsBlueDark,
    onPrimary = OnNewsBlueDark,
    primaryContainer = NewsBlueContainerDark,
    onPrimaryContainer = OnNewsBlueContainerDark,
    secondary = NewsTealDark,
    onSecondary = OnNewsTealDark,
    secondaryContainer = NewsTealContainerDark,
    onSecondaryContainer = OnNewsTealContainerDark,
    tertiary = NewsAmberDark,
    onTertiary = OnNewsAmberDark,
    tertiaryContainer = NewsAmberContainerDark,
    onTertiaryContainer = OnNewsAmberContainerDark,
    background = NewsBackgroundDark,
    onBackground = NewsOnBackgroundDark,
    surface = NewsSurfaceDark,
    onSurface = NewsOnSurfaceDark,
    surfaceVariant = NewsSurfaceVariantDark,
    onSurfaceVariant = NewsOnSurfaceVariantDark,
    outline = NewsOutlineDark,
    error = NewsErrorDark,
    onError = NewsOnErrorDark
)

private val LightColorScheme = lightColorScheme(
    primary = NewsBlue,
    onPrimary = OnNewsBlue,
    primaryContainer = NewsBlueContainer,
    onPrimaryContainer = OnNewsBlueContainer,
    secondary = NewsTeal,
    onSecondary = OnNewsTeal,
    secondaryContainer = NewsTealContainer,
    onSecondaryContainer = OnNewsTealContainer,
    tertiary = NewsAmber,
    onTertiary = OnNewsAmber,
    tertiaryContainer = NewsAmberContainer,
    onTertiaryContainer = OnNewsAmberContainer,
    background = NewsBackground,
    onBackground = NewsOnBackground,
    surface = NewsSurface,
    onSurface = NewsOnSurface,
    surfaceVariant = NewsSurfaceVariant,
    onSurfaceVariant = NewsOnSurfaceVariant,
    outline = NewsOutline,
    error = NewsError,
    onError = NewsOnError
)

@Composable
fun FirstAppWithDeepseekTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    // Set status bar color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
