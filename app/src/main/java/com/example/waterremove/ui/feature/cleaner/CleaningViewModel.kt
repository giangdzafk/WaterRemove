package com.example.waterremove.ui.feature.cleaner

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterremove.core.audio.TonePlayer
import com.example.waterremove.core.audio.TonePrograms.totalSeconds
import com.example.waterremove.core.audio.VolumeBooster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class CleaningViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val player: TonePlayer,
    private val volume: VolumeBooster,
) : ViewModel() {

    val feature = CleanerFeature.fromKey(savedStateHandle.get<String>("feature"))
    val level: Int = savedStateHandle.get<Int>("level") ?: 0

    private val program = feature.program(level)
    private val totalSec = program.totalSeconds()

    val progress: StateFlow<Float> = player.elapsedSec
        .map { (it / totalSec).toFloat().coerceIn(0f, 1f) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0f)

    private val _result = MutableStateFlow<Int?>(null)
    val result: StateFlow<Int?> = _result.asStateFlow()

    private val token: Int = run {
        volume.boost()
        player.playProgram(program) {
            volume.restore()
            _result.value = 100
        }
    }

    fun stop() {
        if (_result.value != null) return
        val percent = (progress.value * 100).toInt().coerceAtMost(99)
        player.stop(token)
        volume.restore()
        _result.value = percent
    }

    override fun onCleared() {
        player.stop(token)
        volume.restore()
    }
}
