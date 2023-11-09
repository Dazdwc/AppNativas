package org.helios.mythicdoors.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController

class ActionResultScreenViewModel(
    private val dataController: DataController
    ): ViewModel() {
    fun navigateToGameOptsScreen(navController: NavController,
                                 scope: CoroutineScope,
                                 snackbarHostState: SnackbarHostState
    ) {
        try {
            navController.navigate("game_opts_screen")
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Game Option")
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
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Game Action")
            }
        }
    }

    }

