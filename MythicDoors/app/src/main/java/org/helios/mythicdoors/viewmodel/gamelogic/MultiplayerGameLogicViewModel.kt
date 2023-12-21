package org.helios.mythicdoors.viewmodel.gamelogic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.Jackpot
import org.helios.mythicdoors.model.entities.User
import kotlin.properties.Delegates

class MultiplayerGameLogicViewModel(
    private val dataController: DataController
): ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun jackpotRandomizer(
        onRandomUserReceived: (User) -> Unit
    ) {
        var selectedUser: User? = null

        try {
            viewModelScope.launch {
                val totalUsersNumber = dataController.countFSUsers()
                val randomUserNumber = (0..<totalUsersNumber).random()
                selectedUser = dataController.getAllFSUsers()?.get(randomUserNumber) ?: User.createEmptyUser()
            }.invokeOnCompletion {
                onRandomUserReceived(selectedUser ?: User.createEmptyUser())
            }
        } catch (e: Exception) {
            Log.e("MultiplayerGameLogicViewModel", "Error getting random jackpot: ${e.message}")
            User.createEmptyUser()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getJackpotPot(): Long {
        var result = 0L

        return try {
            viewModelScope.launch {
                result = dataController.getJackpotPot()
            }
            result
        } catch (e: Exception) {
            Log.e("MultiplayerGameLogicViewModel", "Error getting jackpot pot: ${e.message}")
            result
        }
    }

    fun setJackpotPot(pot: Long): Boolean {
        var result = false

        return try {

            viewModelScope.launch {
                getLastJackpot().let { jackpot ->
                    val newPot = jackpot.getPot().plus(pot)
                    dataController.postJackpot(Jackpot.create(newPot))
                }
            }.invokeOnCompletion { result = true }

            result
        } catch (e: Exception) {
            Log.e("MultiplayerGameLogicViewModel", "Error setting jackpot pot: ${e.message}")
            result
        }
    }

    fun resetJackpotPot() = viewModelScope.launch { dataController.postJackpot(Jackpot.createEmpty()) }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getLastJackpot(): Jackpot {
        var result = Jackpot.createEmpty()

        return try {
            viewModelScope.launch {
                result = dataController.getLastJackpot().getOrNull() ?: Jackpot.createEmpty()
            }

            result
        } catch (e: Exception) {
            Log.e("MultiplayerGameLogicViewModel", "Error getting last jackpot: ${e.message}")
            result
        }
    }
}