package org.helios.mythicdoors.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.goToLoginScreen
import org.helios.mythicdoors.navigation.goToOptsScreen

class OverviewScreenViewModel(
    private val dataController: DataController,
) {

    fun navigateToLoginScreen(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        goToLoginScreen(navController, scope, snackbarHostState)
        //goToOptsScreen(navController,scope,snackbarHostState)

    }
}