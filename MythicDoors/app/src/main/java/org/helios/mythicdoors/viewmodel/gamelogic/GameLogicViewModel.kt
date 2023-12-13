package org.helios.mythicdoors.viewmodel.gamelogic

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.Tasks.await
import kotlinx.coroutines.*
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.Door
import org.helios.mythicdoors.model.entities.Enemy
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.services.location.ILocationCallback
import org.helios.mythicdoors.services.location.LocationService
import org.helios.mythicdoors.services.location.LocationServiceReceiver
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.calendar.CalendarService
import org.helios.mythicdoors.utils.extenssions.hasPostNotificationPermission
import org.helios.mythicdoors.utils.notifications.NotificationFabric
import kotlin.properties.Delegates

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
        scope.launch { handleLocationUpdate(location) }
    }

    private val callback: ILocationCallback = object : ILocationCallback {
        override fun onLocationUpdate(location: Map<String, Double>) {
            Log.e("GameLogicViewModel", "onLocationUpdate: $location")

            scope.launch {
                LocationService.instance.updateLocation()
                dataController.getLastLocation()?.let { actLocation ->
                    Log.e("GameLogicViewModel", "onLocationUpdate: $actLocation")
                    insertEventOnCalendar(actLocation)
                }
                stopLocationService()
            }
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
                isUserSaved = dataController.saveOneFSUser(updatedUser).getOrElse { false } // dataController.saveUser(updatedUser)
            }.join()
            return isUserSaved
        } catch (e: Exception) {
            Log.e("GameLogicViewModel", "updateUser: ${e.message}")
            false
        }
    }

    private fun getNewUserStaticsAfterBattle(): User {
        return User(
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

    private fun generateEnemyLevel(): Long {
        val rangeValues = generateRangeValues()
        return generateRandomNumber(rangeValues.first, rangeValues.second)
    }

    private fun generateRangeValues(): Pair<Long, Long> {
        val minimumEnemyRage = player.getLevel().plus(door.getMinEnemyRangeSetter().coerceAtLeast(0))
        val maximumEnemyRage = player.getLevel().plus(door.getMaxEnemyRangeSetter().coerceAtMost(10))

        return if(minimumEnemyRage > maximumEnemyRage) Pair(0L, 2L) else Pair(minimumEnemyRage, maximumEnemyRage)
    }

    private fun generateRandomNumber(min: Long, max: Long): Long { return (min..max).random().coerceIn(0L..10L) }

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

    private fun getCoinReward(): Long { return door.getBonusRatio().let { it -> enemy.getCoinReward().times(it).let { bet.plus(it) } }.toLong() }

    private fun getXpReward(): Long {
        return if (combatConfrontationResult) door.getBonusRatio().let { player.getLevel().minus(enemy.getLevel()).times(it).plus(enemy.getCoinReward()).times(getCoinReward()).toLong() }
            else 0
    }

    private fun calculateScore(): Long {
        val scoreIncrement: Long = player.getScore().plus(enemy.getCoinReward().times(door.getBonusRatio()).toLong())
        return if (combatConfrontationResult) player.getScore().plus(scoreIncrement) else 0L
    }

    private fun increaseCurrentLevelIfNeeded(): Long {
        val experienceIncrement = 1000
        val newUserExperience = player.getExperience().plus(storeManager.getAppStore().combatResults.resultXpAmount)

        newUserExperience.takeIf { it >= experienceIncrement && it % experienceIncrement == 0L }?.let { return player.getLevel().plus(1).coerceIn(1L..9L) }
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
        val context = MainActivity.getContext()

        try {
            Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                context.startService(this)
            }

            LocationService.instance.locationCallback = callback
        } catch (e: Exception) {
            Log.e("GameLogicViewModel", "startLocationService: ${e.message}")
        }
    }

    private fun handleLocationUpdate(location: Map<String, Double>) {
        Log.e("GameLogicViewModel", "handleLocationUpdate: $location")
        try {
                scope.launch {
                    val currentLocation = Location.create(
//                        player,
                        player.getEmail() ?: "" /*throw Exception("Player not found")*/,
                        location["latitude"] ?: 0.0,
                        location["longitude"] ?: 0.0
                    )
                    currentLocation.apply {
                        stopLocationService()
                    }
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

    private fun insertEventOnCalendar(location: Location) {
        val context = MainActivity.getContext()
        val calendarService = CalendarService(context, location)

        viewModelScope.launch {
            calendarService.insertEvent().let {
                if (it) {
                    try {
                        NotificationFabric.create(AppConstants.NotificationChannels.CALENDAR_NOTIFICATION_CHANNEL)
                            .also { notification ->
                                NotificationFabric.send(notification)
                            }
                    } catch (e: Exception) {
                        Log.e("GameLogicViewModel", "Notification sender: $e")
                    }
                }
            }
        }
    }
}