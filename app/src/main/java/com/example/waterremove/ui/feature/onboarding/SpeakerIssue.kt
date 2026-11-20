package com.example.waterremove.ui.feature.onboarding


import androidx.annotation.StringRes
import com.example.waterremove.R

/**
 * Các option ở Onboarding 1 (nội dung tạm — sửa cho đúng chữ trong Figma).
 * Enum chứa ID chuỗi chứ không chứa chữ → đổi ngôn ngữ thì chữ đổi theo.
 */
enum class SpeakerIssue(@param:StringRes val titleRes: Int, val emoji: String) {
    WATER(R.string.issue_water, "💧"),
    DUST(R.string.issue_dust, "🌫️"),
    LOW_SOUND(R.string.issue_low_sound, "🔉"),
    TEST(R.string.issue_test, "🔊"),
}