package com.example.waterremove.ui.feature.cleaner

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.waterremove.R
import com.example.waterremove.ui.components.AppIcons
import com.example.waterremove.ui.components.PrimaryButton
import com.example.waterremove.ui.theme.Blue
import com.example.waterremove.ui.theme.BlueLight
import com.example.waterremove.ui.theme.Orange
import com.example.waterremove.ui.theme.WaterRemoveTheme


@Composable
fun ResultScreen(
    percent: Int,
    onHome: () -> Unit,
    onCleanAgain: () -> Unit,
    onTestSpeaker: () -> Unit,
) {
    BackHandler(onBack = onHome)
    val completed = percent >= 100

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth()) {
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onHome) {
                Icon(Icons.Outlined.Home, stringResource(R.string.home))
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            stringResource(if (completed) R.string.result_complete_title else R.string.result_incomplete_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            stringResource(if (completed) R.string.result_complete_desc else R.string.result_incomplete_desc),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(36.dp))

        if (completed) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Blue),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(72.dp))
            }
        } else {
            // TODO: thay bằng ảnh "điện thoại + cảnh báo" export từ Figma
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(BlueLight),
                contentAlignment = Alignment.Center,
            ) {
                Icon(AppIcons.Speaker, null, tint = Blue, modifier = Modifier.size(56.dp))
                Icon(
                    Icons.Filled.Warning, null, tint = Orange,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp)
                        .size(32.dp),
                )
            }
        }

        Spacer(Modifier.height(28.dp))
        Text(
            stringResource(R.string.percent_completed, percent.coerceIn(0, 100)),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(20.dp))
        PrimaryButton(stringResource(R.string.clean_again), onCleanAgain, icon = AppIcons.Sparkle)
        TextButton(onClick = onTestSpeaker) {
            Text(stringResource(R.string.test_speaker), color = Blue)
        }
    }
}

@Preview(showSystemUi = true, name = "100%")
@Composable
private fun ResultCompletePreview() {
    WaterRemoveTheme { ResultScreen(100, {}, {}, {}) }
}

@Preview(showSystemUi = true, name = "80%")
@Composable
private fun ResultIncompletePreview() {
    WaterRemoveTheme { ResultScreen(80, {}, {}, {}) }
}
