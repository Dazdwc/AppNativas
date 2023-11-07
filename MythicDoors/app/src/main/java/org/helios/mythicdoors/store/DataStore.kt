package org.helios.mythicdoors.store

import org.helios.mythicdoors.model.entities.EnemyDBoj
import org.helios.mythicdoors.model.entities.User

data class AppStore(
    val combatResults: CombatResults = CombatResults(),
    var actualUser: User? = null
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

    fun getAppStore(): AppStore {
        return appStore
    }

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

    fun updateActualUser(user: User) {
        appStore.actualUser = user
    }

}

//class CombatResultsStore {
//    var currentResults: CombatResults = CombatResults()
//        private set
//
//    fun updateResults(
//        isPlayerWinner: Boolean,
//        enemy: EnemyDBoj?,
//        resultCoinAmount: Double,
//        resultXpAmount: Int
//    ) {
//        currentResults.isPlayerWinner = isPlayerWinner
//        currentResults.enemy = enemy
//        currentResults.resultCoinAmount = resultCoinAmount
//        currentResults.resultXpAmount = resultXpAmount
//    }
//}

//data ob CombatResultsStore {
//    var isPlayerWinner: Boolean = false
//    var enemy: EnemyDBoj? = null
//    var resultCoinAmount: Double = 0.0
//    var resultXpAmount: Double = 0.0
//}