package org.helios.mythicdoors.model

import org.helios.mythicdoors.model.entities.EnemyDBoj

data class CombatResults(
    var isPlayerWinner: Boolean = false,
    var enemy: EnemyDBoj? = null,
    var resultCoinAmount: Double = 0.0,
    var resultXpAmount: Int = 0
)

class CombatResultsStore {
    var currentResults: CombatResults = CombatResults()
        private set

    fun updateResults(
        isPlayerWinner: Boolean,
        enemy: EnemyDBoj?,
        resultCoinAmount: Double,
        resultXpAmount: Int
    ) {
        currentResults.isPlayerWinner = isPlayerWinner
        currentResults.enemy = enemy
        currentResults.resultCoinAmount = resultCoinAmount
        currentResults.resultXpAmount = resultXpAmount
    }
}

//data ob CombatResultsStore {
//    var isPlayerWinner: Boolean = false
//    var enemy: EnemyDBoj? = null
//    var resultCoinAmount: Double = 0.0
//    var resultXpAmount: Double = 0.0
//}