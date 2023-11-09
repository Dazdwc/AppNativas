package org.helios.mythicdoors.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.viewmodel.GameOptsScreenViewModel


@Composable
fun GameOptsScreen(navController: NavController) {
    val controller: GameOptsScreenViewModel = MainActivity.viewModelsMap["game-opts-screen-viewmodel"] as GameOptsScreenViewModel
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.DarkGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.hard_door),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .padding(20.dp)

            )
            Box(
            ) {
                Text(text = "Mythicdoors",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(onClick = {
                controller.navigateToGameActionScreen(navController, scope, snackbarHostState)
            }) {
                Text(text = "Play")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                controller.navigateToGameScoresScreen(navController, scope, snackbarHostState)
            }) {
                Text(text = "Ladderboard")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                //Falta logica Loggout
               // if(!login)
                controller.navigateToLoginScreen(navController, scope, snackbarHostState)

            }) {
                Text(text = "Loggout")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview(showSystemUi = true)
@Preview(showBackground = true)
@Composable
fun GameOptsScreenPreview() {
    GameOptsScreen(navController = NavController(LocalContext.current))
}