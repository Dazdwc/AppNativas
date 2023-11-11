package org.helios.mythicdoors.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.viewmodel.LoginScreenViewModel
import org.helios.mythicdoors.viewmodel.ScoresScreenViewModel

@Composable
fun ScoresScreen(navController: NavController) {
    val controller: ScoresScreenViewModel = (MainActivity.viewModelsMap[AppConstants.ScreensViewModels.SCORES_SCREEN_VIEWMODEL] as ScoresScreenViewModel).apply { setNavController(navController) }
    var games by remember { mutableStateOf(emptyList<Game>())}

    Scaffold() { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Button(onClick = {
                    controller.createSerGames()
                    games = controller.game.value.orEmpty()
                    Log.w("Games","$games")
                }) {
                    Text(text = "Actualizar Ladder")
                }
                Text(text = "User        ||        Score ||      Lvl Enemy ||      Coins")
                LazyColumn {
                    items(games) { game ->
                        Row(){

                            Text(text = " ${game.getUser().getName()}                 ${game.getScore()}                      ${game.getMaxEnemyLevel()}             ${game.getCoin()}")
                        }

                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScoresScreenPreview() {
    ScoresScreen(navController = NavController(LocalContext.current))
}