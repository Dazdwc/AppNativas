package org.helios.mythicdoors.viewmodel

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.store.StoreManager

class ScoresScreenViewModel(
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
    private val actualPlayer: User by lazy { chargePlayer() }

    val actualUserGamesList: MutableLiveData<List<Game>> by lazy { MutableLiveData<List<Game>>() }
    val allUsersGameList: MutableLiveData<List<Game>> by lazy { MutableLiveData<List<Game>>() }

    val loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private fun chargePlayer(): User { return store.getAppStore().actualUser ?: throw Exception("User not found") }

    suspend fun loadPlayerGamesList() {
        loading.value = true
        try {
            viewModelScope.launch {
                actualUserGamesList.value = dataController.getAllFSGames()
                    ?.filter { it.getUser().getEmail() == actualPlayer.getEmail() }
                    ?.sortedByDescending { it.getScore() }
                    ?: listOf()
            }.join()
        } catch (e: Exception) {
            Log.e("ScoresScreenViewModel", "ladUserGamesList: $e")
            actualUserGamesList.value = listOf()
        } finally {
            loading.value = false
        }
    }

    suspend fun loadAllPlayersgameList() {
        loading.value = true
        try {
            viewModelScope.launch {
               allUsersGameList.value = getTenBestGameFromAllPlayers().also {topTen ->
                   if (!checkIfActualPlayerIsOnTenBestScoresList(topTen)) {
                       loadBestPlayerGame()?.let { topTen.plus(it) }
                   }
               }
            }.join()
        } catch (e: Exception) {
            Log.e("ScoresScreenViewModel", "loadAllPlayersgameList: $e")
            allUsersGameList.value = listOf()
        } finally {
            loading.value = false
        }
    }

    fun returnToGameOptionsScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        scope.launch {
            try {
                navFunctions.navigateGameOptsScreen(scope, snackbarHostState)
            } catch (e: Exception) {
                Log.e("ScoresScreenViewModel", "returnToGameOptionsScreen: $e")
            }
        }
    }


    private suspend fun loadBestPlayerGame(): Game? {
        return try {
            dataController.getAllFSGames()
                ?.filter { it.getUser().getEmail() != actualPlayer.getEmail() }?.maxByOrNull { it.getScore() }
        } catch (e: Exception) {
            Log.e("ScoresScreenViewModel", "loadBestPlayerGame: $e")
            null
        }
    }


    private suspend fun getTenBestGameFromAllPlayers(): List<Game> {
        return try {
            val allGames = dataController.getAllFSGames()?.sortedByDescending { it.getScore() } ?: listOf()
            if (allGames.size < 10) {
                return allGames
            }
            allGames.subList(0, 10)
        } catch (e: Exception) {
            Log.e("ScoresScreenViewModel", "getTenBestGameFromAllPlayers: $e")
            listOf()
        }
    }

    private fun checkIfActualPlayerIsOnTenBestScoresList(
        topTenList: List<Game>
    ): Boolean {
        return topTenList.any { it.getUser().getEmail() == actualPlayer.getEmail() }
    }
}