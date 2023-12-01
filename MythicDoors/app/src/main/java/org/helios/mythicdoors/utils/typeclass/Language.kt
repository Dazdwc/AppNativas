package org.helios.mythicdoors.utils.typeclass

import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.locales.Locales
import java.util.*

class Language(
    val languageLocale: Locale,
    val languageName: String,
    val languageCode: String,
    val languageFlag: Int
) {
    override fun toString(): String {
        return languageName
    }

    companion object {
        val ENGLISH = Language(
            languageLocale = Locale.ENGLISH,
            languageName = "English",
            languageCode = "en",
            languageFlag = R.drawable.ic_english_flag
        )
        val SPANISH = Language(
            languageLocale = Locales.spainLocale,
            languageName = "Español",
            languageCode = "es",
            languageFlag = R.drawable.ic_spanish_flag
        )
        val CATALAN = Language(
            languageLocale = Locale("ca", "ES"),
            languageName = "Català",
            languageCode = "ca",
            languageFlag = R.drawable.ic_catalan_flag
        )
        val languages = listOf(
            ENGLISH,
            SPANISH,
            CATALAN
        )
    }
}