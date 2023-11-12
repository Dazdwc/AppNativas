package org.helios.mythicdoors.viewmodel

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.EnemyDBoj
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.store.StoreManager

class ActionResultScreenViewModel(
    private val dataController: DataController
    ): ViewModel() {
    private val navController: NavController
        get() {
            return _navController
        }
    private val navFunctions: INavFunctions by lazy { NavFunctionsImp.getInstance(navController) }

    private lateinit var _navController: NavController
    fun setNavController(navController: NavController) {
        _navController = navController
    }

    private val store: StoreManager by lazy { StoreManager.getInstance() }
    private val playerData: User by lazy { chargePlayerData() }
    private val gameResultsData: GameResults by lazy { chargeGameResults() }

    fun loadPlayerData(): User { return playerData }

    fun loadGameResultsData(): GameResults { return gameResultsData }

    fun isEnoughCoins(): Boolean {
        return try {
            playerData.getCoins().coerceAtLeast(0) > 0
        } catch (e: NumberFormatException) {
            Log.e("GameActionScreenViewModel", "getPlayerCoins: $e")
            false
        }
    }

    private fun chargeGameResults(): GameResults {
        return GameResults.create(
            store.getAppStore().combatResults.isPlayerWinner,
            store.getAppStore().combatResults.enemy,
            store.getAppStore().combatResults.resultCoinAmount,
            store.getAppStore().combatResults.resultXpAmount
        )
    }

    private fun chargePlayerData(): User { return store.getAppStore().actualUser ?: throw Exception("User not found") }

    fun returnToGameOptionsScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            scope.launch {
                saveGameResults(scope).takeIf { it }
                    .run { navFunctions.navigateGameOptsScreen(scope, snackbarHostState) }
            }
            return
        } catch (e: Exception) {
            Log.e("GameActionScreenViewModel", "navigateToGameOptionsScreen: $e")
        }
        scope.launch {
            snackbarHostState.showSnackbar("Error: Impossible to save the game results!")
        }
        navFunctions.navigateToLoginScreen(scope, snackbarHostState)
    }

    fun returnToGameActionScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            scope.launch {
                saveGameResults(scope).takeIf { it }
                    .run { navFunctions.navigateGameActionScreen(scope, snackbarHostState) }
            }
            return
        } catch (e: Exception) {
            Log.e("GameActionScreenViewModel", "returnToGameActionScreen: $e")
        }
        scope.launch {
            snackbarHostState.showSnackbar("Error: Impossible to save the game results!")
        }
        navFunctions.navigateToLoginScreen(scope, snackbarHostState)
    }

    private suspend fun saveGameResults(scope: CoroutineScope): Boolean {
        return try {
            var saveFlag = false
            val originalPlayerStats = store.getAppStore().playerInitialStats
            val lastGameResults = store.getAppStore().combatResults

            val actualGame: Game = Game.create(
                store.getAppStore().actualUser ?: throw Exception("User not found"),
                lastGameResults.resultCoinAmount.minus(originalPlayerStats.coins).coerceAtLeast(0),
                playerData.getLevel(),
                playerData.getScore().minus(originalPlayerStats.score).coerceAtLeast(0),
                lastGameResults.resultXpAmount.minus(originalPlayerStats.experience).coerceAtLeast(0),
            )

            scope.launch { saveFlag = dataController.saveGame(actualGame) }.join()
            return saveFlag
        } catch (e: Exception) {
            Log.e("GameActionScreenViewModel", "saveGameResults: $e")
            false
        }
    }
}

/*
* Usamos un objeto anónimo porque no lo vamos a necesitar en ningún otro sitio. Solamente lo usan la Screen y el ViewModel para la pantalla de resultados que están intrínsicamente
* relacionados.
*/
object GameResults {
    private var isPlayerWinner: Boolean = false
    private var enemy: EnemyDBoj? = null
    private var resultCoinAmount: Int = 0
    private var resultXpAmount: Int = 0

    fun getIsPlayerWinner(): Boolean { return isPlayerWinner }

    fun getEnemy(): EnemyDBoj? { return enemy }

    fun getResultCoinAmount(): Int { return resultCoinAmount }

    fun getResultXpAmount(): Int { return resultXpAmount }


    fun create(isPlayerWinner: Boolean, enemy: EnemyDBoj?, resultCoinAmount: Int, resultXpAmount: Int): GameResults {
        return GameResults.apply {
            this.isPlayerWinner = isPlayerWinner
            this.enemy = enemy
            this.resultCoinAmount = resultCoinAmount
            this.resultXpAmount = resultXpAmount
        }
    }
}