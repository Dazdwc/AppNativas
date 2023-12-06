package org.helios.mythicdoors.utils.typeclass

import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.locales.Locales
import java.util.*

class Language(
    val languageLocale: Locale,
    val languageName: String,
    val languageFlag: Int
) {
    override fun toString(): String {
        return languageName
    }

    companion object {
        private val ENGLISH = Language(
            languageLocale = Locale.ENGLISH,
            languageName = "English",
            languageFlag = R.drawable.ic_english_flag
        )
        private val SPANISH = Language(
            languageLocale = Locales.spainLocale,
            languageName = "Español",
            languageFlag = R.drawable.ic_spanish_flag
        )
        private val CATALAN = Language(
            languageLocale = Locale("ca", "ES"),
            languageName = "Català",
            languageFlag = R.drawable.ic_catalan_flag
        )
        val languages = listOf(
            ENGLISH,
            SPANISH,
            CATALAN
        )
    }
}