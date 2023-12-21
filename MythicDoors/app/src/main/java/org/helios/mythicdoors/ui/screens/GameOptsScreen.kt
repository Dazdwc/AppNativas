package org.helios.mythicdoors.ui.screens

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.utils.AppConstants.GameMode
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.GAME_OPTS_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.GameOptsScreenViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun GameOptsScreen(navController: NavController) {
    val controller: GameOptsScreenViewModel = (MainActivity.viewModelsMap[GAME_OPTS_SCREEN_VIEWMODEL] as GameOptsScreenViewModel).apply { setNavController(navController) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    val isGameStarted by controller.isGameModeSelected.observeAsState(false)
    LaunchedEffect(isGameStarted, snackbarHostState) {
        if (isGameStarted) {
            Toast.makeText(
                context,
                context.getString(R.string.game_opts_screen_game_start),
                Toast.LENGTH_SHORT
            ).show()
            controller.navigateToGameActionScreen(scope, snackbarHostState)
            controller.resetIsGameStarted()
        }
    }

    val isGoldCoinsDialogOpen = remember { mutableStateOf(false) }
    if (isGoldCoinsDialogOpen.value) {
        CreateGoldCoinsDialog(
            isGoldCoinDialogOpen = isGoldCoinsDialogOpen,
            context = context,
            controller = controller
            )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column{
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(top = ScreenConstants.DOUBLE_PADDING.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
            )
            Text(
                text = stringResource(id = R.string.game_opts),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(
                        top = ScreenConstants.DOUBLE_PADDING.dp,
                        bottom = ScreenConstants.DOUBLE_PADDING.dp
                    )
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
            )
            Column(Modifier.verticalScroll(scrollState)) {
                Row {
                    Column(modifier = Modifier
                        .padding(
                            top = ScreenConstants.AVERAGE_PADDING.dp,
                            bottom = ScreenConstants.DOUBLE_PADDING.dp,
                            end = ScreenConstants.AVERAGE_PADDING.dp
                        )
                        .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(painterResource(id = R.drawable.claptrap_rich),
                            contentDescription = "Single player image",
                            Modifier.size(200.dp, 200.dp))
                        Button(onClick = {
                            controller.startSinglePlayerGame(
                                gameMode = GameMode.SINGLE_PLAYER.toString()
                            ) },
                            modifier = Modifier
                                .padding(
                                    top = ScreenConstants.DOUBLE_PADDING.dp,
                                    bottom = ScreenConstants.DOUBLE_PADDING.dp
                                )
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            elevation = ButtonDefaults.buttonElevation(2.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.single_player).uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                    Column(modifier = Modifier
                        .padding(
                            top = ScreenConstants.AVERAGE_PADDING.dp,
                            bottom = ScreenConstants.DOUBLE_PADDING.dp,
                            end = ScreenConstants.AVERAGE_PADDING.dp
                        )
                        .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(painterResource(id = R.drawable.multiplayer),
                            contentDescription = "Multiplayer image",
                            Modifier.size(200.dp, 200.dp))
                        Button(modifier = Modifier
                            .padding(
                                top = ScreenConstants.DOUBLE_PADDING.dp,
                                bottom = ScreenConstants.DOUBLE_PADDING.dp
                            )
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                            elevation = ButtonDefaults.buttonElevation(2.dp),
                            enabled = true,
                            onClick = {
                                if (controller.checkIfUserHaveCoinsToPlay()) {
                                    controller.startMultiplayerGame(
                                        gameMode = GameMode.MULTI_PLAYER.toString(),
                                    )
                                } else {
                                    isGoldCoinsDialogOpen.value = true
                                }
                            }
                        ) {
                            Text(text = stringResource(id = R.string.multi_player).uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
                Row {
                    Column(modifier = Modifier
                        .padding(
                            top = ScreenConstants.AVERAGE_PADDING.dp,
                            bottom = ScreenConstants.DOUBLE_PADDING.dp,
                            end = ScreenConstants.AVERAGE_PADDING.dp
                        )
                        .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(painterResource(id = R.drawable.sirtrap),
                            contentDescription = "Scores Ladder image",
                            Modifier.size(200.dp, 200.dp))
                        Button(onClick = { controller.navigateToScoresScreen(scope, snackbarHostState) },
                            modifier = Modifier
                                .padding(
                                    top = ScreenConstants.DOUBLE_PADDING.dp,
                                    bottom = ScreenConstants.DOUBLE_PADDING.dp
                                )
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            elevation = ButtonDefaults.buttonElevation(2.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.ladder).uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                    Column(modifier = Modifier
                        .padding(
                            top = ScreenConstants.AVERAGE_PADDING.dp,
                            bottom = ScreenConstants.DOUBLE_PADDING.dp,
                            end = ScreenConstants.AVERAGE_PADDING.dp
                        )
                        .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(painterResource(id = R.drawable.oldclap),
                            contentDescription = "Logout Image",
                            Modifier.size(200.dp, 200.dp))
                        Button(modifier = Modifier
                            .padding(
                                top = ScreenConstants.DOUBLE_PADDING.dp,
                                bottom = ScreenConstants.DOUBLE_PADDING.dp
                            )
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                            elevation = ButtonDefaults.buttonElevation(2.dp),
                            onClick = { controller.logout(scope, snackbarHostState) },
                        ) {
                            Text(text = stringResource(id = R.string.logout).uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
                Row {
                    Column(modifier = Modifier
                        .padding(
                            top = ScreenConstants.AVERAGE_PADDING.dp,
                            bottom = ScreenConstants.DOUBLE_PADDING.dp,
                            end = ScreenConstants.AVERAGE_PADDING.dp
                        )
                        .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(painterResource(id = R.drawable.clapwub),
                            contentDescription = "Game Guide image",
                            Modifier.size(200.dp, 200.dp))
                        Button(onClick = { controller.navigateToGameGuideWebView(scope, snackbarHostState) },
                            modifier = Modifier
                                .padding(
                                    top = ScreenConstants.DOUBLE_PADDING.dp,
                                    bottom = ScreenConstants.DOUBLE_PADDING.dp
                                )
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            elevation = ButtonDefaults.buttonElevation(2.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.guide).uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateGoldCoinsDialog(
    isGoldCoinDialogOpen: MutableState<Boolean> = remember { mutableStateOf(false) },
    context: Context,
    controller: GameOptsScreenViewModel
) {
    val goldCoinValue = remember { mutableIntStateOf(0) }
    val isBagSelected = remember { mutableStateOf(false) }
    var iconSelector by remember { mutableStateOf<Int?>(null) }
    
    Dialog(
        onDismissRequest = { isGoldCoinDialogOpen.value = false },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            decorFitsSystemWindows = false,
        )
    ) {
       Box(contentAlignment = Alignment.Center) {
           Column(
               modifier = Modifier
                   .fillMaxWidth()
                   .background(Color.White)
                   .padding(ScreenConstants.AVERAGE_PADDING.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
           ) {
               Text(
                   text = stringResource(id = R.string.gold_coins_dialog_title),
                   style = MaterialTheme.typography.headlineLarge,
                   textAlign = TextAlign.Center,
                   )
                Spacer(modifier = Modifier.height(ScreenConstants.AVERAGE_PADDING.dp))
               Text(
                   text = stringResource(id = R.string.gold_coins_dialog_body),
                   style = MaterialTheme.typography.bodyMedium,
               )
                Spacer(modifier = Modifier.height(ScreenConstants.AVERAGE_PADDING.dp))
               Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(ScreenConstants.AVERAGE_PADDING.dp),
                   horizontalArrangement = Arrangement.SpaceBetween,
               ) {
                   Column(
                          horizontalAlignment = Alignment.CenterHorizontally,
                          verticalArrangement = Arrangement.Center
                   ) {
                       BagIcon(
                           resourceId = R.drawable.small_gold_bag,
                           contentDescriptor = "Small gold bag icon",
                           isSelected = iconSelector == BagValues.SMALL_BAG
                       ) {
                           iconSelector = BagValues.SMALL_BAG
                           handleBagSelection(
                               bagValue = BagValues.SMALL_BAG,
                               goldCoinValue = goldCoinValue,
                               isBagSelected = isBagSelected
                           )
                       }
                       Text(
                           text = stringResource(id = R.string.gold_coins_dialog_small_bag_text),
                           style = MaterialTheme.typography.labelSmall,
                       )
                   }

                   Column(
                          horizontalAlignment = Alignment.CenterHorizontally,
                          verticalArrangement = Arrangement.Center
                   ) {
                       BagIcon(
                           resourceId = R.drawable.medium_gold_bag,
                           contentDescriptor = "Medium gold bag icon",
                           isSelected = iconSelector == BagValues.MEDIUM_BAG
                       ) {
                           iconSelector = BagValues.MEDIUM_BAG
                           handleBagSelection(
                               bagValue = BagValues.MEDIUM_BAG,
                               goldCoinValue = goldCoinValue,
                               isBagSelected = isBagSelected
                           )
                       }
                       Text(
                           text = stringResource(id = R.string.gold_coins_dialog_medium_bag_text),
                           style = MaterialTheme.typography.labelSmall,
                       )
                   }

                   Column(
                       horizontalAlignment = Alignment.CenterHorizontally,
                       verticalArrangement = Arrangement.Center
                   ) {
                       BagIcon(
                           resourceId = R.drawable.great_gold_bag,
                           contentDescriptor = "Great gold bag icon",
                           isSelected = iconSelector == BagValues.GREAT_BAG
                       ) {
                           iconSelector = BagValues.GREAT_BAG
                           handleBagSelection(
                               bagValue = BagValues.GREAT_BAG,
                               goldCoinValue = goldCoinValue,
                               isBagSelected = isBagSelected
                           )
                       }
                       Text(
                           text = stringResource(id = R.string.gold_coins_dialog_great_bag_text),
                            style = MaterialTheme.typography.labelSmall,
                       )
                   }
               }

               Spacer(modifier = Modifier.height(ScreenConstants.AVERAGE_PADDING.dp))

               Row(
                   modifier = Modifier
                       .fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceBetween
               ) {
                   Button(onClick = {
                       Toast.makeText(
                           context,
                           context.getString(R.string.gold_coins_dialog_cancel_toast_message),
                           Toast.LENGTH_SHORT)
                           .show()
                       isGoldCoinDialogOpen.value = false
                   }) {
                       Text(text = stringResource(id = R.string.gold_coins_dialog_cancel_button).uppercase())
                   }
                   Button(
                       onClick = {
                          Toast.makeText(
                            context,
                            context.getString(R.string.gold_coins_dialog_ok_toast_message, goldCoinValue.intValue),
                            Toast.LENGTH_SHORT)
                            .show()

                          controller.startMultiplayerGame(
                               gameMode = GameMode.MULTI_PLAYER.toString(),
                               goldCoins = goldCoinValue.intValue
                          )

                          isGoldCoinDialogOpen.value = false
                       },
                       enabled = isBagSelected.value
                   ) {
                       Text(text = stringResource(id = R.string.gold_coins_dialog_ok_button).uppercase())
                   }
               }
           }
       }
    }
}

@Composable
private fun BagIcon(
    resourceId: Int,
    contentDescriptor: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    IconButton(onClick = onSelect) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = contentDescriptor,
            modifier = Modifier
                .size(ScreenConstants.BAG_SIZE.dp)
                .border(if (isSelected) 2.dp else 0.dp, Color.Red)
        )
    }
}

private fun handleBagSelection(
    bagValue: Int,
    goldCoinValue: MutableIntState,
    isBagSelected: MutableState<Boolean>
) {
    goldCoinValue.intValue = bagValue
    isBagSelected.value = true
}

private object BagValues {
    const val SMALL_BAG = 1
    const val MEDIUM_BAG = 2
    const val GREAT_BAG = 3
}