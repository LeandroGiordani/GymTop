package com.example.gymtop

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GymTopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

