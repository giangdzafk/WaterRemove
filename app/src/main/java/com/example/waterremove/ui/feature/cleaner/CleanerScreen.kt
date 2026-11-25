package com.example.waterremove.ui.feature.cleaner

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.waterremove.ui.components.AppIcons
import com.example.waterremove.ui.components.AppTopBar
import com.example.waterremove.ui.components.FrequencyDial
import com.example.waterremove.ui.components.PrimaryButton
import com.example.waterremove.ui.components.SecondaryButton
import com.example.waterremove.ui.components.SegmentedTabs
import com.example.waterremove.ui.components.VolumeWatcher
import com.example.waterremove.ui.theme.WaterRemoveTheme


@Composable
fun CleanerScreen(
    onBack: () -> Unit,
    onStartAuto: (feature: CleanerFeature, level: Int) -> Unit,
    onOpenGuide: (CleanerFeature) -> Unit,
    vm: CleanerViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) { vm.stopManual() }

    CleanerContent(
        feature = vm.feature,
        state = state,
        onBack = { vm.stopManual(); onBack() },
        onGuide = { vm.stopManual(); vm.dismissGuideHint(); onOpenGuide(vm.feature) },
        onDismissHint = vm::dismissGuideHint,
        onModeChange = vm::setMode,
        onLevelSelect = vm::selectLevel,
        onStart = { onStartAuto(vm.feature, state.level.ordinal) },
        onHzChange = vm::setManualHz,
        onToggleManual = vm::toggleManual,
    )
}

@Composable
fun CleanerContent(
    feature: CleanerFeature,
    state: CleanerUiState,
    onBack: () -> Unit,
    onGuide: () -> Unit,
    onDismissHint: () -> Unit,
    onModeChange: (CleanerMode) -> Unit,
    onLevelSelect: (WetnessLevel) -> Unit,
    onStart: () -> Unit,
    onHzChange: (Double) -> Unit,
    onToggleManual: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.fillMaxSize()) {
            AppTopBar(title = stringResource(feature.screenTitle), onBack = onBack) {
                IconButton(onClick = onGuide) {
                    Icon(AppIcons.Guide, stringResource(R.string.action_guide))
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            ) {
                SegmentedTabs(
                    options = listOf(stringResource(R.string.mode_auto), stringResource(R.string.mode_manual)),
                    selectedIndex = state.mode.ordinal,
                    onSelect = { onModeChange(CleanerMode.entries[it]) },
                )
                VolumeWatcher(Modifier.padding(top = 10.dp))
                when (state.mode) {
                    CleanerMode.AUTO -> AutoContent(feature, state.level, onLevelSelect, onStart)
                    CleanerMode.MANUAL -> ManualContent(state, onHzChange, onToggleManual)
                }
            }
        }

        if (state.showGuideHint) {
            GuideHintOverlay(onGuideClick = onGuide, onDismiss = onDismissHint)
        }
    }
}

@Composable
private fun AutoContent(
    feature: CleanerFeature,
    level: WetnessLevel,
    onLevelSelect: (WetnessLevel) -> Unit,
    onStart: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(20.dp))
        StartCircleButton(icon = AppIcons.Sparkle, onClick = onStart)
        Spacer(Modifier.height(16.dp))
        Text(stringResource(feature.modeTitle), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Text(
            stringResource(feature.modeDesc),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        if (feature == CleanerFeature.WATER) {
            Spacer(Modifier.height(24.dp))
            Text(
                stringResource(R.string.state_of_wetness),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WetnessLevel.entries.forEach { item ->
                    WetnessCard(
                        level = item,
                        selected = item == level,
                        onClick = { onLevelSelect(item) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ManualContent(
    state: CleanerUiState,
    onHzChange: (Double) -> Unit,
    onToggleManual: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        FrequencyDial(hz = state.manualHz, onHzChange = onHzChange)
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(R.string.drag_to_adjust),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.weight(1f))
        if (state.manualPlaying) {
            SecondaryButton(stringResource(R.string.stop_cleaning), onToggleManual, icon = AppIcons.Sparkle)
        } else {
            PrimaryButton(stringResource(R.string.start_cleaning), onToggleManual, icon = AppIcons.Sparkle)
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showSystemUi = true, name = "Water - Auto")
@Composable
private fun CleanerAutoPreview() {
    WaterRemoveTheme {
        CleanerContent(
            CleanerFeature.WATER, CleanerUiState(), {}, {}, {}, {}, {}, {}, {}, {},
        )
    }
}

@Preview(showSystemUi = true, name = "Water - Manual")
@Composable
private fun CleanerManualPreview() {
    WaterRemoveTheme {
        CleanerContent(
            CleanerFeature.WATER, CleanerUiState(mode = CleanerMode.MANUAL, manualHz = 7140.0),
            {}, {}, {}, {}, {}, {}, {}, {},
        )
    }
}

@Preview(showSystemUi = true, name = "Guide hint")
@Composable
private fun CleanerHintPreview() {
    WaterRemoveTheme {
        CleanerContent(
            CleanerFeature.DUST, CleanerUiState(showGuideHint = true),
            {}, {}, {}, {}, {}, {}, {}, {},
        )
    }
}
