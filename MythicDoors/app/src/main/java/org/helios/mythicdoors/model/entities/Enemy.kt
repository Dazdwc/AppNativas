package org.helios.mythicdoors.model.entities

import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants.ENEMY_MUMMY
import org.helios.mythicdoors.utils.AppConstants.ENEMY_FRANKY
import org.helios.mythicdoors.utils.AppConstants.ENEMY_JACKO
import org.helios.mythicdoors.utils.AppConstants.ENEMY_REAPY
import org.helios.mythicdoors.utils.AppConstants.ENEY_WOLFIE

data class Enemy(
    private val name: String,
    private val level: Long,
    private val coinReward: Long,
    private var imageResourceId: Int?
) {
    fun getName(): String { return name }
    fun getLevel(): Long { return level }
    fun getCoinReward(): Long { return coinReward }
    fun getImage(): Int? { return imageResourceId }

    fun setImage(imageResourceId: Int?) { this.imageResourceId = imageResourceId }

    companion object {
        fun create(
            level: Long,
        ): Enemy {
            return when (level) {
                in 0L..2L -> Enemy(ENEMY_MUMMY, level, 10, R.drawable.enemy_mummy)
                in 3L..4L-> Enemy(ENEMY_FRANKY, level, 20, R.drawable.enemy_franky)
                in 5L..6L -> Enemy(ENEY_WOLFIE, level, 30, R.drawable.enemy_wolfie)
                in 7L..8L -> Enemy(ENEMY_JACKO, level, 40, R.drawable.enemy_jacko)
                in 9L..10L -> Enemy(ENEMY_REAPY, level, 50, R.drawable.enemy_reapy)
                else -> throw Exception("Invalid enemy level")
            }
        }

        fun createEmptyEnemy(): Enemy {
            return Enemy(
                "",
                0L,
                0L,
                null
            )
        }
    }

    fun isEmpty(): Boolean {
        return name.isBlank()
    }
}