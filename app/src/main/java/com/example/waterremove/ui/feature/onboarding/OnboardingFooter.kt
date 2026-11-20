package com.example.waterremove.ui.feature.onboarding


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.waterremove.R
import com.example.waterremove.ui.components.PageIndicator
import com.example.waterremove.ui.components.PrimaryButton


const val ONBOARDING_PAGES = 3

@Composable
internal fun OnboardingFooter(
    pageIndex: Int,
    looksEnabled: Boolean,
    onNextClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PageIndicator(
            pageCount = ONBOARDING_PAGES,
            currentPage = pageIndex,
            modifier = Modifier.weight(1f),
        )
        PrimaryButton(
            text = stringResource(R.string.action_next),
            onClick = onNextClick,
            looksEnabled = looksEnabled,
            modifier = Modifier.width(140.dp),
        )
    }
}