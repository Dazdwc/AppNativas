package org.helios.mythicdoors.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.navigateSingleTopTo

class OverviewScreenViewModel(
    private val dataController: DataController,
) {
   /*fun navigateToLoginScreen(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navController.navigateSingleTopTo("login_screen")
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Login Screen")
            }
        }
    }*/
}