package org.helios.mythicdoors.viewmodel.tools

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val context: Context by lazy { store.getContext() ?: throw Exception("Context is null") }

    var languageName = context.getString(R.string.locale_default_language_name)
    var languageFlag = R.drawable.ic_english_flag
    var locale: Locale = Locale.ENGLISH

    private val _languageChanged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val languageChanged: StateFlow<Boolean> get() = _languageChanged

    fun setLanguageChanged(value: Boolean) { _languageChanged.value = value }

    fun changeLanguage(context: Context) {
        try {
            loadLanguage(context)
        } catch (e: Exception) {
            Log.e("LANGUAGE", "Error loading language")
        }
    }

    fun setLanguage(
        locale: Locale,
        languageName: String,
        languageFlag: Int
    ) {
        this.locale = locale
        this.languageName = languageName
        this.languageFlag = languageFlag
        _languageChanged.value = true
    }

    private fun loadLanguage(context: Context) {
        Locale.setDefault(locale)

        val newLocale: Locale = locale
        val newConfiguration = context.resources.configuration.apply { setLocale(newLocale) }

        context.createConfigurationContext(newConfiguration)
    }
}