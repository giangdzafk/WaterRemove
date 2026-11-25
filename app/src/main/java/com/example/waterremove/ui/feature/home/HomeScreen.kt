package com.example.waterremove.ui.feature.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.waterremove.R
import com.example.waterremove.ui.components.AppIcons
import com.example.waterremove.ui.components.AppTopBar
import com.example.waterremove.ui.feature.rate.RateFlow
import com.example.waterremove.ui.feature.rate.RateViewModel
import com.example.waterremove.ui.feature.tips.TipsList
import com.example.waterremove.ui.theme.Blue
import com.example.waterremove.ui.theme.BlueDark
import com.example.waterremove.ui.theme.Orange
import com.example.waterremove.ui.theme.OrangeLight
import com.example.waterremove.ui.theme.Purple
import com.example.waterremove.ui.theme.PurpleLight
import com.example.waterremove.ui.theme.WaterRemoveTheme

@Composable
fun HomeScreen(
    onWater: () -> Unit,
    onDust: () -> Unit,
    onSoundTest: () -> Unit,
    onTip: (Int) -> Unit,
    onSettings: () -> Unit,
    rateVm: RateViewModel = hiltViewModel(),
) {
    val showRate by rateVm.visible.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { rateVm.autoShowIfNeeded() }

    HomeContent(onWater, onDust, onSoundTest, onTip, onSettings)

    if (showRate) RateFlow(onDismiss = rateVm::dismiss, onRated = rateVm::markRated)
}

@Composable
fun HomeContent(
    onWater: () -> Unit,
    onDust: () -> Unit,
    onSoundTest: () -> Unit,
    onTip: (Int) -> Unit,
    onSettings: () -> Unit,
) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(title = stringResource(R.string.home_title)) {
                IconButton(onClick = onSettings) {
                    Icon(Icons.Outlined.Settings, stringResource(R.string.settings))
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                val colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Blue,
                    selectedTextColor = Blue,
                    indicatorColor = Color.Transparent,
                )
                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = { Icon(Icons.Outlined.Home, null) },
                    label = { Text(stringResource(R.string.tab_cleaner)) },
                    colors = colors,
                )
                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = { Icon(Icons.Outlined.Info, null) },
                    label = { Text(stringResource(R.string.tab_tips)) },
                    colors = colors,
                )
            }
        },
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (tab == 0) CleanerTab(onWater, onDust, onSoundTest)
            else TipsList(onTipClick = onTip, contentPadding = PaddingValues(16.dp))
        }
    }
}

@Composable
private fun CleanerTab(onWater: () -> Unit, onDust: () -> Unit, onSoundTest: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        WaterBanner(onClick = onWater)
        FeatureCard(
            title = stringResource(R.string.feature_dust),
            icon = AppIcons.Dust,
            iconColors = listOf(OrangeLight, Orange),
            decoImage = R.drawable.dust_removal,
            decoAlignment = Alignment.BottomEnd,
            onClick = onDust,
        )
        FeatureCard(
            title = stringResource(R.string.feature_sound),
            icon = AppIcons.Speaker,
            iconColors = listOf(PurpleLight, Purple),
            decoImage = R.drawable.test_sound,
            decoAlignment = Alignment.CenterEnd,
            onClick = onSoundTest,
        )
    }
}

@Composable
private fun WaterBanner(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(Brush.linearGradient(listOf(Color(0xFF2E86F5), BlueDark))),
        ) {
            // Minh hoạ điện thoại + giọt nước ở góc phải trên (lấn nhẹ ra mép, bị card cắt như Figma)
            Image(
                painter = painterResource(R.drawable.water_removal),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 8.dp)
                    .size(width = 172.dp, height = 150.dp),
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 18.dp),
            ) {
                Text(
                    stringResource(R.string.feature_water),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(R.string.start_cleaning), color = Blue, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Blue, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(
    title: String,
    icon: ImageVector,
    iconColors: List<Color>,
    @DrawableRes decoImage: Int,
    decoAlignment: Alignment,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(96.dp),
        ) {
            val deco = painterResource(decoImage)
            Image(
                painter = deco,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(decoAlignment)
                    .padding(end = if (decoAlignment == Alignment.BottomEnd) 0.dp else 12.dp)
                    .height(96.dp)
                    .aspectRatio(deco.intrinsicSize.width / deco.intrinsicSize.height),
            )
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(iconColors)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Column(Modifier.padding(start = 14.dp)) {
                    Text(title, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                    Text(
                        stringResource(R.string.tap_to_try),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomePreview() {
    WaterRemoveTheme { HomeContent({}, {}, {}, {}, {}) }
}
