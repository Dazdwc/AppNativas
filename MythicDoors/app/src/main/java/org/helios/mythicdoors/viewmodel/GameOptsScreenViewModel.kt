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
        goToScoreScreen(navController, scope,snackbarHostState)
    }
    fun navigateToGameActionScreen(navController: NavController,
                                    scope: CoroutineScope,
                                    snackbarHostState: SnackbarHostState
    ) {
        goToActionScreen(navController,scope,snackbarHostState)
    }
}

