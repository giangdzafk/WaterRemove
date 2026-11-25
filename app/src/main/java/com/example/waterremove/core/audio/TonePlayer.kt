package com.example.waterremove.core.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin


@Singleton
class TonePlayer @Inject constructor() {

    private val _elapsedSec = MutableStateFlow(0.0)
    val elapsedSec: StateFlow<Double> = _elapsedSec.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    @Volatile private var targetHz = 100.0
    @Volatile private var leftOn = true
    @Volatile private var rightOn = true

    private var worker: Thread? = null
    @Volatile private var running = false
    private var currentToken = 0


    @Synchronized
    fun playProgram(segments: List<ToneSegment>, onComplete: () -> Unit = {}): Int =
        start(segments, onComplete)

    @Synchronized
    fun playContinuous(hz: Double): Int {
        targetHz = hz
        return start(null) {}
    }

    /** Đổi tần số khi đang phát liên tục (kéo vòng Manual). */
    fun setFrequency(hz: Double) {
        targetHz = hz
    }

    /** Bật/tắt loa trái/phải (Stereo test). */
    fun setChannels(left: Boolean, right: Boolean) {
        leftOn = left
        rightOn = right
    }

    /** Dừng phát. Nếu truyền [token], chỉ dừng khi đó đúng là lần phát hiện tại. */
    @Synchronized
    fun stop(token: Int? = null) {
        if (token != null && token != currentToken) return
        stopWorker()
    }

    private fun start(program: List<ToneSegment>?, onComplete: () -> Unit): Int {
        stopWorker()
        currentToken++
        val token = currentToken
        _elapsedSec.value = 0.0
        running = true
        _isPlaying.value = true
        worker = Thread({ render(program, token, onComplete) }, "tone-player").apply { start() }
        return token
    }

    private fun stopWorker() {
        running = false
        worker?.join(500)
        worker = null
        _isPlaying.value = false
        leftOn = true
        rightOn = true
    }

    private fun render(program: List<ToneSegment>?, token: Int, onComplete: () -> Unit) {
        val track = buildTrack()
        track.play()

        val buffer = FloatArray(FRAMES_PER_CHUNK * 2) // stereo: [L, R, L, R, ...]
        val frame = ToneFrame()
        val total = program?.sumOf { it.durationSec } ?: Double.MAX_VALUE

        var sampleIndex = 0L
        var phase1 = 0.0
        var phase2 = 0.0
        var currentHz = targetHz      // dùng cho chế độ liên tục, trượt mượt tới targetHz
        var gate = 0.0                // làm mượt bật/tắt để không bị tiếng "lụp bụp"
        var leftGain = 0.0
        var rightGain = 0.0
        var finished = false

        // Tìm đoạn đang phát theo thời gian, đi tuần tự cho nhanh
        var segIndex = 0
        var segStart = 0.0

        while (running && !finished) {
            for (i in 0 until FRAMES_PER_CHUNK) {
                val t = sampleIndex.toDouble() / SAMPLE_RATE

                if (program != null) {
                    if (t >= total) { finished = true }
                    while (!finished && t - segStart >= program[segIndex].durationSec) {
                        segStart += program[segIndex].durationSec
                        segIndex++
                        if (segIndex >= program.size) finished = true
                    }
                    if (finished) {
                        frame.gate = 0.0
                    } else {
                        program[segIndex].frame(t - segStart, frame)
                    }
                } else {
                    currentHz *= (targetHz / currentHz).pow(GLIDE)
                    frame.hz1 = currentHz; frame.hz2 = 0.0; frame.gate = 1.0
                }

                gate += (frame.gate - gate) * SMOOTH
                leftGain += ((if (leftOn) 1.0 else 0.0) - leftGain) * SMOOTH
                rightGain += ((if (rightOn) 1.0 else 0.0) - rightGain) * SMOOTH

                phase1 = (phase1 + 2 * PI * frame.hz1 / SAMPLE_RATE) % (2 * PI)
                var s = sin(phase1)
                if (frame.hz2 > 0) {
                    phase2 = (phase2 + 2 * PI * frame.hz2 / SAMPLE_RATE) % (2 * PI)
                    s = (s + sin(phase2)) * 0.5
                }
                val v = s * AMPLITUDE * gate
                buffer[i * 2] = (v * leftGain).toFloat()
                buffer[i * 2 + 1] = (v * rightGain).toFloat()
                sampleIndex++
            }
            track.write(buffer, 0, buffer.size, AudioTrack.WRITE_BLOCKING)
            _elapsedSec.value = (sampleIndex.toDouble() / SAMPLE_RATE).coerceAtMost(total)
        }

        track.pause()
        track.flush()
        track.release()

        if (finished) {
            synchronized(this) {
                if (token == currentToken) {
                    running = false
                    _isPlaying.value = false
                }
            }
            onComplete()
        }
    }

    private fun buildTrack(): AudioTrack {
        val minBytes = AudioTrack.getMinBufferSize(
            SAMPLE_RATE, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_FLOAT,
        )
        return AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(SAMPLE_RATE)
                    .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                    .build(),
            )
            .setBufferSizeInBytes(max(minBytes, FRAMES_PER_CHUNK * 2 * 4 * 2))
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
    }

    private companion object {
        const val SAMPLE_RATE = 44_100
        const val FRAMES_PER_CHUNK = 1024   // ~23 ms mỗi lần ghi
        const val AMPLITUDE = 0.9
        const val SMOOTH = 0.002            // hệ số làm mượt gate/kênh
        const val GLIDE = 0.0008            // tốc độ trượt tần số ở chế độ Manual
    }
}
