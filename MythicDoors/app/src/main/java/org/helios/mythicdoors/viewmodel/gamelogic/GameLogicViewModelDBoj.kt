package org.helios.mythicdoors.viewmodel.gamelogic

import kotlinx.coroutines.runBlocking
import org.helios.mythicdoors.model.CombatResultsStore
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.DoorDBoj
import org.helios.mythicdoors.model.entities.EnemyDBoj
import org.helios.mythicdoors.model.entities.User

class GameLogicViewModelDBoj(
    private val dataController: DataController,
    private val chosenDoor: String,
    private val id: Long,
    private val bet: Int
) {
    /* En esta ocasión no podemos continuar con la lógica hasta tener al usuario, así que la captura de usuario se genera en una coroutine que bloquea el resto de lógica */
    private val user: User by lazy {
        runBlocking { dataController.getUser(id) ?: throw Exception("User not found") }
    }

    private val door: DoorDBoj by lazy { generateDoor() }
    private lateinit var enemy: EnemyDBoj
    private lateinit var combatResults: CombatResultsStore
    private val combatConfrontationResult: Boolean by lazy { generateCombat() }



    //fun getActualUser(): User { return user }


  //  fun getUser(): User { return user }


    fun getNewUserStadistics(): User {
        if (combatConfrontationResult) {
            winingCombatUpdater()
        } else {
            loosingCombatUpdater()
        }

        return User(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            calculateScore(),
            increaseCurrentLevelIfNeeded(),
            combatResults.currentResults.resultXpAmount,
            combatResults.currentResults.resultCoinAmount.toInt(),
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
        val minimumEnemyRage = if (user.getLevel().plus(door.getMinEnemyRangeSetter()) < 0) 0 else user.getLevel().plus(door.getMinEnemyRangeSetter())
        val maximumEnemyRage = if (user.getLevel().plus(door.getMaxEnemyRangeSetter()) > 10) 10 else user.getLevel().plus(door.getMaxEnemyRangeSetter())

        if(maximumEnemyRage < minimumEnemyRage) Pair(0, 2)

        return Pair(minimumEnemyRage, maximumEnemyRage)
    }

    private fun generateRandomNumber(
        min: Int,
        max: Int
    ): Int {
        return (min..max).random()
    }

    private fun winingCombatUpdater() {

        combatResults.updateResults(
            true,
            enemy,
            user.getCoins().plus(getCoinReward()),
            if (user.getLevel() >= enemy.getLevel()) user.getExperience().plus(getXpReward()) else 0
        )
    }

    private fun loosingCombatUpdater() {

        combatResults.updateResults(
            true,
            enemy,
            if (user.getCoins() >= bet) user.getCoins().minus(bet).toDouble() else -1.0,
            0
        )
    }

    private fun getCoinReward(): Double { return bet.plus(enemy.getCoinReward().times(door.getBonusRatio())) }

    private fun getXpReward(): Int { return enemy.getLevel().minus(user.getLevel()).times(door.getBonusRatio()).toInt() }

    private fun calculateScore(): Int { return if (combatResults.currentResults.resultCoinAmount > user.getScore()) combatResults.currentResults.resultCoinAmount.toInt() else user.getScore() }

    private fun increaseCurrentLevelIfNeeded(): Int {
        val experienceIncrement = 100

        if (user.getExperience() >= experienceIncrement && user.getExperience() % experienceIncrement == 0) return user.getLevel().plus(1)

        return user.getLevel()
    }
}