package com.example.waterremove.ui.feature.onboarding

import com.example.waterremove.R
import com.example.waterremove.ui.theme.WaterRemoveTheme
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private data class IntroContent(
    val emoji: String,
    @param:StringRes val title: Int,
    @param:StringRes val description: Int,
)

private fun introFor(page: Int) = when (page) {
    2 -> IntroContent("🌊", R.string.onb2_title, R.string.onb2_desc)
    else -> IntroContent("🔊", R.string.onb3_title, R.string.onb3_desc)
}



@Composable
fun OnboardingIntroScreen(page: Int, onNext: () -> Unit) {
    val content = introFor(page)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(content.emoji, fontSize = 96.sp)
            }
            Spacer(Modifier.height(40.dp))
            Text(
                stringResource(content.title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                stringResource(content.description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        OnboardingFooter(pageIndex = page - 1, looksEnabled = true, onNextClick = onNext)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun OnboardingIntroPreview() {
    WaterRemoveTheme() {
        OnboardingIntroScreen(page = 2, onNext = {})
    }
}