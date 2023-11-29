package org.helios.mythicdoors.viewmodel.tools

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import org.helios.mythicdoors.utils.typeclass.Language
import org.helios.mythicdoors.R
import org.helios.mythicdoors.store.StoreManager
import java.util.*

class LanguageManagerViewModel(): ViewModel() {
    companion object {
        private lateinit var instance: LanguageManagerViewModel

        fun getInstance(): LanguageManagerViewModel {
            if (!::instance.isInitialized) {
                instance = LanguageManagerViewModel()
            }
            return instance
        }
    }

    private val store: StoreManager by lazy { StoreManager.getInstance() }
    val languages: Map<String, Language> = store.getLanguages()

    @Suppress("DEPRECATION")
    fun changeLanguage(
        language: Language,
        activity: Activity,
        activityContext: Context
    ) {
        val locale = language.languageLocale
        Locale.setDefault(locale)

        val resources = activityContext.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)

        activityContext.createConfigurationContext(configuration)
        resources.configuration.updateFrom(configuration)

        /* This method is deprecated, but it must be a bug in SDK 34 because it doesn't work without it */
        resources.updateConfiguration(configuration, resources.displayMetrics)

        SharedPreferences.Editor::class.java
            .getMethod("apply")
            .invoke(
                activityContext.getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
                    .putString("language", language.languageCode)
            )

        ActivityCompat.recreate(activity)
    }

    fun setLanguageFlag(): Int {
        val actualLocaleLanguage: String = Locale.getDefault().language

        return languages.filter { it.value.languageLocale.language == actualLocaleLanguage }.values.firstOrNull()?.languageFlag
            ?: R.drawable.ic_english_flag
    }
}