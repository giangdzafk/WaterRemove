package com.example.waterremove.ui.feature.cleaner

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.waterremove.R
import com.example.waterremove.core.audio.ToneSegment
import com.example.waterremove.core.audio.TonePrograms
import com.example.waterremove.ui.components.AppIcons

enum class WetnessLevel(@param:StringRes val titleRes: Int, val drops: Int, val program: List<ToneSegment>) {
    SLIGHTLY(R.string.wet_slightly, 1, TonePrograms.waterSlightly),    // 15s
    HEAVILY(R.string.wet_heavily, 2, TonePrograms.waterHeavily),       // 36s
    STRONG(R.string.wet_strong, 3, TonePrograms.waterStrong),          // 60s
}


enum class CleanerFeature(
    val key: String,
    @param:StringRes val screenTitle: Int,
    @param:StringRes val modeTitle: Int,
    @param:StringRes val modeDesc: Int,
    val icon: ImageVector,
) {
    WATER("water", R.string.water_remover, R.string.water_remove_title, R.string.water_remove_desc, AppIcons.WaterDrop),
    DUST("dust", R.string.dust_remover, R.string.dust_remove_title, R.string.dust_remove_desc, AppIcons.Dust);

    fun program(level: Int): List<ToneSegment> = when (this) {
        WATER -> WetnessLevel.entries[level.coerceIn(0, WetnessLevel.entries.lastIndex)].program
        DUST -> TonePrograms.dust
    }

    companion object {
        fun fromKey(key: String?): CleanerFeature = entries.firstOrNull { it.key == key } ?: WATER
    }
}
