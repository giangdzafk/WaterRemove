package com.example.waterremove.ui.feature.onboarding


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OnboardingSelectViewModel @Inject constructor() : ViewModel() {

    private val _selected = MutableStateFlow<Set<SpeakerIssue>>(emptySet())
    val selected: StateFlow<Set<SpeakerIssue>> = _selected.asStateFlow()

    fun toggle(issue: SpeakerIssue) {
        _selected.update { current -> if (issue in current) current - issue else current + issue }
    }
}