package org.helios.mythicdoors.viewmodel.gamelogic

import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.Door
import org.helios.mythicdoors.model.entities.Enemy
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.services.location.ILocationCallback
import org.helios.mythicdoors.services.location.LocationService
import org.helios.mythicdoors.services.location.LocationServiceReceiver
import org.helios.mythicdoors.store.StoreManager
import kotlin.properties.Delegates

class GameLogicViewModel(private val dataController: DataController): ViewModel() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val storeManager: StoreManager by lazy { StoreManager.getInstance() }

    private lateinit var player: User
    private var chosenDoor: String = ""
    private var bet: Int = 0

    private lateinit var door: Door
    private lateinit var enemy: Enemy
    private var combatConfrontationResult by Delegates.notNull<Boolean>()

    private val locationService: LocationService
        get() = LocationService.instance
    private val locationServiceObserver: Observer<Map<String, Double>> = Observer { location ->
        Log.e("GameLogicViewModel", "locationServiceObserver: $location")
        handleLocationUpdate(location)
    }

    private val callback: ILocationCallback = object : ILocationCallback {
        override fun onLocationUpdate(location: Map<String, Double>) {
            Log.e("GameLogicViewModel", "onLocationUpdate: $location")
            LocationService.instance.updateLocation()
        }
    }

    init {
        locationService.locationLiveData.observe(MainActivity(), locationServiceObserver)
    }

    override fun onCleared() {
        super.onCleared()
        locationService.locationLiveData.removeObserver(locationServiceObserver)
    }

    fun loadBetValues() {
        chosenDoor = runBlocking { storeManager.getAppStore().playerAction.selectedDoorId }
        bet = runBlocking { storeManager.getAppStore().playerAction.bet }
        player = loadPlayer()
    }

    fun battle(): Boolean {

        door = generateDoor()
        enemy = generateEnemy()

        return try {
            combatConfrontationResult = generateCombat()
            combatUpdater(combatConfrontationResult).let { scope.launch { updateUser() } }
            clearEnemyAndDoor()
            true
        } catch (e: Exception) {
            Log.e("GameLogicViewModel", "battle: ${e.message}")
            false
        }
    }

    private fun loadPlayer(): User {
        return try {
            runBlocking { storeManager.getAppStore().actualUser ?: User.createEmptyUser().also { throw Exception("User not found") } }
        } catch (e: Exception) {
            Log.e("GameLogicViewModel", "loadPlayer: $e")
            User.createEmptyUser()
        }
    }

    private suspend fun updateUser(): Boolean {
        return try {
            var isUserSaved = false
            val updatedUser: User = getNewUserStaticsAfterBattle()
            Log.w("GameLogicViewModel", "updateUser: $updatedUser")
            storeManager.updateActualUser(updatedUser)

            scope.launch {
                isUserSaved = dataController.saveUser(updatedUser)
            }.join()
            return isUserSaved
        } catch (e: Exception) {
            Log.e("GameLogicViewModel", "updateUser: ${e.message}")
            false
        }
    }

    private fun getNewUserStaticsAfterBattle(): User {
        return User(
            player.getId(),
            player.getName(),
            player.getEmail(),
            player.getPassword(),
            calculateScore(),
            increaseCurrentLevelIfNeeded(),
            storeManager.getAppStore().combatResults.resultXpAmount,
            storeManager.getAppStore().combatResults.resultCoinAmount,
            player.getGoldCoins(),
            player.getIsActive(),
            player.getCreatedAt()
        ).also { Log.w("GameLogicViewModel", "getNewUserStaticsAfterBattle: $it") }
    }

    private fun generateDoor(): Door { return Door.create(chosenDoor) }

    private fun generateEnemy(): Enemy { return Enemy.create(generateEnemyLevel()) }

    private fun generateCombat(): Boolean {
        return player.getLevel() >= (enemy.getLevel()) }

    private fun generateEnemyLevel(): Int {
        val rangeValues = generateRangeValues()
        return generateRandomNumber(rangeValues.first, rangeValues.second)
    }

    private fun generateRangeValues(): Pair<Int, Int> {
        val minimumEnemyRage = player.getLevel().plus(door.getMinEnemyRangeSetter().coerceAtLeast(0))
        val maximumEnemyRage = player.getLevel().plus(door.getMaxEnemyRangeSetter().coerceAtMost(10))

        return if(minimumEnemyRage > maximumEnemyRage) Pair(0, 2) else Pair(minimumEnemyRage, maximumEnemyRage)
    }

    private fun generateRandomNumber(min: Int, max: Int): Int { return (min..max).random().coerceIn(0..10) }

    private fun combatUpdater(userHasWon: Boolean): Boolean {
        try {
            userHasWon.let { winner ->
                if (winner) {
                    storeManager.updateCombatResults(
                        true,
                        enemy,
                        player.getCoins().plus(getCoinReward()).coerceAtLeast(0),
                        player.getLevel().takeIf { it >= (enemy.getLevel()) }
                            ?.let { player.getExperience().plus(getXpReward()) } ?: 0
                    )
                    storeManager.updateGameScore(storeManager.getAppStore().gameScore.plus(calculateScore()))

                    saveLocation()
                } else {
                    storeManager.updateCombatResults(
                        false,
                        enemy,
                        player.getCoins().takeIf { it >= bet }?.minus(bet) ?: -1,
                        player.getExperience().coerceAtLeast(0)
                    )
                }
            }
            return true
        } catch (e: Exception) {
            Log.e("GameLogicViewModel", "combatUpdater: ${e.message}").also { return false }
        }
    }

    private fun getCoinReward(): Int { return door.getBonusRatio().let { it -> enemy.getCoinReward().times(it).let { bet.plus(it).toInt() } } }

    private fun getXpReward(): Int {
        return if (combatConfrontationResult) door.getBonusRatio().let { player.getLevel().minus(enemy.getLevel()).times(it).plus(enemy.getCoinReward()).times(getCoinReward()).toInt() }
            else 0
    }

    private fun calculateScore(): Int {
        val scoreIncrement: Int = player.getScore().plus(enemy.getCoinReward().times(door.getBonusRatio()).toInt())
        return if (combatConfrontationResult) player.getScore().plus(scoreIncrement) else 0
    }

    private fun increaseCurrentLevelIfNeeded(): Int {
        val experienceIncrement = 1000
        val newUserExperience = player.getExperience().plus(storeManager.getAppStore().combatResults.resultXpAmount)

        newUserExperience.takeIf { it >= experienceIncrement && it % experienceIncrement == 0 }?.let { return player.getLevel().plus(1).coerceIn(1..9) }
            ?: return player.getLevel()
    }

    private fun clearEnemyAndDoor() {
        enemy.takeIf { true }?.let { enemy = Enemy.createEmptyEnemy() }
        door.takeIf { true }?.let { door = Door.createEmptyDoor()}
    }

    private fun saveLocation() {
        try {
            startLocationService()
        } catch (e: Exception) {
            Log.e("GameLogicViewModel", "saveLocation: ${e.message}")
        }
    }

    private fun startLocationService() {
        val context = storeManager.getContext()

        try {
            Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                context?.startService(this)
            }

            LocationService.instance.locationCallback = callback
        } catch (e: Exception) {
            Log.e("GameLogicViewModel", "startLocationService: ${e.message}")
        }
    }

    private fun handleLocationUpdate(location: Map<String, Double>) {
        try {
            viewModelScope.launch {
                Location.create(
                    player,
                    location["latitude"] ?: 0.0,
                    location["longitude"] ?: 0.0
                ).also { dataController.saveLocation(it) }
            }
        } catch (e: Exception) {
            Log.e("GameLogicViewModel", "handleLocationUpdate: ${e.message}")
        }
    }

    private fun stopLocationService() {
        val context = storeManager.getContext()

        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            context?.startService(this)
        }
    }
}