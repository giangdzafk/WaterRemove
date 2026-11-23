package com.example.waterremove.ui.feature.language


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.waterremove.R
import com.example.waterremove.ui.components.NativeAdSlot
import com.example.waterremove.ui.components.SelectableCard
import com.example.waterremove.ui.components.showToast
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.waterremove.ui.theme.WaterRemoveTheme


@Composable
fun LanguageScreen(
    onNext: () -> Unit,
    vm: LanguageViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val selectFirstMessage = stringResource(R.string.toast_select_language)

    LanguageContent(
        state = state,
        onLanguageClick = vm::select,
        onNextClick = {
            val selected = state.selected
            if (selected == null) {
                context.showToast(selectFirstMessage)
            } else {
                onNext()
                applyAppLocale(selected.code)
            }
        },
    )
}


@Composable
fun LanguageContent(
    state: LanguageUiState,
    onLanguageClick: (AppLanguage) -> Unit,
    onNextClick: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LanguageTopBar(looksEnabled = state.selected != null, onNextClick = onNextClick)
        },
        bottomBar = {
            NativeAdSlot(
                placement = if (state.selected == null) "native_language_1" else "native_language_2",
                modifier = Modifier.navigationBarsPadding(),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(state.languages, key = { it.code }) { language ->
                LanguageItem(
                    language = language,
                    selected = language == state.selected,
                    onClick = { onLanguageClick(language) },
                )
            }
        }
    }
}

@Composable
private fun LanguageTopBar(looksEnabled: Boolean, onNextClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 20.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.language_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f),
        )
        // Không dùng enabled=false để vẫn bắt được click và hiện toast
        IconButton(onClick = onNextClick) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = stringResource(R.string.action_next),
                tint = if (looksEnabled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            )
        }
    }
}

@Composable
private fun LanguageItem(language: AppLanguage, selected: Boolean, onClick: () -> Unit) {
    SelectableCard(selected = selected, onClick = onClick) {
        Text(language.flag, fontSize = 28.sp)
        Column(modifier = Modifier.weight(1f)) {
            Text(language.nativeName, style = MaterialTheme.typography.titleMedium)
            if (language.nativeName != language.englishName) {
                Text(
                    language.englishName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        RadioButton(selected = selected, onClick = null)
    }
}

@Preview(showSystemUi = true, name = "Chưa chọn")
@Composable
private fun LanguageNotSelectedPreview() {
    WaterRemoveTheme {
        LanguageContent(
            state = LanguageUiState(languages = Languages.sortedFor("vi")),
            onLanguageClick = {},
            onNextClick = {},
        )
    }
}

@Preview(showSystemUi = true, name = "Đã chọn")
@Composable
private fun LanguageSelectedPreview() {
    val languages = Languages.sortedFor("vi")
    WaterRemoveTheme() {
        LanguageContent(
            state = LanguageUiState(languages = languages, selected = languages.first()),
            onLanguageClick = {},
            onNextClick = {},
        )
    }
}