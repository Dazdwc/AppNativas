package org.helios.mythicdoors.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.LANGUAGE_MANAGER_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.typeclass.Language
import org.helios.mythicdoors.viewmodel.tools.LanguageManagerViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LanguageManager(
    activity: MainActivity,
    activityContext: Context
) {
    val controller: LanguageManagerViewModel = (MainActivity.viewModelsMap[LANGUAGE_MANAGER_SCREEN_VIEWMODEL] as LanguageManagerViewModel)
    val languages: Map<String, Language> = remember { controller.languages }
    val openDialog = rememberSaveable { mutableStateOf(false) }

    Surface {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(id = R.string.language_icon_title),
                    style = MaterialTheme.typography.labelSmall
                )
                IconButton(onClick = { openDialog.value = !openDialog.value }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.arrow_down_float),
                        contentDescription = "Arrow down icon for the language selector.",
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
    CreateLanguageDialog(
        openDialog = openDialog,
        activity = activity,
        activityContext = activityContext,
        controller = controller,
        languages = languages
    )
}

@Composable
fun CreateLanguageDialog(
    openDialog: MutableState<Boolean>,
    activity: Activity,
    activityContext: Context,
    controller: LanguageManagerViewModel,
    languages: Map<String, Language>
) {
    val languageNames: List<String> = languages.values.map { it.languageName }

    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(AppConstants.ScreenConstants.AVERAGE_PADDING.dp)
            ) {
                Text(text = stringResource(id = R.string.language_manager_title))
                Spacer(modifier = Modifier.height(8.dp))
                languageNames.forEach { languageName ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ClickableText(
                            text = AnnotatedString.Builder(languageName)
                                .apply {
                                    addStyle(
                                        style = SpanStyle(
                                            color = MaterialTheme.colorScheme.onBackground
                                        ),
                                        start = 0,
                                        end = languageName.length
                                    )
                                }
                                .toAnnotatedString(),
                            onClick = {
                                controller.changeLanguage(
                                    languages[languageName] ?: Language.ENGLISH,
                                    activity,
                                    activityContext
                                )
                                openDialog.value = false
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                TextButton(
                    onClick = { openDialog.value = false }) {
                    Text(text = stringResource(id = R.string.close_button))
                }
            }
        }
    }
}
