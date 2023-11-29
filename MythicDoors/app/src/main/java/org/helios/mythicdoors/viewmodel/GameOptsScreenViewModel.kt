package org.helios.mythicdoors.viewmodel

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.utils.AppConstants

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

    val isGameModeSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    fun resetIsGameStarted() { isGameModeSelected.value = false }

    fun startGame(gameMode: String) {
        try {
            updateGameModeInStore(gameMode)
            store.getAppStore().actualUser?.let { saveOriginalPlayerStats(it) }
            loadInitialCoins()
            clearCombatResultsInStore()
            isGameModeSelected.value = true
        } catch (e: Exception) {
            Log.e("GameOptsScreenViewModel", "Error starting game: ${e.message}").also { isGameModeSelected.value = false }
        }
    }

    private fun updateGameModeInStore(gameMode: String) { store.updateGameMode(gameMode) }

    private fun saveOriginalPlayerStats(user: User?) { store.updateOriginalPlayerStats(user) }

    private fun loadInitialCoins() {
        store.getAppStore().actualUser?.let {
            store.updatePlayerCoins(AppConstants.INITIAL_COINS_AMOUNT)
        }
    }

    private fun clearCombatResultsInStore() { store.clearCombatResults() }

    fun navigateToGameActionScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navFunctions.navigateGameActionScreen(scope, snackbarHostState)
        } catch (e: Exception) {
            Log.e("GameOptsScreenViewModel", "Error navigating to GameActionScreen: ${e.message}")
        }
    }

    fun navigateToScoresScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navFunctions.navigateScoresScreen(scope, snackbarHostState)
        } catch (e: Exception) {
            Log.e("GameOptsScreenViewModel", "Error navigating to ScoresScreen: ${e.message}")
        }
    }

    fun logout(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            store.logout().also { navigateToLoginScreen(scope, snackbarHostState) }
        } catch (e: Exception) {
            Log.e("GameOptsScreenViewModel", "Error logging out: ${e.message}")
            scope.launch { snackbarHostState.showSnackbar("Error logging out: ${e.message}") }
        }
    }

    fun navigateToGameGuideWebView(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navFunctions.navigateGameGuideWebViewScreen(scope, snackbarHostState)
        } catch (e: Exception) {
            Log.e("GameOptsScreenViewModel", "Error navigating to GameGuideWebView: ${e.message}")
        }
    }

    private fun navigateToLoginScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navFunctions.navigateToLoginScreen(scope, snackbarHostState)
        } catch (e: Exception) {
            Log.e("GameOptsScreenViewModel", "Error navigating to LoginScreen: ${e.message}")
        }
    }
}