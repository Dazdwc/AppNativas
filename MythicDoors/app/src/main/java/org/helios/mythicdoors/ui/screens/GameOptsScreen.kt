package org.helios.mythicdoors.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants.GameMode
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.GAME_OPTS_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.GameOptsScreenViewModel

@Composable
fun GameOptsScreen(navController: NavController) {
    val controller: GameOptsScreenViewModel = (MainActivity.viewModelsMap[GAME_OPTS_SCREEN_VIEWMODEL] as GameOptsScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            // TODO: Add top bar
        },
        bottomBar = {
            // TODO: Add bottom bar
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Text(
                    text = "Mythic Doors",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(top = 30.dp, bottom = 30.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                )
                Column {
                    Text(
                        text = "Game Options",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(top = 30.dp, bottom = 30.dp)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                    )
                    Row {
                        Column(
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 30.dp, end = 15.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painterResource(id = R.drawable.sirtrap),
                                contentDescription = "Single player image",
                                Modifier.size(150.dp, 150.dp)
                            )
                            Button(
                                onClick = {
                                    controller.updateGameModeInStore(GameMode.SINGLE_PLAYER.toString())
                                    controller.navigateToGameActionScreen(scope, snackbarHostState)
                                },
                                modifier = Modifier
                                    .padding(top = 30.dp, bottom = 30.dp)
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                                elevation = ButtonDefaults.buttonElevation(2.dp),
                            ) {
                                Text(
                                    text = "SINGLE PLAYER",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 30.dp, end = 15.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painterResource(id = R.drawable.multiplayer),
                                contentDescription = "Multiplayer image",
                                Modifier.size(150.dp, 150.dp)
                            )
                            Button(
                                modifier = Modifier
                                    .padding(top = 30.dp, bottom = 30.dp)
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                                elevation = ButtonDefaults.buttonElevation(2.dp),
                                enabled = false,
                                onClick = {
                                    controller.updateGameModeInStore(GameMode.MULTI_PLAYER.toString())
                                    controller.navigateToGameActionScreen(scope, snackbarHostState)
                                },
                            ) {
                                Text(
                                    text = "MULTIPLAYER",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                    }

                    Row() {
                         Column(
                             modifier = Modifier
                                 .padding(top = 20.dp, bottom = 30.dp, end = 15.dp)
                                 .weight(1f),
                             horizontalAlignment = Alignment.CenterHorizontally,
                             verticalArrangement = Arrangement.Center
                         ) {
                             Image(
                                painterResource(id = R.drawable.ladder),
                                contentDescription = "Ladder Image",
                                Modifier.size(150.dp, 150.dp)
                            )
                            Button(modifier = Modifier
                                .padding(top = 30.dp, bottom = 30.dp)
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                                elevation = ButtonDefaults.buttonElevation(2.dp),
                                enabled = true,
                                onClick = {
                                    //falta la opcion de desloguearse
                                    controller.navigateToScores(scope, snackbarHostState)
                                }) {
                                Text(
                                    text = "Ladder",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }

                         Column(
                             modifier = Modifier
                                 .padding(top = 20.dp, bottom = 30.dp, end = 15.dp)
                                 .weight(1f),
                             horizontalAlignment = Alignment.CenterHorizontally,
                             verticalArrangement = Arrangement.Center
                         ) {
                             Image(
                                painterResource(id = R.drawable.clapbrazos),
                                contentDescription = "Loggout Image",
                                Modifier.size(150.dp, 150.dp)
                         )
                            Button(modifier = Modifier
                                .padding(top = 30.dp, bottom = 30.dp)
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                                elevation = ButtonDefaults.buttonElevation(2.dp),
                                enabled = false,
                                onClick = {
                                //falta la opcion de desloguearse
                                controller.navigateToLogin(scope, snackbarHostState)
                            }) {
                            Text(
                                text = "Unlogin",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    } }
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