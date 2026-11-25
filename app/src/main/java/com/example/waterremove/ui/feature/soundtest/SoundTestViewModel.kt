package com.example.waterremove.ui.feature.soundtest

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterremove.R
import com.example.waterremove.core.audio.TonePlayer
import com.example.waterremove.core.audio.TonePrograms
import com.example.waterremove.core.data.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FrequencyBand(@param:StringRes val titleRes: Int, val fromHz: Double, val toHz: Double) {
    LOW(R.string.band_low, 20.0, 300.0),
    MID(R.string.band_mid, 300.0, 5_000.0),
    HIGH(R.string.band_high, 5_000.0, 20_000.0),
}

data class SoundTestUiState(
    val tab: Int = 0,
    val playingBand: FrequencyBand? = null,
    val stereoPlaying: Boolean = false,
    val leftOn: Boolean = false,
    val rightOn: Boolean = false,
    val showGuideHint: Boolean = false,
)

const val STEREO_HZ = 1000.0
private const val GUIDE_KEY = "sound"

@HiltViewModel
class SoundTestViewModel @Inject constructor(
    private val player: TonePlayer,
    private val prefs: AppPrefs,
) : ViewModel() {

    private val _state = MutableStateFlow(SoundTestUiState())
    val state: StateFlow<SoundTestUiState> = _state.asStateFlow()

    val elapsedSec: StateFlow<Double> = player.elapsedSec
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    private var token: Int? = null

    init {
        viewModelScope.launch {
            if (!prefs.isTutorialSeen(GUIDE_KEY)) _state.update { it.copy(showGuideHint = true) }
        }
    }

    fun dismissGuideHint() {
        _state.update { it.copy(showGuideHint = false) }
        viewModelScope.launch { prefs.setTutorialSeen(GUIDE_KEY) }
    }

    fun selectTab(tab: Int) {
        stopAll()
        _state.update { it.copy(tab = tab) }
    }

    fun toggleBand(band: FrequencyBand) {
        if (_state.value.playingBand == band) {
            stopAll()
            return
        }
        stopAll()
        val myToken = player.playProgram(TonePrograms.soundBand(band.fromHz, band.toHz)) {
            _state.update { s -> if (s.playingBand == band) s.copy(playingBand = null) else s }
        }
        token = myToken
        _state.update { it.copy(playingBand = band) }
    }

    fun toggleStereo() {
        if (_state.value.stereoPlaying) {
            stopAll()
        } else {
            stopAll()
            token = player.playContinuous(STEREO_HZ)
            player.setChannels(left = true, right = true)
            _state.update { it.copy(stereoPlaying = true, leftOn = true, rightOn = true) }
        }
    }

    fun toggleSpeaker(left: Boolean) {
        if (!_state.value.stereoPlaying) return
        _state.update { if (left) it.copy(leftOn = !it.leftOn) else it.copy(rightOn = !it.rightOn) }
        player.setChannels(_state.value.leftOn, _state.value.rightOn)
    }

    fun stopAll() {
        token?.let { player.stop(it) }
        token = null
        _state.update { it.copy(playingBand = null, stereoPlaying = false, leftOn = false, rightOn = false) }
    }

    override fun onCleared() {
        token?.let { player.stop(it) }
    }
}
