package com.example.waterremove.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.waterremove.R
import com.example.waterremove.ui.theme.Freq0
import com.example.waterremove.ui.theme.Freq1
import com.example.waterremove.ui.theme.Freq2
import com.example.waterremove.ui.theme.Freq3
import com.example.waterremove.ui.theme.Freq4
import com.example.waterremove.ui.theme.Freq5
import com.example.waterremove.ui.theme.Gray200
import com.example.waterremove.ui.theme.Ink
import com.example.waterremove.ui.theme.WaterRemoveTheme
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.roundToInt
import kotlin.math.sin

const val MIN_HZ = 20.0
const val MAX_HZ = 20_000.0

private const val START_ANGLE = 135f   // góc bắt đầu của cung (độ, 0° = hướng 3 giờ)
private const val SWEEP_ANGLE = 270f   // cung dài 270°

fun hzToPosition(hz: Double): Float = (ln(hz / MIN_HZ) / ln(MAX_HZ / MIN_HZ)).toFloat().coerceIn(0f, 1f)
fun positionToHz(p: Float): Double = MIN_HZ * exp(ln(MAX_HZ / MIN_HZ) * p.coerceIn(0f, 1f))

fun colorForHz(hz: Double): Color = when {
    hz < 600 -> Freq0
    hz < 1200 -> Freq1
    hz < 2400 -> Freq2
    hz < 5000 -> Freq3
    hz < 10_000 -> Freq4
    else -> Freq5
}


@Composable
fun FrequencyDial(
    hz: Double,
    onHzChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnChange by rememberUpdatedState(onHzChange)
    val position = hzToPosition(hz)

    // Đổi toạ độ chạm → vị trí 0..1 trên cung
    fun positionAt(offset: Offset, size: Size): Float {
        val dx = offset.x - size.width / 2
        val dy = offset.y - size.height / 2
        val angle = Math.toDegrees(atan2(dy, dx).toDouble()).toFloat()
        var rel = (angle - START_ANGLE + 360f) % 360f
        if (rel > SWEEP_ANGLE) rel = if (rel > SWEEP_ANGLE + (360f - SWEEP_ANGLE) / 2) 0f else SWEEP_ANGLE
        return rel / SWEEP_ANGLE
    }

    Box(modifier = modifier.size(240.dp), contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val s = Size(size.width.toFloat(), size.height.toFloat())
                        currentOnChange(positionToHz(positionAt(offset, s)))
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val s = Size(size.width.toFloat(), size.height.toFloat())
                        currentOnChange(positionToHz(positionAt(change.position, s)))
                    }
                },
        ) {
            val strokeWidth = 14.dp.toPx()
            val knobRadius = 12.dp.toPx()
            val inset = knobRadius + 2.dp.toPx()
            val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
            val topLeft = Offset(inset, inset)

            // Cung nền xám
            drawArc(
                color = Gray200,
                startAngle = START_ANGLE,
                sweepAngle = SWEEP_ANGLE,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
            )


            val k = SWEEP_ANGLE / 360f
            rotate(START_ANGLE) {
                drawArc(
                    brush = Brush.sweepGradient(
                        0f to Freq0, 0.3f * k to Freq0, 0.54f * k to Freq1, 0.64f * k to Freq2,
                        0.75f * k to Freq3, 0.85f * k to Freq4, 0.95f * k to Freq5, k to Freq5,
                        (k + 1f) / 2f to Freq0, 1f to Freq0,
                    ),
                    startAngle = 0f,
                    sweepAngle = SWEEP_ANGLE * position,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(strokeWidth, cap = StrokeCap.Round),
                )
            }

            // Núm tròn
            val rad = Math.toRadians((START_ANGLE + SWEEP_ANGLE * position).toDouble())
            val r = arcSize.width / 2
            val knob = Offset(center.x + r * cos(rad).toFloat(), center.y + r * sin(rad).toFloat())
            drawCircle(Color.White, knobRadius, knob)
            drawCircle(colorForHz(hz), knobRadius, knob, style = Stroke(4.dp.toPx()))
        }

        Text(
            stringResource(R.string.hz_value, hz.roundToInt()),
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FrequencyDialPreview() {
    WaterRemoveTheme { FrequencyDial(hz = 7140.0, onHzChange = {}) }
}
