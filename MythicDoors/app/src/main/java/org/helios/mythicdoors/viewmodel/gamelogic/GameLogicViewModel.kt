package org.helios.mythicdoors.viewmodel.gamelogic

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.DoorDBoj
import org.helios.mythicdoors.model.entities.EnemyDBoj
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.store.StoreManager

class GameLogicViewModel(private val dataController: DataController): ViewModel() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val storeManager: StoreManager by lazy { StoreManager.getInstance() }

    /* En esta ocasión no podemos continuar con la lógica hasta tener al usuario jugador, la puerta escogida por este y su apuesta,
    * así que la captura de usuario se genera en una coroutine que bloquea el resto de lógica.
    */
    private val player: User by lazy {
        runBlocking {
            storeManager.getAppStore().actualUser ?: throw Exception("User not found") }
    }

    private val chosenDoor: String by lazy {
        runBlocking {
            storeManager.getAppStore().playerAction.selectedDoorId
        }
    }
    private val bet: Int by lazy {
        runBlocking {
            storeManager.getAppStore().playerAction.bet
        }
    }

    private lateinit var door: DoorDBoj
    private lateinit var enemy: EnemyDBoj
    private val combatConfrontationResult: Boolean by lazy { generateCombat() }

    fun battle(): Boolean {
        return try {
            door = generateDoor()
            enemy = generateEnemy()
            combatUpdater(combatConfrontationResult).takeIf { it }?.let { scope.launch { updateUser() } }.also { clearEnemyAndDoor() }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private suspend fun updateUser(): Boolean {
        return try {
            storeManager.updateActualUser(getNewUserStaticsAfterBattle())

            val saveUserJob: CompletableJob = Job()
            saveUserJob.apply {
                scope.launch {
                    dataController.saveUser(storeManager.getAppStore().actualUser ?: throw Exception("User not found"))
                    saveUserJob.complete()
                }
                    .join()
            }
            return saveUserJob.isCompleted
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getNewUserStaticsAfterBattle(): User {
//        //combatConfrontationResult.takeIf { it }?.let { combatUpdater(true)} ?: combatUpdater(false)
//        combatUpdater(combatConfrontationResult)

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

    private fun generateDoor(): DoorDBoj { return DoorDBoj.create(chosenDoor) }

    private fun generateEnemy(): EnemyDBoj { return EnemyDBoj.create(generateEnemyLevel()) }

    private fun generateCombat(): Boolean {
        Log.e("GameLogicViewModel", "generateCombat: ${player.getLevel() >= (enemy.getLevel())}")
        return player.getLevel() >= (enemy.getLevel()) }

    private fun generateEnemyLevel(): Int {
        val rangeValues = generateRangeValues()
        return generateRandomNumber(rangeValues.first, rangeValues.second)
    }

    private fun generateRangeValues(): Pair<Int, Int> {
        val minimumEnemyRage = door.getMinEnemyRangeSetter().let { player.getLevel().plus(it).coerceAtLeast(0) }
        val maximumEnemyRage = door.getMaxEnemyRangeSetter().let { player.getLevel().plus(it).coerceAtMost(10) }

        return if(minimumEnemyRage < maximumEnemyRage) Pair(0, 2) else Pair(minimumEnemyRage, maximumEnemyRage)
    }

    private fun generateRandomNumber(min: Int, max: Int): Int { return (min..max).random() }

    private fun combatUpdater(userHasWon: Boolean): Boolean {
        try {
            userHasWon.takeIf { it }?.let {
                storeManager.updateCombatResults(
                    true,
                    enemy,
                    player.getCoins().plus(getCoinReward()).coerceAtLeast(0),
                    player.getLevel().takeIf { it >= (enemy.getLevel()) }?.let { player.getExperience().plus(getXpReward()) } ?: 0
                )
            } ?: storeManager.updateCombatResults(
                false,
                enemy,
                player.getCoins().takeIf { it >= bet }?.minus(bet) ?: -1,
                player.getExperience().coerceAtLeast(0)
            )
            return true
        } catch (e: Exception) {
            e.printStackTrace().also { return false }
        }
    }

    private fun getCoinReward(): Int {
        return door.getBonusRatio().let { it -> enemy.getCoinReward().times(it).let { bet.plus(it).toInt() } }
    }

    private fun getXpReward(): Int { return door.getBonusRatio().let { enemy.getLevel().minus(player.getLevel()).times(it).toInt() } }

    private fun calculateScore(): Int {
        val scoreIncrement: Int = storeManager.getAppStore().combatResults.resultCoinAmount.times(door.getBonusRatio()).toInt()
        return if (storeManager.getAppStore().combatResults.isPlayerWinner) player.getScore().plus(scoreIncrement) else 0
    }

    private fun increaseCurrentLevelIfNeeded(): Int {
        val experienceIncrement = 100
        val newUserExperience = player.getExperience().plus(storeManager.getAppStore().combatResults.resultXpAmount)

        newUserExperience.takeIf { it >= experienceIncrement && it % experienceIncrement == 0 }?.let { return player.getLevel().plus(1).coerceAtMost(9) }
            ?: return player.getLevel()
    }

    private fun clearEnemyAndDoor() {
        enemy.takeIf { true }?.let { enemy = EnemyDBoj.createEmptyEnemy() }
        door.takeIf { true }?.let { door = DoorDBoj.createEmptyDoor()}
    }
}