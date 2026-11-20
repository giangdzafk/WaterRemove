package com.example.waterremove.ui.feature.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

data class AppLanguage(
    val code: String,
    val englishName: String,
    val nativeName: String,
    val flag: String,
)

object Languages {
    val all = listOf(
        AppLanguage("en", "English", "English", "🇺🇸"),
        AppLanguage("vi", "Vietnamese", "Tiếng Việt", "🇻🇳"),
        AppLanguage("hi", "Hindi", "हिन्दी", "🇮🇳"),
        AppLanguage("es", "Spanish", "Español", "🇪🇸"),
        AppLanguage("fr", "French", "Français", "🇫🇷"),
        AppLanguage("pt", "Portuguese", "Português", "🇧🇷"),
        AppLanguage("de", "German", "Deutsch", "🇩🇪"),
        AppLanguage("id", "Indonesian", "Bahasa Indonesia", "🇮🇩"),
        AppLanguage("ja", "Japanese", "日本語", "🇯🇵"),
        AppLanguage("ko", "Korean", "한국어", "🇰🇷"),
        AppLanguage("ru", "Russian", "Русский", "🇷🇺"),
        AppLanguage("ar", "Arabic", "العربية", "🇸🇦"),
    )


    fun sortedFor(deviceCode: String): List<AppLanguage> {
        val code = normalize(deviceCode)
        val english = all.first { it.code == "en" }
        val device = all.find { it.code == code && it.code != "en" }
        val rest = all.filter { it != english && it != device }.sortedBy { it.englishName }
        return listOfNotNull(device, english) + rest
    }

    // Android cũ trả mã ngôn ngữ kiểu cũ: Indonesia = "in", Hebrew = "iw"
    private fun normalize(code: String) = when (code) {
        "in" -> "id"
        "iw" -> "he"
        else -> code
    }
}


fun applyAppLocale(code: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(code))
}