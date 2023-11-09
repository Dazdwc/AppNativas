package org.helios.mythicdoors.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController

class GameOptsScreenViewModel(
    private val dataController: DataController
): ViewModel() {

    fun navigateToGameScoresScreens(navController: NavController,
                                 scope: CoroutineScope,
                                 snackbarHostState: SnackbarHostState
    ) {
        try {
            navController.navigate("scores_screen")
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Game Score Screen")
            }
        }
    }
    fun navigateToGameActionScreen(navController: NavController,
                                    scope: CoroutineScope,
                                    snackbarHostState: SnackbarHostState
    ) {
        try {
            navController.navigate("game_action_screen")
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Action Screen")
            }
        }
    }
}

