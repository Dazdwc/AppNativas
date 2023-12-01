package org.helios.mythicdoors.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.ui.fragments.AudioPlayer
import org.helios.mythicdoors.ui.fragments.MenuBar
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.utils.AppConstants.GameMode
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.GAME_OPTS_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.GameOptsScreenViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                            enabled = false,
                            onClick = { controller.startGame(GameMode.MULTI_PLAYER.toString()) },
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
