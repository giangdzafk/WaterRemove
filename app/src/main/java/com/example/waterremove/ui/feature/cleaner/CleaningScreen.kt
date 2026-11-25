package com.example.waterremove.ui.feature.cleaner

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.waterremove.R
import com.example.waterremove.ui.components.AppIcons
import com.example.waterremove.ui.components.SecondaryButton
import com.example.waterremove.ui.components.VolumeWatcher
import com.example.waterremove.ui.theme.Blue
import com.example.waterremove.ui.theme.BlueLight
import com.example.waterremove.ui.theme.Gray200
import com.example.waterremove.ui.theme.WaterRemoveTheme

@Composable
fun CleaningScreen(
    onResult: (feature: CleanerFeature, level: Int, percent: Int) -> Unit,
    vm: CleaningViewModel = hiltViewModel(),
) {
    val progress by vm.progress.collectAsStateWithLifecycle()
    val result by vm.result.collectAsStateWithLifecycle()
    var showStopDialog by rememberSaveable { mutableStateOf(false) }
    val currentOnResult by rememberUpdatedState(onResult)

    LaunchedEffect(result) {
        result?.let { currentOnResult(vm.feature, vm.level, it) }
    }
    BackHandler { showStopDialog = true }

    CleaningContent(
        icon = vm.feature.icon,
        progress = progress,
        onStopClick = { showStopDialog = true },
    )

    if (showStopDialog) {
        StopCleaningDialog(
            onStop = { showStopDialog = false; vm.stop() },
            onContinue = { showStopDialog = false },
        )
    }
}

@Composable
fun CleaningContent(icon: androidx.compose.ui.graphics.vector.ImageVector, progress: Float, onStopClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VolumeWatcher(Modifier.padding(top = 8.dp))
        Spacer(Modifier.weight(0.6f))
        Text(stringResource(R.string.clean_speaker), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Text(
            stringResource(R.string.do_not_exit),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(40.dp))
        CleaningRing(progress = progress, icon = icon)
        Spacer(Modifier.height(32.dp))
        Text(
            stringResource(R.string.percent_value, (progress * 100).toInt()),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(6.dp))
        Text(stringResource(R.string.cleaning), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.weight(1f))
        SecondaryButton(stringResource(R.string.stop_cleaning), onStopClick)
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun StopCleaningDialog(onStop: () -> Unit, onContinue: () -> Unit) {
    Dialog(onDismissRequest = onContinue) {
        Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(BlueLight, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(AppIcons.Sparkle, null, tint = Blue, modifier = Modifier.size(32.dp))
                }
                Spacer(Modifier.height(12.dp))
                Text(stringResource(R.string.stop_dialog_title), fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text(
                    stringResource(R.string.stop_dialog_desc),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onStop,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Gray200, contentColor = Blue),
                    ) { Text(stringResource(R.string.action_stop)) }
                    Button(
                        onClick = onContinue,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                    ) { Text(stringResource(R.string.action_continue)) }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CleaningPreview() {
    WaterRemoveTheme { CleaningContent(AppIcons.Sparkle, 0.5f) {} }
}

@Preview(showBackground = true)
@Composable
private fun StopDialogPreview() {
    WaterRemoveTheme { StopCleaningDialog({}, {}) }
}
