package org.helios.mythicdoors.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.ui.fragments.AudioPlayer
import org.helios.mythicdoors.ui.fragments.MenuBar
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.ACTION_RESULT_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.ActionResultScreenViewModel
import org.helios.mythicdoors.viewmodel.GameResults
import org.helios.mythicdoors.viewmodel.tools.SoundManagementViewModel

@Composable
fun ActionResultScreen(navController: NavController) {
    val controller: ActionResultScreenViewModel = (MainActivity.viewModelsMap[ACTION_RESULT_SCREEN_VIEWMODEL] as ActionResultScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context: Context = LocalContext.current

    val soundManager = remember { SoundManagementViewModel(context) }.apply { loadSoundsIfNeeded(context) }

    val playerCurrentStats: User by lazy { controller.playerData ?: User.createEmptyUser() }
    val gameCurrentStats: GameResults by lazy { controller.gameResultsData ?: GameResults.create(false, null, 0, 0) }

    val isEnoughCoins: Boolean by lazy { controller.isEnoughCoins() }

    controller.initialLoad()
    soundManager.playSound(R.raw.werewolf)

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background) {
        BoxWithConstraints {

            val maxWidth = constraints.maxWidth

            Column {
                Text(
                    text = "Mythic Doors",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(
                            top = ScreenConstants.DOUBLE_PADDING.dp,
                            bottom = ScreenConstants.DOUBLE_PADDING.dp
                        )
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
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
                                    text = "You won!".uppercase(),
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
                                Text(text = "You lost!".uppercase(),
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
                        TextField(modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                            .weight(1f),
                            value = playerCurrentStats.getCoins().coerceAtLeast(0).toString(),
                            onValueChange = { playerCurrentStats.getCoins().toString() },
                            label = { Text(text = "Current Coins") },
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
                        TextField(modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                            .weight(1f),
                            value = playerCurrentStats.getLevel().toString(),
                            onValueChange = {playerCurrentStats.getLevel().toString() },
                            label = { Text(text = "Current Level") },
                            readOnly = true,
                        )
                    }
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
                            Text(text = "CONTINUE",
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
                            Text(text = "EXIT",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(top = ScreenConstants.AVERAGE_PADDING.dp))
                    isEnoughCoins.takeIf { !it }?.let {
                        Text(text = "You don't have enough coins to bet",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }
        }
    }
}