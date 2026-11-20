package com.example.waterremove.ui.feature.splash

import androidx.lifecycle.ViewModel
import com.example.waterremove.core.data.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prefs: AppPrefs,
) : ViewModel() {
    suspend fun isFirstLaunch(): Boolean {
        prefs.increaseOpenCount()
        return !prefs.isOnboardingDone()
    }
}