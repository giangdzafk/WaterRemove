package com.example.waterremove.ui.feature.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.waterremove.R
import com.example.waterremove.ui.components.AppIcons
import com.example.waterremove.ui.components.AppTopBar
import com.example.waterremove.ui.feature.rate.RateFlow
import com.example.waterremove.ui.feature.rate.RateViewModel
import com.example.waterremove.ui.theme.Blue
import com.example.waterremove.ui.theme.WaterRemoveTheme

// TODO: thay bằng link chính sách bảo mật thật trước khi phát hành
private const val PRIVACY_POLICY_URL = "https://sites.google.com/view/water-remove-privacy"

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLanguage: () -> Unit,
    rateVm: RateViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val showRate by rateVm.visible.collectAsStateWithLifecycle()
    val appName = stringResource(R.string.app_name)

    SettingsContent(
        onBack = onBack,
        onLanguage = onLanguage,
        onRate = rateVm::show,
        onShare = { context.shareApp(appName) },
        onPrivacy = { context.openUrl(PRIVACY_POLICY_URL) },
    )

    if (showRate) RateFlow(onDismiss = rateVm::dismiss, onRated = rateVm::markRated)
}

@Composable
fun SettingsContent(
    onBack: () -> Unit,
    onLanguage: () -> Unit,
    onRate: () -> Unit,
    onShare: () -> Unit,
    onPrivacy: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        AppTopBar(title = stringResource(R.string.settings), onBack = onBack)
        SettingRow(AppIcons.Globe, stringResource(R.string.language_title), onLanguage)
        SettingRow(Icons.Outlined.Star, stringResource(R.string.rate_this_app), onRate)
        SettingRow(Icons.Outlined.Share, stringResource(R.string.share_us), onShare)
        SettingRow(AppIcons.Shield, stringResource(R.string.privacy_policy), onPrivacy)
    }
}

@Composable
private fun SettingRow(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = Blue, modifier = Modifier.size(22.dp))
        Text(title, fontSize = 15.sp, modifier = Modifier
            .weight(1f)
            .padding(start = 12.dp))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun Context.shareApp(appName: String) {
    val link = "https://play.google.com/store/apps/details?id=$packageName"
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text, appName, link))
    }
    startActivity(Intent.createChooser(send, null))
}

private fun Context.openUrl(url: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    } catch (_: ActivityNotFoundException) {
        // Máy không có trình duyệt — bỏ qua
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsPreview() {
    WaterRemoveTheme { SettingsContent({}, {}, {}, {}, {}) }
}
