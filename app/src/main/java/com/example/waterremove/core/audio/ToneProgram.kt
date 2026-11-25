package com.example.waterremove.core.audio

import kotlin.math.ln
import kotlin.math.exp

/**
 * Một đoạn âm thanh trong chương trình làm sạch.
 * [frame] trả về tần số (Hz) và độ mở (gate 0/1) tại thời điểm [t] (giây, tính từ đầu đoạn).
 */
sealed interface ToneSegment {
    val durationSec: Double
    fun frame(t: Double, out: ToneFrame)
}

class ToneFrame {
    var hz1 = 0.0
    var hz2 = 0.0
    var gate = 1.0
}

/** Phát cố định một tần số. */
data class Constant(val hz: Double, override val durationSec: Double) : ToneSegment {
    override fun frame(t: Double, out: ToneFrame) {
        out.hz1 = hz; out.hz2 = 0.0; out.gate = 1.0
    }
}

/**
 * Quét tần số từ [fromHz] tới [toHz].
 * [logarithmic] = true khi dải rộng (vd 400 → 20000 Hz) để tai nghe đều hơn.
 */
data class Sweep(
    val fromHz: Double,
    val toHz: Double,
    override val durationSec: Double,
    val logarithmic: Boolean = false,
) : ToneSegment {
    override fun frame(t: Double, out: ToneFrame) {
        val p = (t / durationSec).coerceIn(0.0, 1.0)
        out.hz1 = if (logarithmic) exp(ln(fromHz) + (ln(toHz) - ln(fromHz)) * p)
        else fromHz + (toHz - fromHz) * p
        out.hz2 = 0.0
        out.gate = 1.0
    }
}

/** Phát ngắt quãng: bật [onSec] giây, tắt [offSec] giây. */
data class Pulse(
    val hz: Double,
    override val durationSec: Double,
    val onSec: Double,
    val offSec: Double,
) : ToneSegment {
    override fun frame(t: Double, out: ToneFrame) {
        out.hz1 = hz; out.hz2 = 0.0
        out.gate = if (t % (onSec + offSec) < onSec) 1.0 else 0.0
    }
}

/** Phát chồng 2 tần số cùng lúc. */
data class Dual(val hz1: Double, val hz2: Double, override val durationSec: Double) : ToneSegment {
    override fun frame(t: Double, out: ToneFrame) {
        out.hz1 = hz1; out.hz2 = hz2; out.gate = 1.0
    }
}

/** Các chương trình âm thanh theo tài liệu MVP. */
object TonePrograms {

    /** Slightly Wet — 15 giây: 10s 165 Hz, 5s 190 Hz. */
    val waterSlightly: List<ToneSegment> = listOf(
        Constant(165.0, 10.0),
        Constant(190.0, 5.0),
    )

    /** Heavily Wet — 36 giây (2 chu kỳ): 8s 200 Hz, 5s quét 200–600 Hz, 5s 350 Hz pulse 0.3s/0.1s. */
    val waterHeavily: List<ToneSegment> = List(2) {
        listOf(
            Constant(200.0, 8.0),
            Sweep(200.0, 600.0, 5.0),
            Pulse(350.0, 5.0, onSec = 0.3, offSec = 0.1),
        )
    }.flatten()

    /** Strong Remove — 60 giây: 15s 150→300 Hz, 20s 600→1500 Hz, 15s 900+2000 Hz, 10s 300 Hz. */
    val waterStrong: List<ToneSegment> = listOf(
        Sweep(150.0, 300.0, 15.0),
        Sweep(600.0, 1500.0, 20.0),
        Dual(900.0, 2000.0, 15.0),
        Constant(300.0, 10.0),
    )

    /** Dust — quét 400 → 20000 Hz, lặp 5 vòng, mỗi vòng 3 giây (tổng 15 giây). */
    val dust: List<ToneSegment> = List(5) { Sweep(400.0, 20_000.0, 3.0, logarithmic = true) }

    /** Sound test — quét một dải tần trong 25 giây (thanh tiến trình 00:00 → 00:25). */
    const val SOUND_TEST_SECONDS = 25.0
    fun soundBand(fromHz: Double, toHz: Double): List<ToneSegment> =
        listOf(Sweep(fromHz, toHz, SOUND_TEST_SECONDS, logarithmic = true))

    fun List<ToneSegment>.totalSeconds(): Double = sumOf { it.durationSec }
}
