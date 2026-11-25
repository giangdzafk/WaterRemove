package com.example.waterremove.ui.feature.onboarding

import androidx.annotation.StringRes
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.example.waterremove.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * 4 lựa chọn ở Onboarding 1 ("What do you want to clean?").
 * Chỉ giữ ID của chữ và ảnh; màn hình gọi stringResource / painterResource để lấy ra.
 */
enum class CleanTarget(@param:StringRes val titleRes: Int, @param:DrawableRes val imageRes: Int) {
    WATER(R.string.target_water, R.drawable.on_water),
    DUST(R.string.target_dust, R.drawable.on_dust),
    POLLEN(R.string.target_pollen, R.drawable.on_pollen),
    SAND(R.string.target_sand, R.drawable.on_sand),
}

@HiltViewModel
class OnboardingSelectViewModel @Inject constructor() : ViewModel() {

    private val _selected = MutableStateFlow<Set<CleanTarget>>(emptySet())
    val selected: StateFlow<Set<CleanTarget>> = _selected.asStateFlow()

    /** Bấm lần 1 → chọn, bấm lần 2 → bỏ chọn (chọn được 1–4 option). */
    fun toggle(target: CleanTarget) {
        _selected.update { current -> if (target in current) current - target else current + target }
    }
}
