package org.helios.mythicdoors.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.presentation.sign_in.FirebaseBaseAuthClient
import org.helios.mythicdoors.presentation.sign_in.GoogleAuthUiClient
import org.helios.mythicdoors.presentation.sign_in.IFirebaseBaseAuthClient
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
    private val context by lazy { store.getContext()}
    private val googleAuthClient: GoogleAuthUiClient by lazy { GoogleAuthUiClient(
        context = context ?: throw Exception("Error getting context"),
        oneTapClient = Identity.getSignInClient(context ?: throw Exception("Error getting context"))
    ) }
    private val firebaseAuth: IFirebaseBaseAuthClient by lazy { FirebaseBaseAuthClient.getInstance(FirebaseAuth.getInstance()) }

    val isGameModeSelected: MutableLiveData<Boolean> = MutableLiveData(false)

    fun resetIsGameStarted() { isGameModeSelected.value = false }

    fun startSinglePlayerGame(gameMode: String) {
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

    fun startMultiplayerGame(
        gameMode: String,
        goldCoins: Int? = null,
    ) {
        val startingCoins: Long = goldCoins?.times(AppConstants.INITIAL_COINS_AMOUNT)
            ?: store.getAppStore().actualUser?.getCoins() ?: AppConstants.INITIAL_COINS_AMOUNT

        viewModelScope.launch {
            try {
                updateGameModeInStore(gameMode)
                loadMultiPlayerInitialCoins(startingCoins)
                store.getAppStore().actualUser?.let { saveOriginalPlayerStats(it) }
                clearCombatResultsInStore()
                isGameModeSelected.value = true
            } catch (e: Exception) {
                Log.e("GameOptsScreenViewModel", "Error starting game: ${e.message}").also { isGameModeSelected.value = false }
            }
        }
    }

    fun checkIfUserHaveCoinsToPlay(): Boolean { return store.getAppStore().actualUser?.getCoins()?.let { it >= AppConstants.MINIMUN_COINS_AMMOUNT } ?: false }

    private fun updateGameModeInStore(gameMode: String) { store.updateGameMode(gameMode) }

    private fun saveOriginalPlayerStats(user: User?) { store.updateOriginalPlayerStats(user) }

    private fun loadInitialCoins() {
        store.getAppStore().actualUser?.let {
            store.updatePlayerCoins(AppConstants.INITIAL_COINS_AMOUNT)
        }
    }

    private fun loadMultiPlayerInitialCoins(startingCoins: Long) {
        store.getAppStore().actualUser?.let {
            store.updatePlayerCoins(startingCoins)
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
            viewModelScope.launch {
                googleAuthClient.signOut()
                firebaseAuth.signOut()
            }
            store.logout().also { navigateToLoginScreen(scope, snackbarHostState) }
        } catch (e: Exception) {
            Log.e("GameOptsScreenViewModel", "Error logging out: ${e.message}")
            Toast.makeText(context, "Error logging out: ${e.message}", Toast.LENGTH_LONG).show()
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