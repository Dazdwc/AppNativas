package org.helios.mythicdoors.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.goToResultScreen

class GameActionScreenViewModel (
    private val dataController: DataController
): ViewModel() {

    fun navigateToActionsResult(navController: NavController,
                                            scope: CoroutineScope,
                                            snackbarHostState: SnackbarHostState
) {
        goToResultScreen(navController, scope,snackbarHostState)
} }

