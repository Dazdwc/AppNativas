package org.helios.mythicdoors.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.GAME_ACTION_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.GameActionScreenViewModel

@Composable
fun GameActionScreen(navController: NavController) {
    val controller: GameActionScreenViewModel = (MainActivity.viewModelsMap[GAME_ACTION_SCREEN_VIEWMODEL] as GameActionScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isDoorSelected: Boolean by remember { mutableStateOf(false) }

    var playerLevel: Int by remember { mutableIntStateOf(1) }


    var playerCoins: Int by remember { mutableIntStateOf(0) }
    var playerBet: Int by remember { mutableIntStateOf(0) }
    var isBetValid: Boolean by remember { mutableStateOf(false) }



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
            color = MaterialTheme.colorScheme.background) {
          BoxWithConstraints {
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
                      Box(
                          modifier = Modifier
                              .width(200.dp)
                              .height(100.dp)
                              .background(MaterialTheme.colorScheme.background)
                              .border(1.dp, MaterialTheme.colorScheme.tertiary)
                              .padding(top = 30.dp, bottom = 30.dp),
                          contentAlignment = Alignment.Center
                      ) {
                      }
                      Text(
                          text = "Current Player Level: $playerLevel",
                          style = MaterialTheme.typography.headlineMedium,
                          color = MaterialTheme.colorScheme.onBackground,
                          modifier = Modifier
                              .padding(top = 30.dp, bottom = 30.dp)
                              .fillMaxWidth()
                              .wrapContentWidth(Alignment.CenterHorizontally),
                      )
                  }
                  Row {
                      Image(painterResource(id = R.drawable.easy_door),
                          contentDescription = "Easy door image",
                          Modifier.size(200.dp, 200.dp)
                      )
                      Image(painterResource(id = R.drawable.average_door),
                          contentDescription = "Medium door image",
                          modifier = Modifier
                              .size(200.dp, 200.dp)
                              .clickable { }
                      )
                      Image(painterResource(id = R.drawable.hard_door),
                          contentDescription = "Medium door image",
                          modifier = Modifier
                              .size(200.dp, 200.dp)
                              .clickable { }
                      )
                  }
                  Text(text = "Make a Bet And Choose a Door",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(top = 30.dp, bottom = 30.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally),
                  )
                  Row {
                    Icon(modifier = Modifier
                        .padding(end = 10.dp)
                        .size(40.dp, 40.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.actual_coins_500),
                        contentDescription = "icon representing the user-s actual coins",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    TextField(modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                        .weight(1f),
                        value = playerCoins.toString(),
                        onValueChange = { playerCoins = it.toInt() },
                        label = { Text(text = "Bet") },
                        readOnly = true,
                    )
                  }
                  Row {
                      Icon(modifier = Modifier
                          .padding(end = 10.dp)
                          .size(40.dp, 40.dp),
                          imageVector = ImageVector.vectorResource(R.drawable.bet_500),
                          contentDescription = "icon representing the user-s actual coins",
                          tint = MaterialTheme.colorScheme.secondary
                      )
                      TextField(modifier = Modifier
                          .background(MaterialTheme.colorScheme.primary)
                          .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                          .weight(1f),
                          value = playerBet.toString(),
                          onValueChange = {
                              playerBet = it.toInt()
                              isBetValid = playerBet <= playerCoins
                          },
                          label = { Text(text = "Bet") },
                          isError = isBetValid,
                      )
                  }
                  Button(onClick = { /*TODO*/ },
                      enabled = isBetValid && isDoorSelected,
                      ) {
                      Text(text = "OPEN DOOR",
                          style = MaterialTheme.typography.labelMedium,
                          color = MaterialTheme.colorScheme.onBackground,
                      )
                  }
              }
          }
        }
    }
}