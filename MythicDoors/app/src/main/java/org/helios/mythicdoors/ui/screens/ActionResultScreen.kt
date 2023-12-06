package org.helios.mythicdoors.ui.screens

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
<<<<<<< HEAD
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
=======
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.entities.User
<<<<<<< HEAD
import org.helios.mythicdoors.services.interfaces.LanguageChangeListener
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.ui.fragments.AudioPlayer
import org.helios.mythicdoors.ui.fragments.MenuBar
=======
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.ACTION_RESULT_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.lenguage
import org.helios.mythicdoors.viewmodel.ActionResultScreenViewModel
import org.helios.mythicdoors.viewmodel.GameResults
import org.helios.mythicdoors.viewmodel.tools.SoundManagementViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ActionResultScreen(navController: NavController) {
    val controller: ActionResultScreenViewModel = (MainActivity.viewModelsMap[ACTION_RESULT_SCREEN_VIEWMODEL] as ActionResultScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val context: Context = LocalContext.current
    val activity = context.findActivity() as MainActivity
    val density = LocalDensity.current.density

    val soundManager: SoundManagementViewModel = (MainActivity.viewModelsMap[AppConstants.ScreensViewModels.SOUND_MANAGEMENT_SCREEN_VIEWMODEL] as SoundManagementViewModel)
        .apply { loadSoundsIfNeeded() }

    val playerCurrentStats: User by lazy { controller.playerData ?: User.createEmptyUser() }
    val gameCurrentStats: GameResults = controller.gameResultsData ?: GameResults.create(false, null, 0, 0)

    val isEnoughCoins: Boolean by lazy { controller.isEnoughCoins() }

    val captured = remember { mutableStateOf<Bitmap?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            soundManager.stopPlayingSounds()
        }
    }

    controller.initialLoad()
    soundManager.playSound(R.raw.werewolf)
    var currentLanguage by remember { mutableStateOf("en") }
    val storeManager = StoreManager.getInstance()

    DisposableEffect(Unit) {
        val observer: LanguageChangeListener = object : LanguageChangeListener {
            override fun onLanguageChanged(newLanguage: String) {
                currentLanguage = newLanguage
            }
        }
        storeManager.addObserver(observer)
        onDispose {
            storeManager.removeObserver(observer)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val width = constraints.maxWidth
            val height = constraints.maxHeight
            val maxWidth = ((width).toInt()).minus(10)

            captured.value = takeScreenShot(context, width, height)

            val isPlayerWon: Boolean = gameCurrentStats.getIsPlayerWinner()
            val onShowScreenshotDialogState = rememberSaveable { mutableStateOf(isPlayerWon) }

            ScreenshotAlertDialog(
                controller = controller,
                context = context,
                activity = activity,
                bitmap = captured.value ?: takeScreenShot(context, maxWidth, height),
                onShowScreenshotDialog = onShowScreenshotDialogState)

            Column(modifier = Modifier.verticalScroll(scrollState)) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(
                            top = ScreenConstants.DOUBLE_PADDING.dp,
                            bottom = ScreenConstants.DOUBLE_PADDING.dp
                        )
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = ScreenConstants.AVERAGE_PADDING.dp, end = ScreenConstants.AVERAGE_PADDING.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    gameCurrentStats.getIsPlayerWinner().let { isWinner ->
                        if (isWinner) {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize(Alignment.Center)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .border(1.dp, MaterialTheme.colorScheme.tertiary)
                                    .padding(bottom = ScreenConstants.AVERAGE_PADDING.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
<<<<<<< HEAD
                                    text = lenguage["win_$currentLanguage"]?:"You won!".uppercase(),
=======
                                    text = stringResource(id = R.string.game_won).uppercase(),
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .padding(ScreenConstants.AVERAGE_PADDING.dp)
                                        .padding(bottom = 0.dp),
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize(Alignment.Center)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .border(1.dp, MaterialTheme.colorScheme.tertiary)
                                    .padding(bottom = ScreenConstants.AVERAGE_PADDING.dp),
                                contentAlignment = Alignment.Center
                            ) {
<<<<<<< HEAD
                                Text(text = lenguage["lose_$currentLanguage"]?:"You lost!".uppercase(),
=======
                                Text(
                                    text = stringResource(id = R.string.game_lost).uppercase(),
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .padding(ScreenConstants.AVERAGE_PADDING.dp)
                                        .padding(bottom = 0.dp),
                                )
                            }
                        }
                    }

                    gameCurrentStats.getEnemy()?.getImage()?.let { painterResource(id = it) }
                        ?.let { Image(painter = it,
                            contentDescription = "Enemy image",
                            modifier = Modifier
                                .padding(top = ScreenConstants.AVERAGE_PADDING.dp, bottom = ScreenConstants.AVERAGE_PADDING.dp),
                        ) }
                        ?: Image(painter = painterResource(id = R.drawable.enemy_placeholder),
                            contentDescription = "Enemy image",
                            modifier = Modifier
                                .padding(top = ScreenConstants.AVERAGE_PADDING.dp, bottom = ScreenConstants.AVERAGE_PADDING.dp)
                        )

                    Row(Modifier.padding(bottom = ScreenConstants.AVERAGE_PADDING.dp)) {
                        Icon(modifier = Modifier
                            .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
                            .size(40.dp, 40.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.actual_coins_500),
                            contentDescription = "icon representing the user's actual coins",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        TextField(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary)
                                .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                                .weight(1f),
                            value = playerCurrentStats.getCoins().coerceAtLeast(0).toString(),
                            onValueChange = { playerCurrentStats.getCoins().toString() },
<<<<<<< HEAD
                            label = { Text(text = lenguage["currentcoins_$currentLanguage"]?:"Current Coins") },
=======
                            label = { Text(text = stringResource(id = R.string.current_coins)) },
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
                            readOnly = true,
                        )
                    }
                    Row(Modifier.padding(bottom = 15.dp)) {
                        Icon(modifier = Modifier
                            .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
                            .size(40.dp, 40.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.star_500),
                            contentDescription = "icon representing the user's actual experience",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        TextField(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary)
                                .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                                .weight(1f),
                            value = playerCurrentStats.getLevel().toString(),
<<<<<<< HEAD
                            onValueChange = {playerCurrentStats.getLevel().toString() },
                            label = { Text(text = lenguage["currentlvl_$currentLanguage"]?:"Current Level") },
=======
                            onValueChange = { playerCurrentStats.getLevel().toString() },
                            label = { Text(text = stringResource(id = R.string.current_level)) },
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
                            readOnly = true,
                        )
                    }
                    Spacer(modifier = Modifier.padding(top = ScreenConstants.AVERAGE_PADDING.dp))
                    Row(modifier = Modifier
                        .width((maxWidth / 2).dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ){
                        Button(onClick = { controller.returnToGameActionScreen(scope, snackbarHostState) },
                            enabled = isEnoughCoins,
                            elevation = ButtonDefaults.buttonElevation(2.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
                        ) {
<<<<<<< HEAD
                            Text(text = lenguage["continue_$currentLanguage"]?:"CONTINUE",
=======
                            Text(
                                text = stringResource(id = R.string.continue_opt).uppercase(),
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        Button(onClick = { controller.returnToGameOptionsScreen(scope, snackbarHostState) },
                            elevation = ButtonDefaults.buttonElevation(2.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = ScreenConstants.AVERAGE_PADDING.dp)
                        ) {
<<<<<<< HEAD
                            Text(text = lenguage["exit_$currentLanguage"]?:"EXIT",
=======
                            Text(
                                text = stringResource(id = R.string.exit_opt).uppercase(),
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(top = ScreenConstants.AVERAGE_PADDING.dp))
                    isEnoughCoins.takeIf { !it }?.let {
<<<<<<< HEAD
                        Text(text = lenguage["nocoins_$currentLanguage"]?:"You don't have enough coins to bet",
=======
                        Text(
                            text = stringResource(id = R.string.not_coins),
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun ScreenshotAlertDialog(
    controller: ActionResultScreenViewModel,
    context: Context,
    activity: MainActivity,
    bitmap: Bitmap,
    onShowScreenshotDialog: MutableState<Boolean>
) {
    if (onShowScreenshotDialog.value) {
        AlertDialog(
            onDismissRequest = { onShowScreenshotDialog.value = false },
            title = { Text(text = stringResource(id = R.string.screenshot_dialog_title).uppercase()) },
            text = { Text(text = stringResource(id = R.string.screenshot_dialog_text)) },
            confirmButton = {
                Button(
                    onClick = {
                        controller.createImageFile(
                            context = context,
                            activity = activity,
                            bitmap = bitmap
                        )
                        onShowScreenshotDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)
                ) {
                    Text(text = stringResource(id = R.string.screenshot_dialog_confirm).uppercase())
                }
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary),
            dismissButton = {
                Button(
                    onClick = { onShowScreenshotDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)
                ) {
                    Text(text = stringResource(id = R.string.screenshot_dialog_cancel).uppercase())
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_camera),
                    contentDescription = "screenshot icon",
                    modifier = Modifier
                        .size(40.dp, 40.dp)
                        .padding(end = ScreenConstants.AVERAGE_PADDING.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        )
    }
}

private fun Context.findActivity(): MainActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is MainActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

private fun takeScreenShot(context: Context, width: Int, height: Int): Bitmap {
    return createBitmap(width, height, Bitmap.Config.ARGB_8888)
}