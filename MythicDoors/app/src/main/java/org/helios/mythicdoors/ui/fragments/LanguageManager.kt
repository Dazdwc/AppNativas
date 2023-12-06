package org.helios.mythicdoors.ui.fragments

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.utils.typeclass.Language.Companion.languages
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.LANGUAGE_MANAGER_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.typeclass.Language
import org.helios.mythicdoors.viewmodel.tools.LanguageManagerViewModel
import java.util.Locale

@Composable
fun LanguageManager(
    activity: MainActivity
) {
    val controller: LanguageManagerViewModel = (MainActivity.viewModelsMap[LANGUAGE_MANAGER_SCREEN_VIEWMODEL] as LanguageManagerViewModel)

    var selectedLocale: Locale = remember { controller.locale }
    var selectedLanguageName: String = remember { controller.languageName }
    var selectedLanguageFlag: Int = remember { controller.languageFlag }

    val languageChangesListener by controller.languageChanged.collectAsState()
    if (languageChangesListener) {
        val newLocale = controller.locale
        Locale.setDefault(newLocale)
        val newConfiguration = Configuration(activity.resources.configuration)
        newConfiguration.setLocale(newLocale)
        activity.baseContext.createConfigurationContext(newConfiguration)

        val context = LocalContext.current
        if (context is Activity) context.recreate()

        controller.setLanguageChanged(false)
    }

    var isMenuExpanded by remember { mutableStateOf(false) }

    Surface {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isMenuExpanded = !isMenuExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    painter = painterResource(id = selectedLanguageFlag),
                    contentDescription = "Icon for the actual language.",
                    modifier = Modifier.size(20.dp)
                )
                IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.arrow_down_float),
                        contentDescription = "Arrow down icon for the language selector.",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false } ) {
                languages.forEach() { language ->
                    DropdownMenuItem(
                        text = { Row {
                            Icon(
                                painter = painterResource(id = language.languageFlag),
                                contentDescription = "Icon for the actual language.",
                                modifier = Modifier.size(20.dp)
                            )
                            Text(text = language.languageName)
                        } },
                        onClick = {
                            isMenuExpanded = false

                            controller.setLanguage(
                                language.languageLocale,
                                language.languageName,
                                language.languageFlag)

                            selectedLocale = controller.locale
                            selectedLanguageName = controller.languageName
                            selectedLanguageFlag = controller.languageFlag
                    })
                }
            }
        }
    }
}