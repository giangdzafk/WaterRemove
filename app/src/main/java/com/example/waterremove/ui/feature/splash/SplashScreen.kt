package com.example.waterremove.ui.feature.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.waterremove.R
import com.example.waterremove.ui.components.AppIcons
import com.example.waterremove.ui.theme.Blue
import com.example.waterremove.ui.theme.WaterRemoveTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

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
            val firstOpen = async { vm.isFirstLaunch() }
            progress.animateTo(1f, tween(durationMillis = SPLASH_DURATION_MS, easing = LinearEasing))
            currentOnFinished(firstOpen.await())
        }
    }

    SplashContent(progress = { progress.value })
}

/** Nửa trên trắng có logo + tên app, nửa dưới là sóng xanh chứa thanh loading (theo Figma). */
@Composable
fun SplashContent(progress: () -> Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        // Sóng xanh phía dưới
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.42f)
                .align(Alignment.BottomCenter),
        ) {
            val w = size.width
            val h = size.height
            val path = Path().apply {
                moveTo(0f, h * 0.12f)
                cubicTo(w * 0.3f, -h * 0.06f, w * 0.65f, h * 0.22f, w, h * 0.02f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(path, Blue)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(0.8f))
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Blue),
                contentAlignment = Alignment.Center,
            ) {
                Icon(AppIcons.WaterDrop, null, tint = Color.White, modifier = Modifier.size(52.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.app_name),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.weight(1f))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.35f),
                drawStopIndicator = {},
            )
            Spacer(Modifier.height(72.dp))
            Spacer(Modifier.navigationBarsPadding())
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SplashScreenPreview() {
    WaterRemoveTheme { SplashContent(progress = { 0.5f }) }
}
