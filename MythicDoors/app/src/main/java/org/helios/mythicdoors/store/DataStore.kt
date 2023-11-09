package org.helios.mythicdoors.store

import org.helios.mythicdoors.model.entities.EnemyDBoj
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.utils.AppConstants.GameMode

data class AppStore(
    val combatResults: CombatResults = CombatResults(),
    var actualUser: User? = null,
    var gameMode: GameMode = GameMode.SINGLE_PLAYER
    )

data class CombatResults(
    var isPlayerWinner: Boolean = false,
    var enemy: EnemyDBoj? = null,
    var resultCoinAmount: Int = 0,
    var resultXpAmount: Int = 0
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

    fun updateActualUser(user: User) { appStore.actualUser = user }

    fun updateGameMode(gameMode: String) { (gameMode == GameMode.MULTI_PLAYER.toString()).let { if(it) appStore.gameMode = GameMode.MULTI_PLAYER else GameMode.SINGLE_PLAYER } }
}