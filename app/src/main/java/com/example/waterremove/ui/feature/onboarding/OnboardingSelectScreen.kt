package com.example.waterremove.ui.feature.onboarding

import com.example.waterremove.R
import com.example.waterremove.ui.components.NativeAdSlot
import com.example.waterremove.ui.components.SelectableCard
import com.example.waterremove.ui.components.showToast
import com.example.waterremove.ui.theme.WaterRemoveTheme
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle


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
        onIssueClick = vm::toggle,
        onNextClick = {
            if (selected.isEmpty()) context.showToast(selectFirstMessage) // "You need to select first"
            else onNext()
        },
    )
}

@Composable
fun OnboardingSelectContent(
    selected: Set<SpeakerIssue>,
    onIssueClick: (SpeakerIssue) -> Unit,
    onNextClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                stringResource(R.string.onb1_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                stringResource(R.string.onb1_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))

            SpeakerIssue.entries.forEach { issue ->
                val isSelected = issue in selected
                SelectableCard(selected = isSelected, onClick = { onIssueClick(issue) }) {
                    Text(issue.emoji, fontSize = 24.sp)
                    Text(
                        stringResource(issue.titleRes),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                    )
                    Checkbox(checked = isSelected, onCheckedChange = null)
                }
            }
        }

        OnboardingFooter(
            pageIndex = 0,
            looksEnabled = selected.isNotEmpty(),
            onNextClick = onNextClick,
        )

        NativeAdSlot(placement = if (selected.isEmpty()) "native_onboarding_1" else "native_onb1_click")
    }
}

@Preview(showSystemUi = true, name = "Chưa chọn")
@Composable
private fun OnboardingSelectEmptyPreview() {
    WaterRemoveTheme() {
        OnboardingSelectContent(selected = emptySet(), onIssueClick = {}, onNextClick = {})
    }
}

@Preview(showSystemUi = true, name = "Đã chọn 2")
@Composable
private fun OnboardingSelectPickedPreview() {
    WaterRemoveTheme {
        OnboardingSelectContent(
            selected = setOf(SpeakerIssue.WATER, SpeakerIssue.TEST),
            onIssueClick = {},
            onNextClick = {},
        )
    }
}