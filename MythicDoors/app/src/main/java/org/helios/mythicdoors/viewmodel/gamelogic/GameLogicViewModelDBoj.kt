package org.helios.mythicdoors.viewmodel.gamelogic

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.DoorDBoj
import org.helios.mythicdoors.model.entities.EnemyDBoj
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.store.StoreManager

class GameLogicViewModelDBoj(
    private val dataController: DataController,
    private val chosenDoor: String,
    private val bet: Int
): ViewModel() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val storeManager: StoreManager by lazy { StoreManager.getInstance() }
    /* En esta ocasión no podemos continuar con la lógica hasta tener al usuario, así que la captura de usuario se genera en una coroutine que bloquea el resto de lógica */
    private val user: User by lazy {
        runBlocking {
            storeManager.getAppStore().actualUser ?: throw Exception("User not found") }
    }

    private val door: DoorDBoj by lazy { generateDoor() }
    private lateinit var enemy: EnemyDBoj
    private val combatConfrontationResult: Boolean by lazy { generateCombat() }

    fun battle(): Boolean {
        return try {
            combatUpdater(combatConfrontationResult).takeIf { it }?.let { scope.launch { updateUser() } }
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
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            calculateScore(),
            increaseCurrentLevelIfNeeded(),
            storeManager.getAppStore().combatResults.resultXpAmount,
            storeManager.getAppStore().combatResults.resultCoinAmount,
            user.getGoldCoins(),
            user.getIsActive(),
            user.getCreatedAt()
        )
    }

    private fun generateDoor(): DoorDBoj { return DoorDBoj.create(chosenDoor) }

    private fun generateCombat(): Boolean { return user.getLevel() >= generateEnemy().getLevel() }

    private fun generateEnemy(): EnemyDBoj { return EnemyDBoj.create(generateEnemyLevel(door)) }

    private fun generateEnemyLevel(door: DoorDBoj): Int {
        val rangeValues = generateRangeValues()
        return generateRandomNumber(rangeValues.first, rangeValues.second)
    }

    private fun generateRangeValues(): Pair<Int, Int> {
        val minimumEnemyRage = user.getLevel().plus(door.getMinEnemyRangeSetter()).coerceAtLeast(0)
        val maximumEnemyRage = user.getLevel().plus(door.getMaxEnemyRangeSetter()).coerceAtMost(10)

        return if(minimumEnemyRage < maximumEnemyRage) Pair(0, 2) else Pair(minimumEnemyRage, maximumEnemyRage)
    }

    private fun generateRandomNumber(min: Int, max: Int): Int { return (min..max).random() }

    private fun combatUpdater(userHasWon: Boolean): Boolean {
        try {
            userHasWon.takeIf { it }?.let {
                storeManager.updateCombatResults(
                    true,
                    enemy,
                    user.getCoins().plus(getCoinReward()).coerceAtLeast(0),
                    user.getLevel().takeIf { it >= enemy.getLevel() }?.let { user.getExperience().plus(getXpReward()) } ?: 0
                )
            } ?: storeManager.updateCombatResults(
                false,
                enemy,
                user.getCoins().takeIf { it >= bet }?.minus(bet) ?: -1,
                user.getExperience().coerceAtLeast(0)
            )
            return true
        } catch (e: Exception) {
            e.printStackTrace().also { return false }
        }
    }

    private fun getCoinReward(): Int { return bet.plus(enemy.getCoinReward().times(door.getBonusRatio())).toInt() }

    private fun getXpReward(): Int { return enemy.getLevel().minus(user.getLevel()).times(door.getBonusRatio()).toInt() }

    private fun calculateScore(): Int { return if (storeManager.getAppStore().combatResults.resultCoinAmount > user.getScore()) storeManager.getAppStore().combatResults.resultCoinAmount else user.getScore() }

    private fun increaseCurrentLevelIfNeeded(): Int {
        val experienceIncrement = 100
        val newUserExperience = user.getExperience().plus(storeManager.getAppStore().combatResults.resultXpAmount)

        newUserExperience.takeIf { it >= experienceIncrement && it % experienceIncrement == 0 }?.let { return user.getLevel().plus(1).coerceAtMost(9) }
            ?: return user.getLevel()
    }
}