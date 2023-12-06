package org.helios.mythicdoors.viewmodel

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
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

    val userGamesList: MutableLiveData<List<Game>> by lazy { MutableLiveData<List<Game>>() }
    val loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private fun chargePlayer(): User { return store.getAppStore().actualUser ?: throw Exception("User not found") }

    suspend fun loadPlayerGamesList() {
        loading.value = true
        try {
            viewModelScope.launch {
                userGamesList.value = dataController.getAllGames()
                    ?.filter { it.getUser().getEmail() == actualPlayer.getEmail() }
                    ?.sortedByDescending { it.getScore() }
                    ?: listOf()
            }.join()
        } catch (e: Exception) {
            Log.e("ScoresScreenViewModel", "ladUserGamesList: $e")
            userGamesList.value = listOf()
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
}