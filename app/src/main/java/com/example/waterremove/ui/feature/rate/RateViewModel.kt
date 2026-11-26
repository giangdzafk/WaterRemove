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

    private val _rated = MutableStateFlow(false)
    val rated: StateFlow<Boolean> = _rated.asStateFlow()

    init {
        viewModelScope.launch { _rated.value = prefs.isRated() }
    }

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
        _rated.value = true
        viewModelScope.launch { prefs.setRated() }
    }
}
