package com.example.waterremove.ui.feature.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.waterremove.R
import com.example.waterremove.ui.components.AppTopBar
import com.example.waterremove.ui.components.SelectableCard
import com.example.waterremove.ui.components.showToast
import com.example.waterremove.ui.theme.WaterRemoveTheme


@Composable
fun LanguageScreen(
    onDone: () -> Unit,
    onBack: (() -> Unit)? = null,
    vm: LanguageViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val selectFirstMessage = stringResource(R.string.toast_select_language)

    if (onBack != null) LaunchedEffect(Unit) { vm.preselectCurrent() }

    LanguageContent(
        state = state,
        onBack = onBack,
        onLanguageClick = vm::select,
        onDoneClick = {
            val selected = state.selected
            if (selected == null) {
                context.showToast(selectFirstMessage)
            } else {
                onDone()
                applyAppLocale(selected.code)
            }
        },
    )
}

@Composable
fun LanguageContent(
    state: LanguageUiState,
    onBack: (() -> Unit)?,
    onLanguageClick: (AppLanguage) -> Unit,
    onDoneClick: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AppTopBar(title = stringResource(R.string.language_title), onBack = onBack) {
                // Không dùng enabled=false để vẫn bắt được click và hiện toast
                IconButton(onClick = onDoneClick) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = stringResource(R.string.action_next),
                        tint = if (state.selected != null) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline,
                    )
                }
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.languages, key = { it.code }) { language ->
                LanguageItem(
                    language = language,
                    selected = language == state.selected,
                    onClick = { onLanguageClick(language) },
                )
            }
            item { Box(Modifier.navigationBarsPadding()) }
        }
    }
}

@Composable
private fun LanguageItem(language: AppLanguage, selected: Boolean, onClick: () -> Unit) {
    SelectableCard(selected = selected, onClick = onClick) {
        if (selected) {
            Checkbox(checked = true, onCheckedChange = null, modifier = Modifier.size(20.dp))
        }
        Text(language.englishName, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(language.nativeName, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showSystemUi = true, name = "Chưa chọn")
@Composable
private fun LanguageNotSelectedPreview() {
    WaterRemoveTheme {
        LanguageContent(
            state = LanguageUiState(languages = Languages.sortedFor("vi")),
            onBack = null, onLanguageClick = {}, onDoneClick = {},
        )
    }
}

@Preview(showSystemUi = true, name = "Đã chọn (từ Setting)")
@Composable
private fun LanguageSelectedPreview() {
    val languages = Languages.sortedFor("en")
    WaterRemoveTheme {
        LanguageContent(
            state = LanguageUiState(languages = languages, selected = languages.first()),
            onBack = {}, onLanguageClick = {}, onDoneClick = {},
        )
    }
}
