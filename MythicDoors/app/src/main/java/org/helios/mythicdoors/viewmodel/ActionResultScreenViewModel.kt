package org.helios.mythicdoors.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.goToActionScreen
import org.helios.mythicdoors.navigation.goToOptsScreen

class ActionResultScreenViewModel(
    private val dataController: DataController
    ): ViewModel() {

    fun navigateToOptsScreen(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        goToOptsScreen(navController, scope, snackbarHostState)
    }

    fun navigateToGameActionScreen(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        goToActionScreen(navController, scope, snackbarHostState)
    }

    }

