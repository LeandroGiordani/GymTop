package com.example.gymtop.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// GymTop dark scheme — maps brand colours to Material3 roles so that any
// Composable using MaterialTheme.colorScheme automatically gets the right tones.
private val GymTopDarkColorScheme = darkColorScheme(
    primary      = GymTopNeonGreen,   // buttons, active indicators
    onPrimary    = GymTopOnPrimary,   // text/icons placed on top of primary
    background   = GymTopBackground, // window background
    onBackground = GymTopNeonGreen,   // default text on background
    surface      = GymTopSurface,    // cards, dialogs, bottom sheets
    onSurface    = GymTopNeonGreen,   // text/icons on surface
    secondary    = PurpleGrey80,
    tertiary     = Pink80
)

@Composable
fun GymTopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set dynamicColor = false to always enforce the GymTop brand palette.
    // When true, Android 12+ devices may override colours with the system wallpaper.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Always use the GymTop dark scheme; the app has a dark-only design.
        else -> GymTopDarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
