package com.example.waterremove.ui.feature.tutorial

import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.waterremove.R
import com.example.waterremove.ui.components.AppIcons
import com.example.waterremove.ui.components.AppTopBar
import com.example.waterremove.ui.components.NumberBadge
import com.example.waterremove.ui.theme.Blue
import com.example.waterremove.ui.theme.BlueLight
import com.example.waterremove.ui.theme.WaterRemoveTheme

@Composable
fun TutorialScreen(featureKey: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        AppTopBar(title = stringResource(R.string.tutorial), onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .navigationBarsPadding(),
        ) {
            if (featureKey == "sound") SoundTutorial() else CleanerTutorial()
        }
    }
}

@Composable
private fun CleanerTutorial() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        StepCard(1, AppIcons.Speaker, R.string.tut_step1_title, R.string.tut_step1_desc)
        StepCard(2, AppIcons.WaterDrop, R.string.tut_step2_title, R.string.tut_step2_desc)
    }
}

@Composable
private fun StepCard(number: Int, icon: ImageVector, @StringRes title: Int, @StringRes desc: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth()) { NumberBadge(number) }
        // TODO: thay icon bằng ảnh minh hoạ export từ Figma
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(BlueLight),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null, tint = Blue, modifier = Modifier.size(44.dp))
        }
        Spacer(Modifier.height(12.dp))
        Text(stringResource(title), fontSize = 15.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(4.dp))
        Text(
            stringResource(desc),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

/** Sound test: ảnh + "How to Run the Frequency Test?" + 4 bước đánh số. */
@Composable
private fun SoundTutorial() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(BlueLight),
            contentAlignment = Alignment.Center,
        ) {
            Icon(AppIcons.Speaker, null, tint = Blue, modifier = Modifier.size(52.dp))
        }
        Spacer(Modifier.height(20.dp))
    }
    Text(stringResource(R.string.tut_sound_title), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(6.dp))
    Text(stringResource(R.string.tut_sound_desc), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.height(14.dp))
    val steps = listOf(
        R.string.tut_sound_1_title to R.string.tut_sound_1_desc,
        R.string.tut_sound_2_title to R.string.tut_sound_2_desc,
        R.string.tut_sound_3_title to R.string.tut_sound_3_desc,
        R.string.tut_sound_4_title to R.string.tut_sound_4_desc,
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        steps.forEachIndexed { i, (title, desc) ->
            val t = stringResource(title)
            val d = stringResource(desc)
            Row(verticalAlignment = Alignment.Top) {
                NumberBadge(i + 1, Modifier.size(22.dp))
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(t) }
                        append(" ")
                        append(d)
                    },
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }
        }
    }
}

@Preview(showSystemUi = true, name = "Water/Dust")
@Composable
private fun TutorialCleanerPreview() {
    WaterRemoveTheme { TutorialScreen("water") {} }
}

@Preview(showSystemUi = true, name = "Sound")
@Composable
private fun TutorialSoundPreview() {
    WaterRemoveTheme { TutorialScreen("sound") {} }
}
