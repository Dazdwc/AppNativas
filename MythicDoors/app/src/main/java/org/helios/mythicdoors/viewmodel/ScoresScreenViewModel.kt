package org.helios.mythicdoors.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.navigateSingleTopTo

class ScoresScreenViewModel(
    private val dataController: DataController
) {
    fun navigateToOptsScreen(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navController.navigateSingleTopTo("game_opts_screen")
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Options Screen")
            }
        }
    }
}