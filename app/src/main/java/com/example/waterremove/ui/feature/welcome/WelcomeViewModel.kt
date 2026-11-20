package com.example.waterremove.ui.feature.welcome


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterremove.core.data.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val prefs: AppPrefs,
) : ViewModel() {

    fun finishOnboarding(onDone: () -> Unit) {
        viewModelScope.launch {
            prefs.setOnboardingDone() 
            onDone()
        }
    }
}