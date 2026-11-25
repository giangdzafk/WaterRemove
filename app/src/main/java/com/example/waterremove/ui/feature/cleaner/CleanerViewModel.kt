package com.example.waterremove.ui.feature.cleaner

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterremove.core.audio.TonePlayer
import com.example.waterremove.core.data.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class CleanerMode { AUTO, MANUAL }

data class CleanerUiState(
    val mode: CleanerMode = CleanerMode.AUTO,
    val level: WetnessLevel = WetnessLevel.SLIGHTLY,
    val manualHz: Double = DEFAULT_MANUAL_HZ,
    val manualPlaying: Boolean = false,
    val showGuideHint: Boolean = false,
)

const val DEFAULT_MANUAL_HZ = 100.0

@HiltViewModel
class CleanerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val player: TonePlayer,
    private val prefs: AppPrefs,
) : ViewModel() {

    val feature = CleanerFeature.fromKey(savedStateHandle.get<String>("feature"))

    private val _state = MutableStateFlow(CleanerUiState())
    val state: StateFlow<CleanerUiState> = _state.asStateFlow()

    private var manualToken: Int? = null

    init {
        // Lần đầu vào tính năng: hiện gợi ý bấm vào icon guide
        viewModelScope.launch {
            if (!prefs.isTutorialSeen(feature.key)) _state.update { it.copy(showGuideHint = true) }
        }
    }

    fun dismissGuideHint() {
        _state.update { it.copy(showGuideHint = false) }
        viewModelScope.launch { prefs.setTutorialSeen(feature.key) }
    }

    fun setMode(mode: CleanerMode) {
        stopManual()
        _state.update { it.copy(mode = mode) }
    }

    fun selectLevel(level: WetnessLevel) = _state.update { it.copy(level = level) }

    fun setManualHz(hz: Double) {
        _state.update { it.copy(manualHz = hz) }
        if (_state.value.manualPlaying) player.setFrequency(hz)
    }

    fun toggleManual() {
        if (_state.value.manualPlaying) {
            stopManual()
        } else {
            manualToken = player.playContinuous(_state.value.manualHz)
            _state.update { it.copy(manualPlaying = true) }
        }
    }

    fun stopManual() {
        manualToken?.let { player.stop(it) }
        manualToken = null
        _state.update { it.copy(manualPlaying = false) }
    }

    override fun onCleared() {
        stopManual()
    }
}
