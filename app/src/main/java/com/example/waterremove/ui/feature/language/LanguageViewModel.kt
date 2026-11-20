package com.example.waterremove.ui.feature.language


import android.content.Context
import androidx.core.app.LocaleManagerCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LanguageUiState(
    val languages: List<AppLanguage>,
    val selected: AppLanguage? = null,
)

@HiltViewModel
class LanguageViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    private val deviceCode = LocaleManagerCompat.getSystemLocales(context)[0]?.language ?: "en"

    private val _state = MutableStateFlow(LanguageUiState(languages = Languages.sortedFor(deviceCode)))
    val state: StateFlow<LanguageUiState> = _state.asStateFlow()

    fun select(language: AppLanguage) {
        _state.update { it.copy(selected = language) }
    }
}