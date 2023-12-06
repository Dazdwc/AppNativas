package org.helios.mythicdoors.ui.screens

import android.content.Context
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.ui.theme.DarkBlue
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.AppConstants.AVERAGE_DOOR
import org.helios.mythicdoors.utils.AppConstants.EASY_DOOR
import org.helios.mythicdoors.utils.AppConstants.HARD_DOOR
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.GAME_ACTION_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.GameActionScreenViewModel
import org.helios.mythicdoors.viewmodel.tools.SoundManagementViewModel

@RequiresApi(TIRAMISU)
@Composable
fun GameActionScreen(navController: NavController) {
    val controller: GameActionScreenViewModel = (MainActivity.viewModelsMap[GAME_ACTION_SCREEN_VIEWMODEL] as GameActionScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context: Context = LocalContext.current

    val soundManager: SoundManagementViewModel = (MainActivity.viewModelsMap[AppConstants.ScreensViewModels.SOUND_MANAGEMENT_SCREEN_VIEWMODEL] as SoundManagementViewModel)
        .apply { loadSoundsIfNeeded() }

    var isDoorSelected: Boolean by remember { mutableStateOf(false) }
    var selectedDoorId: String by remember { mutableStateOf("") }

    var playerBet: String by remember { mutableStateOf("") }
    var isBetValid: Boolean by remember { mutableStateOf(false) }

    val isCombatSuccessful by controller.combatSuccessful.observeAsState(false)
    LaunchedEffect(isCombatSuccessful, snackbarHostState) {
        if (isCombatSuccessful) {
            snackbarHostState.currentSnackbarData?.dismiss().run { controller.navigateToActionResultScreen(scope, snackbarHostState) }
            controller.resetCombatSuccessful()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            soundManager.stopPlayingSounds()
        }
    }

    controller.initialLoad()
    soundManager.playSound(R.raw.wolf)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fondogameaction) )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background) {
        LottieAnimation(composition = composition, progress = {progress})
      BoxWithConstraints {
          controller.loadPlayerData()
          val maxWidth = constraints.maxWidth

          Column {
              Text(
                  text = stringResource(id = R.string.app_name),
                  style = MaterialTheme.typography.headlineLarge,
                  color = MaterialTheme.colorScheme.onBackground,
                  modifier = Modifier
                      .padding(top = ScreenConstants.DOUBLE_PADDING.dp, bottom = ScreenConstants.DOUBLE_PADDING.dp)
                      .fillMaxWidth()
                      .wrapContentWidth(Alignment.CenterHorizontally),
              )
              Column(modifier = Modifier
                  .fillMaxWidth()
                  .padding(start = ScreenConstants.AVERAGE_PADDING.dp, end = ScreenConstants.AVERAGE_PADDING.dp),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.Center
              ) {
                  Box(
                      modifier = Modifier
                          .wrapContentSize(Alignment.Center)
                          .background(MaterialTheme.colorScheme.primary)
                          .border(1.dp, MaterialTheme.colorScheme.tertiary),
                      contentAlignment = Alignment.Center
                  ) {
                      Text(
                          text = "${stringResource(id = R.string.current_player_level)} ${controller.getPlayerLevel()}",
                          style = MaterialTheme.typography.titleSmall,
                          color = MaterialTheme.colorScheme.onBackground,
                          modifier = Modifier.padding(ScreenConstants.AVERAGE_PADDING.dp),
                      )
                  }
                  Row(modifier = Modifier
                      .fillMaxWidth()
                      .padding(top = ScreenConstants.DOUBLE_PADDING.dp, bottom = ScreenConstants.AVERAGE_PADDING.dp),
                      horizontalArrangement = Arrangement.SpaceBetween,
                  ) {
                      Image(painterResource(id = R.drawable.easy_door),
                          contentDescription = "Easy door image",
                          modifier = Modifier
                              .height(ScreenConstants.IMAGE_HEIGHT.dp)
                              .clickable {
                                  soundManager
                                      .stopPlayingSounds()
                                      .also { soundManager.playSound(R.raw.door_select) }

                                  (selectedDoorId != EASY_DOOR).also {
                                      selectedDoorId = EASY_DOOR
                                      isDoorSelected = it
                                  }
                              }
                              .border(
                                  setSelectedDoorBorder(selectedDoorId, EASY_DOOR).dp,
                                  MaterialTheme.colorScheme.tertiary
                              )
                      )
                      Image(painterResource(id = R.drawable.average_door),
                          contentDescription = "Medium door image",
                          modifier = Modifier
                              .height(ScreenConstants.IMAGE_HEIGHT.dp)
                              .clickable {
                                  soundManager
                                      .stopPlayingSounds()
                                      .also { soundManager.playSound(R.raw.door_select) }

                                  (selectedDoorId != AVERAGE_DOOR).also {
                                      selectedDoorId = AVERAGE_DOOR
                                      isDoorSelected = it
                                  }
                              }
                              .border(
                                  setSelectedDoorBorder(selectedDoorId, AVERAGE_DOOR).dp,
                                  MaterialTheme.colorScheme.tertiary
                              )
                      )
                      Image(painterResource(id = R.drawable.hard_door),
                          contentDescription = "Medium door image",
                          modifier = Modifier
                              .height(ScreenConstants.IMAGE_HEIGHT.dp)
                              .clickable {
                                  soundManager
                                      .stopPlayingSounds()
                                      .also { soundManager.playSound(R.raw.door_select) }

                                  (selectedDoorId != HARD_DOOR).also {
                                      selectedDoorId = HARD_DOOR
                                      isDoorSelected = it
                                  }
                              }
                              .border(
                                  setSelectedDoorBorder(selectedDoorId, HARD_DOOR).dp,
                                  MaterialTheme.colorScheme.tertiary
                              )
                      )
                  }
                  Text(
                      text = stringResource(id = R.string.playing_main_helper) ,
                      style = MaterialTheme.typography.titleSmall,
                      color = MaterialTheme.colorScheme.onBackground,
                      modifier = Modifier
                          .padding(
                              top = ScreenConstants.AVERAGE_PADDING.dp,
                              bottom = ScreenConstants.DOUBLE_PADDING.dp
                          )
                          .wrapContentWidth(Alignment.CenterHorizontally),
                  )
                  Column(modifier = Modifier
                      .width((maxWidth.minus(ScreenConstants.BET_BLOCK_WIDTH_REDUCER)).dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                  ) {
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
                              value = controller.getPlayerCoins().toString(),
                              onValueChange = { controller.getPlayerCoins() },
                              label = { Text(text = stringResource(id = R.string.player_coins)) },
                              readOnly = true,
                          )
                      }
                      Row(Modifier.padding(bottom = 15.dp)) {
                          Icon(modifier = Modifier
                              .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
                              .size(40.dp, 40.dp),
                              imageVector = ImageVector.vectorResource(R.drawable.bet_500),
                              contentDescription = "icon representing the user's actual coins",
                              tint = MaterialTheme.colorScheme.secondary
                          )
                          TextField(
                              modifier = Modifier
                                  .background(MaterialTheme.colorScheme.primary)
                                  .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                                  .weight(1f),
                              value = playerBet,
                              onValueChange = {
                                  playerBet = it
                                  isBetValid = controller.validateBet(it)
                              },
                              label = { Text(text = stringResource(id = R.string.bet)) },
                              isError = !isBetValid,
                              keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                          )
                      }
                      Button(
                            onClick = {
                            soundManager.stopPlayingSounds().also { soundManager.playSound(R.raw.door_open) }
                            controller.updateValuesOnPlayerAction(playerBet, selectedDoorId)
                            scope.launch {
                               snackbarHostState.showSnackbar(context.getString(R.string.bet_message, playerBet, selectedDoorId),
                                   duration = SnackbarDuration.Short)
                           }
                      },
                          enabled = isBetValid && isDoorSelected,
                          elevation = ButtonDefaults.buttonElevation(2.dp),
                      ) {
                          Text(text = stringResource(id = R.string.open_door).uppercase(),
                              style = MaterialTheme.typography.labelMedium,
                              color = MaterialTheme.colorScheme.onBackground,
                          )
                      }
                      Spacer(modifier = Modifier.padding(top = ScreenConstants.AVERAGE_PADDING.dp))
                      isBetValid.takeIf { !it }?.let {
                            Text(
                                text = stringResource(id = R.string.bet_validator),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                            )
                      }
                      isDoorSelected.takeIf { !it }?.let {
                          Text(
                              text = stringResource(id = R.string.door_validator),
                              style = MaterialTheme.typography.labelSmall,
                              color = MaterialTheme.colorScheme.error,
                          )
                      }
                  }
              }
          }
      }
    }
}

private fun setSelectedDoorBorder(selectedDoorId: String, doorOption: String): Int { return if (selectedDoorId == doorOption) 3 else -1 }
