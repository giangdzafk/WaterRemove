package com.example.waterremove.ui.feature.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.waterremove.R
import com.example.waterremove.ui.components.PrimaryButton
import com.example.waterremove.ui.components.showToast
import com.example.waterremove.ui.theme.WaterRemoveTheme

@Composable
fun OnboardingSelectScreen(
    onNext: () -> Unit,
    vm: OnboardingSelectViewModel = hiltViewModel(),
) {
    val selected by vm.selected.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val selectFirstMessage = stringResource(R.string.toast_select_first)

    OnboardingSelectContent(
        selected = selected,
        onTargetClick = vm::toggle,
        onNextClick = {
            if (selected.isEmpty()) context.showToast(selectFirstMessage) // "You need to select first"
            else onNext()
        },
    )
}

@Composable
fun OnboardingSelectContent(
    selected: Set<CleanTarget>,
    onTargetClick: (CleanTarget) -> Unit,
    onNextClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        CleanTarget.entries.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { target ->
                    TargetCard(
                        target = target,
                        selected = target in selected,
                        onClick = { onTargetClick(target) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(20.dp))
        Text(
            stringResource(R.string.onb1_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(R.string.onb1_subtitle),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.weight(1f))
        PrimaryButton(
            text = stringResource(R.string.action_next),
            onClick = onNextClick,
            looksEnabled = selected.isNotEmpty(),
        )
    }
}

@Composable
private fun TargetCard(
    target: CleanTarget,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            if (selected) 2.dp else 1.dp,
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        ),
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
            Box {
                Image(
                    painter = painterResource(target.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(252f / 150f) // đúng tỉ lệ ảnh gốc
                        .clip(RoundedCornerShape(8.dp)),
                )
                if (selected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                }
            }
            Text(
                stringResource(target.titleRes),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )
        }
    }
}

@Preview(showSystemUi = true, name = "Chưa chọn")
@Composable
private fun OnboardingSelectEmptyPreview() {
    WaterRemoveTheme {
        OnboardingSelectContent(selected = emptySet(), onTargetClick = {}, onNextClick = {})
    }
}

@Preview(showSystemUi = true, name = "Đã chọn 2")
@Composable
private fun OnboardingSelectPickedPreview() {
    WaterRemoveTheme {
        OnboardingSelectContent(
            selected = setOf(CleanTarget.WATER, CleanTarget.SAND),
            onTargetClick = {},
            onNextClick = {},
        )
    }
}
