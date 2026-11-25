package com.example.waterremove.ui.feature.rate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterremove.core.data.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateViewModel @Inject constructor(
    private val prefs: AppPrefs,
) : ViewModel() {

    private val _visible = MutableStateFlow(false)
    val visible: StateFlow<Boolean> = _visible.asStateFlow()

    fun autoShowIfNeeded() {
        viewModelScope.launch {
            if (prefs.openCount() >= 2 && !prefs.isRated() && !prefs.isRateShown()) {
                prefs.setRateShown()
                _visible.value = true
            }
        }
    }

    fun show() {
        _visible.value = true
    }

    fun dismiss() {
        _visible.value = false
    }

    fun markRated() {
        viewModelScope.launch { prefs.setRated() }
    }
}
