package com.example.waterremove.core.audio

import android.content.Context
import android.media.AudioManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class VolumeBooster @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val audioManager = context.getSystemService(AudioManager::class.java)
    private var savedVolume: Int? = null

    @Synchronized
    fun boost() {
        if (savedVolume != null) return
        runCatching {
            val stream = AudioManager.STREAM_MUSIC
            savedVolume = audioManager.getStreamVolume(stream)
            audioManager.setStreamVolume(stream, audioManager.getStreamMaxVolume(stream), 0)
        }
    }

    @Synchronized
    fun restore() {
        val volume = savedVolume ?: return
        savedVolume = null
        runCatching { audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0) }
    }
}
