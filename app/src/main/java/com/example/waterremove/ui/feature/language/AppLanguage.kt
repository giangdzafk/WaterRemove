package com.example.waterremove.ui.feature.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

data class AppLanguage(
    val code: String,
    val englishName: String,
    val nativeName: String,
)

object Languages {
    val all = listOf(
        AppLanguage("en", "English", "English"),
        AppLanguage("es", "Spanish", "Español"),
        AppLanguage("fr", "French", "Français"),
        AppLanguage("pt", "Portuguese", "Português"),
        AppLanguage("de", "German", "Deutsch"),
        AppLanguage("hi", "Indian", "हिंदी"),
        AppLanguage("vi", "Vietnamese", "Tiếng Việt"),
        AppLanguage("ar", "Arabic", "عربي"),
        AppLanguage("id", "Indonesian", "Bahasa Indo"),
        AppLanguage("fil", "Filipino", "Pilipinas"),
        AppLanguage("ru", "Russian", "Русский язык"),
    )


    fun sortedFor(deviceCode: String): List<AppLanguage> {
        val code = normalize(deviceCode)
        val english = all.first { it.code == "en" }
        val device = all.find { it.code == code && it.code != "en" }
        val rest = all.filter { it != english && it != device }.sortedBy { it.englishName }
        return listOfNotNull(device, english) + rest
    }

    fun byCode(code: String?): AppLanguage? = code?.let { c -> all.find { it.code == normalize(c) } }

    private fun normalize(code: String) = when (code) {
        "in" -> "id"
        "tl" -> "fil"
        else -> code
    }
}

fun applyAppLocale(code: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(code))
}

fun currentAppLocaleCode(): String? = AppCompatDelegate.getApplicationLocales()[0]?.language
