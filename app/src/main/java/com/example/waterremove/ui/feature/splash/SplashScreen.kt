package com.example.waterremove.ui.feature.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.waterremove.R
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

// TODO: khi gắn AdMob, thanh loading chạy tới khi inter_splash load xong (tối đa 6s).
private const val SPLASH_DURATION_MS = 3_000

@Composable
fun SplashScreen(
    onFinished: (firstOpen: Boolean) -> Unit,
    vm: SplashViewModel = hiltViewModel(),
) {
    val progress = remember { Animatable(0f) }
    val currentOnFinished by rememberUpdatedState(onFinished)

    LaunchedEffect(Unit) {
        coroutineScope {
            // Đọc DataStore song song với animation loading
            val firstOpen = async { vm.isFirstLaunch() }
            progress.animateTo(1f, tween(durationMillis = SPLASH_DURATION_MS, easing = LinearEasing))
            currentOnFinished(firstOpen.await())
        }
    }

    SplashContent(progress = { progress.value })
}

@Composable
fun SplashContent(progress: () -> Float) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.splash_bg))
            .systemBarsPadding()
            .padding(horizontal = 40.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(112.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center,
        ) {
            Text("💧", fontSize = 56.sp)
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )

        Spacer(Modifier.weight(1f))

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.3f),
            drawStopIndicator = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SplashScreenPreview() {
    SplashContent(progress = { 0.5f })
}
