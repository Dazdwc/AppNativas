package org.helios.mythicdoors.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.store.StoreManager

class GameOptsScreenViewModel(
    private val dataController: DataController
): ViewModel() {
    private val navController: NavController
        get() { return _navController }
    private val navFunctions: INavFunctions by lazy { NavFunctionsImp.getInstance(navController) }

    private lateinit var _navController: NavController
    fun setNavController(navController: NavController) {
        _navController = navController
    }

    private val store: StoreManager by lazy { StoreManager.getInstance() }

    fun updateGameModeInStore(gameMode: String) {
        store.updateGameMode(gameMode)
    }

    fun navigateToGameActionScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        navFunctions.navigateGameActionScreen(scope, snackbarHostState)
    }
    fun navigateToLogin(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        navFunctions.navigateToLoginScreen(scope, snackbarHostState)
    }

    fun navigateToScores(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        navFunctions.navigateScoresScreen(scope, snackbarHostState)
    }
}