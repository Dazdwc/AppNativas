package org.helios.mythicdoors.model.entities

import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants.ENEMY_MUMMY
import org.helios.mythicdoors.utils.AppConstants.ENEMY_FRANKY
import org.helios.mythicdoors.utils.AppConstants.ENEMY_JACKO
import org.helios.mythicdoors.utils.AppConstants.ENEMY_REAPY
import org.helios.mythicdoors.utils.AppConstants.ENEY_WOLFIE

data class EnemyDBoj(
    // private val id: Long?,
    private val name: String,
    private val level: Int,
    private val coinReward: Int,
    private var imageResourceId: Int?
    // TODO: Add fields
) {
    // fun getId(): Long? { return id }
    fun getName(): String { return name }
    fun getLevel(): Int { return level }
    fun getCoinReward(): Int { return coinReward }
    fun getImage(): Int? { return imageResourceId }

    fun setImage(imageResourceId: Int?) { this.imageResourceId = imageResourceId }

    /*
    * Implementación del patrón Fabric
    */
    companion object {
        fun create(
            level: Int,
        ): EnemyDBoj {
            return when (level) {
                in 0..2 -> EnemyDBoj(ENEMY_MUMMY, level, 10, R.drawable.enemy_mummy)
                in 3..4-> EnemyDBoj(ENEMY_FRANKY, level, 20, R.drawable.enemy_franky)
                in 5..6 -> EnemyDBoj(ENEY_WOLFIE, level, 30, R.drawable.enemy_wolfie)
                in 7..8 -> EnemyDBoj(ENEMY_JACKO, level, 40, R.drawable.enemy_jacko)
                in 9..10 -> EnemyDBoj(ENEMY_REAPY, level, 50, R.drawable.enemy_reapy)
                else -> throw Exception("Invalid enemy level")
            }
        }
    }

//    fun isEmpty(): Boolean {
//        return id == null
//    }
}