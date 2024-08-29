package com.jvrcoding.to_do_app.ui.theme

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
import com.jvrcoding.weatherapp.ui.theme.backgroundDark
import com.jvrcoding.weatherapp.ui.theme.backgroundLight
import com.jvrcoding.weatherapp.ui.theme.errorContainerDark
import com.jvrcoding.weatherapp.ui.theme.errorContainerLight
import com.jvrcoding.weatherapp.ui.theme.errorDark
import com.jvrcoding.weatherapp.ui.theme.errorLight
import com.jvrcoding.weatherapp.ui.theme.inverseOnSurfaceDark
import com.jvrcoding.weatherapp.ui.theme.inverseOnSurfaceLight
import com.jvrcoding.weatherapp.ui.theme.inversePrimaryDark
import com.jvrcoding.weatherapp.ui.theme.inversePrimaryLight
import com.jvrcoding.weatherapp.ui.theme.inverseSurfaceDark
import com.jvrcoding.weatherapp.ui.theme.inverseSurfaceLight
import com.jvrcoding.weatherapp.ui.theme.onBackgroundDark
import com.jvrcoding.weatherapp.ui.theme.onBackgroundLight
import com.jvrcoding.weatherapp.ui.theme.onErrorContainerDark
import com.jvrcoding.weatherapp.ui.theme.onErrorContainerLight
import com.jvrcoding.weatherapp.ui.theme.onErrorDark
import com.jvrcoding.weatherapp.ui.theme.onErrorLight
import com.jvrcoding.weatherapp.ui.theme.onPrimaryContainerDark
import com.jvrcoding.weatherapp.ui.theme.onPrimaryContainerLight
import com.jvrcoding.weatherapp.ui.theme.onPrimaryDark
import com.jvrcoding.weatherapp.ui.theme.onPrimaryLight
import com.jvrcoding.weatherapp.ui.theme.onSecondaryContainerDark
import com.jvrcoding.weatherapp.ui.theme.onSecondaryContainerLight
import com.jvrcoding.weatherapp.ui.theme.onSecondaryDark
import com.jvrcoding.weatherapp.ui.theme.onSecondaryLight
import com.jvrcoding.weatherapp.ui.theme.onSurfaceDark
import com.jvrcoding.weatherapp.ui.theme.onSurfaceLight
import com.jvrcoding.weatherapp.ui.theme.onSurfaceVariantDark
import com.jvrcoding.weatherapp.ui.theme.onSurfaceVariantLight
import com.jvrcoding.weatherapp.ui.theme.onTertiaryContainerDark
import com.jvrcoding.weatherapp.ui.theme.onTertiaryContainerLight
import com.jvrcoding.weatherapp.ui.theme.onTertiaryDark
import com.jvrcoding.weatherapp.ui.theme.onTertiaryLight
import com.jvrcoding.weatherapp.ui.theme.outlineDark
import com.jvrcoding.weatherapp.ui.theme.outlineLight
import com.jvrcoding.weatherapp.ui.theme.outlineVariantDark
import com.jvrcoding.weatherapp.ui.theme.outlineVariantLight
import com.jvrcoding.weatherapp.ui.theme.primaryContainerDark
import com.jvrcoding.weatherapp.ui.theme.primaryContainerLight
import com.jvrcoding.weatherapp.ui.theme.primaryDark
import com.jvrcoding.weatherapp.ui.theme.primaryLight
import com.jvrcoding.weatherapp.ui.theme.scrimDark
import com.jvrcoding.weatherapp.ui.theme.scrimLight
import com.jvrcoding.weatherapp.ui.theme.secondaryContainerDark
import com.jvrcoding.weatherapp.ui.theme.secondaryContainerLight
import com.jvrcoding.weatherapp.ui.theme.secondaryDark
import com.jvrcoding.weatherapp.ui.theme.secondaryLight
import com.jvrcoding.weatherapp.ui.theme.surfaceDark
import com.jvrcoding.weatherapp.ui.theme.surfaceLight
import com.jvrcoding.weatherapp.ui.theme.surfaceVariantDark
import com.jvrcoding.weatherapp.ui.theme.surfaceVariantLight
import com.jvrcoding.weatherapp.ui.theme.tertiaryContainerDark
import com.jvrcoding.weatherapp.ui.theme.tertiaryContainerLight
import com.jvrcoding.weatherapp.ui.theme.tertiaryDark
import com.jvrcoding.weatherapp.ui.theme.tertiaryLight

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight
)

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark
)

@Composable
fun WeatherAppTheme(
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}