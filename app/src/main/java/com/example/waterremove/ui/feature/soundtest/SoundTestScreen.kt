package com.example.waterremove.ui.feature.soundtest

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.waterremove.R
import com.example.waterremove.core.audio.TonePrograms
import com.example.waterremove.ui.components.AppIcons
import com.example.waterremove.ui.components.AppTopBar
import com.example.waterremove.ui.components.PrimaryButton
import com.example.waterremove.ui.components.SecondaryButton
import com.example.waterremove.ui.components.SegmentedTabs
import com.example.waterremove.ui.feature.cleaner.GuideHintOverlay
import com.example.waterremove.ui.theme.Blue
import com.example.waterremove.ui.theme.BlueLight
import com.example.waterremove.ui.theme.Gray200
import com.example.waterremove.ui.theme.Gray400
import com.example.waterremove.ui.theme.WaterRemoveTheme

@Composable
fun SoundTestScreen(
    onBack: () -> Unit,
    onOpenGuide: () -> Unit,
    vm: SoundTestViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val elapsed by vm.elapsedSec.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) { vm.stopAll() }

    SoundTestContent(
        state = state,
        elapsedSec = elapsed,
        onBack = { vm.stopAll(); onBack() },
        onGuide = { vm.stopAll(); vm.dismissGuideHint(); onOpenGuide() },
        onDismissHint = vm::dismissGuideHint,
        onTab = vm::selectTab,
        onBandClick = vm::toggleBand,
        onStereoClick = vm::toggleStereo,
        onSpeakerClick = vm::toggleSpeaker,
    )
}

@Composable
fun SoundTestContent(
    state: SoundTestUiState,
    elapsedSec: Double,
    onBack: () -> Unit,
    onGuide: () -> Unit,
    onDismissHint: () -> Unit,
    onTab: (Int) -> Unit,
    onBandClick: (FrequencyBand) -> Unit,
    onStereoClick: () -> Unit,
    onSpeakerClick: (left: Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.fillMaxSize()) {
            AppTopBar(title = stringResource(R.string.sound_test), onBack = onBack) {
                IconButton(onClick = onGuide) { Icon(AppIcons.Guide, stringResource(R.string.action_guide)) }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            ) {
                SegmentedTabs(
                    options = listOf(stringResource(R.string.tab_frequency), stringResource(R.string.tab_stereo)),
                    selectedIndex = state.tab,
                    onSelect = onTab,
                )
                if (state.tab == 0) FrequencyTab(state, elapsedSec, onBandClick)
                else StereoTab(state, onStereoClick, onSpeakerClick)
            }
        }
        if (state.showGuideHint) GuideHintOverlay(onGuideClick = onGuide, onDismiss = onDismissHint)
    }
}

@Composable
private fun Hint(title: Int, desc: Int) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))
        Text(stringResource(title), color = Blue, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            stringResource(desc),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun FrequencyTab(state: SoundTestUiState, elapsedSec: Double, onBandClick: (FrequencyBand) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Hint(R.string.freq_hint_title, R.string.freq_hint_desc)
        FrequencyBand.entries.forEach { band ->
            val playing = state.playingBand == band
            BandCard(
                band = band,
                playing = playing,
                elapsedSec = if (playing) elapsedSec else 0.0,
                onClick = { onBandClick(band) },
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}

private fun formatTime(sec: Double): String {
    val s = sec.toInt()
    return "%02d:%02d".format(s / 60, s % 60)
}

@Composable
private fun BandCard(band: FrequencyBand, playing: Boolean, elapsedSec: Double, onClick: () -> Unit) {
    val total = TonePrograms.SOUND_TEST_SECONDS
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(stringResource(band.titleRes), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(formatTime(elapsedSec), fontSize = 11.sp)
                LinearProgressIndicator(
                    progress = { (elapsedSec / total).toFloat().coerceIn(0f, 1f) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(5.dp)
                        .clip(CircleShape),
                    color = Blue,
                    trackColor = BlueLight,
                    drawStopIndicator = {},
                )
                Text(formatTime(total), fontSize = 11.sp)
            }
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(34.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (playing) Gray200 else Blue,
                    contentColor = if (playing) Blue else Color.White,
                ),
            ) {
                Icon(if (playing) AppIcons.Pause else Icons.Filled.PlayArrow, null, Modifier.size(16.dp))
                Spacer(Modifier.size(4.dp))
                Text(stringResource(if (playing) R.string.action_stop else R.string.action_play), fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun StereoTab(state: SoundTestUiState, onStereoClick: () -> Unit, onSpeakerClick: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Hint(R.string.stereo_hint_title, R.string.stereo_hint_desc)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            SpeakerView(
                label = stringResource(R.string.speaker_left, stringResource(if (state.leftOn) R.string.on else R.string.off)),
                on = state.leftOn,
                onClick = { onSpeakerClick(true) },
            )
            SpeakerView(
                label = stringResource(R.string.speaker_right, stringResource(if (state.rightOn) R.string.on else R.string.off)),
                on = state.rightOn,
                onClick = { onSpeakerClick(false) },
            )
        }
        Spacer(Modifier.height(28.dp))
        if (state.stereoPlaying) {
            SecondaryButton(stringResource(R.string.action_stop), onStereoClick, icon = AppIcons.Pause)
        } else {
            PrimaryButton(stringResource(R.string.start_testing), onStereoClick, icon = Icons.Filled.PlayArrow)
        }
        Spacer(Modifier.height(16.dp))
    }
}

/** Hình loa tròn: xanh có sóng khi On, xám khi Off. Bấm để bật/tắt (khi đang test). */
@Composable
private fun SpeakerView(label: String, on: Boolean, onClick: () -> Unit) {
    val main = if (on) Blue else Gray400
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(Modifier.fillMaxSize()) {
                if (on) {
                    drawCircle(main.copy(alpha = 0.15f), radius = size.minDimension / 2)
                    drawCircle(main.copy(alpha = 0.4f), radius = size.minDimension / 2.25f, style = Stroke(2.dp.toPx()))
                }
                drawCircle(main.copy(alpha = if (on) 0.85f else 0.55f), radius = size.minDimension / 2.6f)
                drawCircle(Color.White.copy(alpha = 0.5f), radius = size.minDimension / 4.5f)
                drawCircle(main, radius = size.minDimension / 7f)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview(showSystemUi = true, name = "Frequency")
@Composable
private fun FrequencyPreview() {
    WaterRemoveTheme {
        SoundTestContent(SoundTestUiState(playingBand = FrequencyBand.LOW), 12.0, {}, {}, {}, {}, {}, {}, {})
    }
}

@Preview(showSystemUi = true, name = "Stereo")
@Composable
private fun StereoPreview() {
    WaterRemoveTheme {
        SoundTestContent(
            SoundTestUiState(tab = 1, stereoPlaying = true, leftOn = true, rightOn = false),
            0.0, {}, {}, {}, {}, {}, {}, {},
        )
    }
}
