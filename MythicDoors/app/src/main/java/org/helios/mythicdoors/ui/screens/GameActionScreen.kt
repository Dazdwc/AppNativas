package org.helios.mythicdoors.ui.screens

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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.ui.fragments.MenuBar
import org.helios.mythicdoors.utils.AppConstants.AVERAGE_DOOR
import org.helios.mythicdoors.utils.AppConstants.EASY_DOOR
import org.helios.mythicdoors.utils.AppConstants.HARD_DOOR
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.GAME_ACTION_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.GameActionScreenViewModel

@Composable
fun GameActionScreen(navController: NavController) {
    val controller: GameActionScreenViewModel = (MainActivity.viewModelsMap[GAME_ACTION_SCREEN_VIEWMODEL] as GameActionScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

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

    controller.initialLoad()

    Scaffold(
        bottomBar = {
            MenuBar(navController)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            color = MaterialTheme.colorScheme.background) {
          BoxWithConstraints {
              controller.loadPlayerData()
              val maxWidth = constraints.maxWidth

              Column {
                  Text(
                      text = "Mythic Doors",
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
                              text = "Current Player Level: ${controller.getPlayerLevel()}",
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
                      Text(text = "Make a Bet And Choose a Door",
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
                                  contentDescription = "icon representing the user-s actual coins",
                                  tint = MaterialTheme.colorScheme.secondary
                              )
                              TextField(modifier = Modifier
                                  .background(MaterialTheme.colorScheme.primary)
                                  .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                                  .weight(1f),
                                  value = controller.getPlayerCoins().toString(),
                                  onValueChange = { controller.getPlayerCoins() },
                                  label = { Text(text = "Player Coins") },
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
                              TextField(modifier = Modifier
                                  .background(MaterialTheme.colorScheme.primary)
                                  .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                                  .weight(1f),
                                  value = playerBet,
                                  onValueChange = {
                                      playerBet = it
                                      isBetValid = controller.validateBet(it)
                                  },
                                  label = { Text(text = "Bet") },
                                  isError = !isBetValid,
                                  keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                              )
                          }
                          Button(onClick = {
                               controller.updateValuesOnPlayerAction(playerBet, selectedDoorId)
                                scope.launch {
                                   snackbarHostState.showSnackbar("You have bet $playerBet coins on the $selectedDoorId door")
                               }
                          },
                              enabled = isBetValid && isDoorSelected,
                              elevation = ButtonDefaults.buttonElevation(2.dp),
                          ) {
                              Text(text = "OPEN DOOR",
                                  style = MaterialTheme.typography.labelMedium,
                                  color = MaterialTheme.colorScheme.onBackground,
                              )
                          }
                          Spacer(modifier = Modifier.padding(top = ScreenConstants.AVERAGE_PADDING.dp))
                          isBetValid.takeIf { !it }?.let {
                                Text(text = "You can only bet up to your current coins",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error,
                                )
                          }
                          isDoorSelected.takeIf { !it }?.let {
                              Text(text = "You must select a door",
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameActionScreenPreview() {
    GameActionScreen(navController = NavController(LocalContext.current))
}

private fun setSelectedDoorBorder(selectedDoorId: String, doorOption: String): Int { return if (selectedDoorId == doorOption) 3 else -1 }