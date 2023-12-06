package org.helios.mythicdoors.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.services.interfaces.LanguageChangeListener
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.ui.fragments.AudioPlayer
import org.helios.mythicdoors.ui.fragments.MenuBar
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.utils.AppConstants.GameMode
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.GAME_OPTS_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.lenguage
import org.helios.mythicdoors.viewmodel.GameOptsScreenViewModel

@Composable
fun GameOptsScreen(navController: NavController) {
    val controller: GameOptsScreenViewModel = (MainActivity.viewModelsMap[GAME_OPTS_SCREEN_VIEWMODEL] as GameOptsScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    val isGameStarted by controller.isGameModeSelected.observeAsState(false)
    LaunchedEffect(isGameStarted, snackbarHostState) {
        if (isGameStarted) {
            snackbarHostState.currentSnackbarData?.dismiss().run { controller.navigateToGameActionScreen(scope, snackbarHostState) }
            controller.resetIsGameStarted()
        }
    }
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
        color = MaterialTheme.colorScheme.background
    ) {
        Column{
            Text(
                text = "Mythic Doors",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(top = ScreenConstants.DOUBLE_PADDING.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
            )
            Text(text = lenguage["gameoptions_$currentLanguage"]?:"Game Options",
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
                        Button(onClick = { controller.startGame(GameMode.SINGLE_PLAYER.toString()) },
                            modifier = Modifier
                                .padding(
                                    top = ScreenConstants.DOUBLE_PADDING.dp,
                                    bottom = ScreenConstants.DOUBLE_PADDING.dp
                                )
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            elevation = ButtonDefaults.buttonElevation(2.dp),
                        ) {
                            Text(text = lenguage["singleplayer_$currentLanguage"]?:"SINGLE PLAYER",
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
                            enabled = false,
                            onClick = { controller.startGame(GameMode.MULTI_PLAYER.toString()) },
                        ) {
                            Text(text = lenguage["multiplayer_$currentLanguage"]?:"MULTIPLAYER",
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
                            Text(text = lenguage["score_$currentLanguage"]?:"SCORES LADDER",
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
                            Text(text = lenguage["logout_$currentLanguage"]?:"LOGOUT",
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

@Preview(showBackground = true)
@Composable
fun GameOptsScreenPreview() {
    GameActionScreen(navController = NavController(LocalContext.current))
}