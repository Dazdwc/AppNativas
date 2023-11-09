package org.helios.mythicdoors.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.goToScoreScreen
import org.helios.mythicdoors.navigation.goToActionScreen

class GameOptsScreenViewModel(
    private val dataController: DataController
): ViewModel() {

    fun navigateToGameScoresScreens(navController: NavController,
                                 scope: CoroutineScope,
                                 snackbarHostState: SnackbarHostState
    ) {
        try {
            goToScoreScreen(navController, scope,snackbarHostState)
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
            goToActionScreen(navController,scope,snackbarHostState)
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Action Screen")
            }
        }
    }
}

