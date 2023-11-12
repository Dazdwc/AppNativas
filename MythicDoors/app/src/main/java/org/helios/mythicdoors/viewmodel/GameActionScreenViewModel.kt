package org.helios.mythicdoors.viewmodel

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.viewmodel.gamelogic.GameLogicViewModel

class GameActionScreenViewModel (
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
    private val gameController: GameLogicViewModel by lazy { GameLogicViewModel(dataController) }

    private val player by lazy { store.getAppStore().actualUser }

    val combatSuccessful: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    fun resetCombatSuccessful() { combatSuccessful.value = false }

    fun getPlayerLevel(): Int {
        return try {
            player?.getLevel() ?: 1
        } catch (e: NumberFormatException) {
            Log.e("GameActionScreenViewModel", "getPlayerLevel: $e")
            1
        }
    }

    fun getPlayerCoins(): Int {
        return try {
            player?.getCoins() ?: 0
        } catch (e: NumberFormatException) {
            Log.e("GameActionScreenViewModel", "getPlayerCoins: $e")
            0
        }
    }

    fun validateBet(bet: String): Boolean {
        return try {
            bet.toInt() in 1..(player?.getCoins() ?: 1)
        } catch (e: NumberFormatException) {
            Log.e("GameActionScreenViewModel", "validateBet: $e")
            false
        }
    }

    fun updateValuesOnPlayerAction(bet: String, selectedDoorId: String, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            store.updatePlayerAction(bet.toInt(), selectedDoorId).run { resolveBetAction(scope, snackbarHostState) }
        } catch (e: Exception) {
            Log.e("GameActionScreenViewModel", "updateOnPlayerActionValues: $e")
        }
    }

    fun navigateToActionResultScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navFunctions.navigateActionResultScreen(scope, snackbarHostState)
        } catch (e: Exception) {
            Log.e("GameActionScreenViewModel", "navigateToActionResultScreen: $e")
        }
    }

    private fun resolveBetAction(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            gameController.battle().takeIf { it }?.let {
                combatSuccessful.value = true
                Log.w("GameActionScreenViewModel", "resolveBetAction: Resolving bet action.")
            }
            Log.e("Store", "Store: ${store.getAppStore().combatResults}")
        } catch (e: Exception) {
            Log.e("GameActionScreenViewModel", "resolveGameAction: $e")
        }
    }
}