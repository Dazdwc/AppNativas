package org.helios.mythicdoors.store

import android.util.Log
import org.helios.mythicdoors.model.entities.EnemyDBoj
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.utils.AppConstants.GameMode

data class AppStore(
    val combatResults: CombatResults = CombatResults(),
    val playerAction: PlayerAction = PlayerAction(),
    val playerInitialStats: PlayerInitialStats = PlayerInitialStats(),
    var actualUser: User? = null,
    var gameMode: GameMode = GameMode.SINGLE_PLAYER,
    var gameScore: Int = 0
    )

data class CombatResults(
    var isPlayerWinner: Boolean = false,
    var enemy: EnemyDBoj? = null,
    var resultCoinAmount: Int = 0,
    var resultXpAmount: Int = 0
)

data class PlayerAction(
    var bet : Int = 0,
    var selectedDoorId: String = ""
)

data class PlayerInitialStats(
    var level: Int = 1,
    var experience: Int = 0,
    var coins: Int = 0,
    var score: Int = 0
)

class StoreManager {
    private var appStore: AppStore = AppStore()

    /* Patrón Singleton */
    companion object {
        @Volatile
        private var instance : StoreManager? = null

        fun getInstance(): StoreManager {
            return instance ?: synchronized(this) {
                instance ?: buildStoreManager().also { instance = it }
            }
        }

        private fun buildStoreManager(): StoreManager {
            return StoreManager()
        }
    }

    fun getAppStore(): AppStore { return appStore }

    fun updateCombatResults(
        isPlayerWinner: Boolean,
        enemy: EnemyDBoj?,
        resultCoinAmount: Int,
        resultXpAmount: Int
    ) {
        appStore.combatResults.isPlayerWinner = isPlayerWinner
        appStore.combatResults.enemy = enemy
        appStore.combatResults.resultCoinAmount = resultCoinAmount
        appStore.combatResults.resultXpAmount = resultXpAmount
    }

    fun updatePlayerAction(bet: Int, selectedDoorId: String) {
        appStore.playerAction.bet = bet
        appStore.playerAction.selectedDoorId = selectedDoorId
    }

    fun updateActualUser(user: User) { appStore.actualUser = user }

    fun updateGameMode(gameMode: String) { (gameMode == GameMode.MULTI_PLAYER.toString()).let { if(it) appStore.gameMode = GameMode.MULTI_PLAYER else GameMode.SINGLE_PLAYER } }

    fun updateOriginalPlayerStats(user: User?) {
        appStore.playerInitialStats.level = user?.getLevel() ?: 1
        appStore.playerInitialStats.experience = user?.getExperience() ?: 0
        appStore.playerInitialStats.coins = user?.getCoins() ?: 0
        appStore.playerInitialStats.score = user?.getScore() ?: 0
    }

    fun updatePlayerCoins(coins: Int) { appStore.actualUser?.getCoins()?.takeIf { it < 100 }.let { appStore.actualUser?.setCoins(coins) } }

    fun updateGameScore(score: Int) { appStore.gameScore = score }

    fun clearCombatResults() {
        appStore.combatResults.isPlayerWinner = false
        appStore.combatResults.enemy = null
        appStore.combatResults.resultCoinAmount = 0
        appStore.combatResults.resultXpAmount = 0
    }

    fun clearCombatData() {
        clearCombatResults()
        clearPlayerAction()
    }

    fun logout() { appStore.actualUser = null }

    fun resetPlayerCoins() { appStore.actualUser?.setCoins(100) }

    private fun clearPlayerAction() {
        appStore.playerAction.bet = 0
        appStore.playerAction.selectedDoorId = ""
    }
}