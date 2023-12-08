package org.helios.mythicdoors.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Picture
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.Enemy
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.AppConstants.NotificationChannels
import org.helios.mythicdoors.utils.notifications.NotificationFabric
import org.helios.mythicdoors.utils.screenshot.ScreenshotService

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
    var playerData: User? = null
    var gameResultsData: GameResults? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun initialLoad() {
        playerData = loadPlayerData()
        gameResultsData = loadGameResults()

        if (gameResultsData?.getIsPlayerWinner() == true) {
            try{
                NotificationFabric.create(NotificationChannels.GAMEWON_NOTIFICATION_CHANNEL)
                    .also { notification ->
                        NotificationFabric.send(notification)
                    }
            } catch (e: Exception) {
                Log.e("GameActionScreenViewModel", "Notification sender: $e")
            }
        }
    }

    fun isEnoughCoins(): Boolean {
        return try {
            (playerData?.getCoins()?.coerceAtLeast(0) ?: 0) > 0
        } catch (e: NumberFormatException) {
            Log.e("GameActionScreenViewModel", "getPlayerCoins: $e")
            false
        }
    }

    private fun loadGameResults(): GameResults {
        return GameResults.create(
            store.getAppStore().combatResults.isPlayerWinner,
            store.getAppStore().combatResults.enemy,
            store.getAppStore().combatResults.resultCoinAmount,
            store.getAppStore().combatResults.resultXpAmount
        )
    }

    private fun loadPlayerData(): User? {
        return try {
            store.getAppStore().actualUser ?: throw Exception("User not found")
        } catch (e: Exception) {
            Log.e("GameActionScreenViewModel", "loadPlayerData: $e")
            null
        }
    }

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
                clearCombatData()
                navFunctions.navigateGameActionScreen(scope, snackbarHostState)
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
                null,
                store.getAppStore().actualUser ?: throw Exception("User not found"),
                lastGameResults.resultCoinAmount.minus(originalPlayerStats.coins).coerceAtLeast(0),
                playerData?.getLevel() ?: 1,
                store.getAppStore().gameScore.minus(originalPlayerStats.score),
                lastGameResults.resultXpAmount.minus(originalPlayerStats.experience).coerceAtLeast(0),
            )

            scope.launch {
                saveFlag = dataController.saveGame(actualGame)
            }.join()
            playerData.let {
                playerData?.setCoins(AppConstants.INITIAL_COINS_AMOUNT)
                scope.launch {
                    saveFlag = dataController.saveUser(it ?: throw Exception("User not found"))
                }.join()
            }
            return saveFlag
        } catch (e: Exception) {
            Log.e("GameActionScreenViewModel", "saveGameResults: $e")
            false
        }
    }

    private fun clearCombatData() {
        try {
            store.clearCombatData()
        } catch (e: Exception) {
            Log.e("GameActionScreenViewModel", "clearCombatData: $e")
        }
    }

    fun createImageFile(
        context: Context,
        activity: MainActivity,
        bitmap: Bitmap
    ) {
        try {
            viewModelScope.launch {
                ScreenshotService.makeScreenshotFile(
                    context = context,
                    activity = activity,
                    bitmap = bitmap
                )
            }
        } catch (e: Exception) {
            Log.e("GameActionScreenViewModel", "createImageFile: $e")
        }
    }
}

/*
* Usamos un objeto anónimo porque no lo vamos a necesitar en ningún otro sitio. Solamente lo usan la Screen y el ViewModel para la pantalla de resultados que están intrínsicamente
* relacionados.
*/
object GameResults {
    private var isPlayerWinner: Boolean = false
    private var enemy: Enemy? = null
    private var resultCoinAmount: Int = 0
    private var resultXpAmount: Int = 0

    fun getIsPlayerWinner(): Boolean { return isPlayerWinner }

    fun getEnemy(): Enemy? { return enemy }

    fun getResultCoinAmount(): Int { return resultCoinAmount }

    fun getResultXpAmount(): Int { return resultXpAmount }


    fun create(isPlayerWinner: Boolean, enemy: Enemy?, resultCoinAmount: Int, resultXpAmount: Int): GameResults {
        return GameResults.apply {
            this.isPlayerWinner = isPlayerWinner
            this.enemy = enemy
            this.resultCoinAmount = resultCoinAmount
            this.resultXpAmount = resultXpAmount
        }
    }
}