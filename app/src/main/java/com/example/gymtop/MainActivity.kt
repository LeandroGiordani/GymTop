package com.example.gymtop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.gymtop.presentation.navigation.NavGraph
import com.example.gymtop.ui.theme.GymTopTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // installSplashScreen() MUST be called before super.onCreate() and setContent.
        // It reads Theme.GymTop.Splash, shows the system splash window immediately,
        // and then switches to postSplashScreenTheme (Theme.GymTop) when the app is ready.
        // This eliminates the blank/white window during cold start.
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            GymTopTheme {
                NavGraph()
            }
        }
    }
}
