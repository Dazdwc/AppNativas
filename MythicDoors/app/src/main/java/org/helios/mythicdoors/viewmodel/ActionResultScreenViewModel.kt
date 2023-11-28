package org.helios.mythicdoors.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.internal.Contexts.getApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.Enemy
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.calendar.CalendarService
import org.helios.mythicdoors.utils.extenssions.hasPostNotificationPermission
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
            sendOnWinNotification()
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun makeScreenshot(
        view: View,
        activity: MainActivity
    ) {
        try {
            viewModelScope.launch {
                ScreenshotService.build(view, activity).takeScreenshot()
                    .also { if (it) sendOnScreenshotNotification() }
            }
        } catch (e: Exception) {
            Log.e("GameActionScreenViewModel", "makeScreenshot: $e")
        }
    }

    /* We can suppress the Missing Permission Check because we have externalized the logic in a util */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    private fun sendOnWinNotification() {
        val context = store.getContext() ?: MainActivity.getContext()
        if (!context.hasPostNotificationPermission()) throw Exception("Post notification permission not granted")

        val notification = createOnWinNotification()
        NotificationManagerCompat.from(context).notify(AppConstants.NotificationIds.GAMEWON_NOTIFICATION_ID, notification.build())
    }

    private fun createOnWinNotification(): NotificationCompat.Builder {
        val context = store.getContext() ?: MainActivity.getContext()

        return NotificationCompat.Builder(context, AppConstants.NotificationChannels.GAMEWON_NOTIFICATION_CHANNEL)
            .setContentTitle(context.getString(R.string.win_notification_title))
            .setContentText(context.getString(R.string.win_notification_content))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setOngoing(true)
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sendOnScreenshotNotification() {
        val context = store.getContext() ?: MainActivity.getContext()
        if (!context.hasPostNotificationPermission()) throw Exception("Post notification permission not granted")

        val notification = createOnScreenshotNotification()
        NotificationManagerCompat.from(context).notify(AppConstants.NotificationIds.IMAGES_NOTIFICATION_ID, notification.build())
    }

    private fun createOnScreenshotNotification(): NotificationCompat.Builder {
        val context = store.getContext() ?: MainActivity.getContext()

        return NotificationCompat.Builder(context, AppConstants.NotificationChannels.IMAGES_NOTIFICATION_CHANNEL)
            .setContentTitle(context.getString(R.string.screenshot_notification_title))
            .setContentText(context.getString(R.string.screenshot_notification_content))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setOngoing(true)
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