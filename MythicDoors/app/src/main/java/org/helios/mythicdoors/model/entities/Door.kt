package org.helios.mythicdoors.model.entities

import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants.EASY_DOOR
import org.helios.mythicdoors.utils.AppConstants.AVERAGE_DOOR
import org.helios.mythicdoors.utils.AppConstants.HARD_DOOR

data class Door(
    private val id: String,
    private val minEnemyRangeSetter: Int,
    private val maxEnemyRangeSetter: Int,
    private val bonusRatio: Double,
    private val imageResourceId: Int?
) {
    fun getId(): String { return id }
    fun getMinEnemyRangeSetter(): Int { return minEnemyRangeSetter }
    fun getMaxEnemyRangeSetter(): Int { return maxEnemyRangeSetter }
    fun getBonusRatio(): Double { return bonusRatio }
    fun getImageResourceid(): Int? { return imageResourceId }

    companion object {
        fun create(
            id: String,
        ): Door {
            return when(id) {
                EASY_DOOR -> Door(id, -3, 1, 1.0, R.drawable.easy_door)
                AVERAGE_DOOR -> Door(id, -2, 2, 1.5, R.drawable.average_door)
                HARD_DOOR -> Door(id, -1, 3, 2.0, R.drawable.hard_door)
                else -> throw Exception("Invalid door id")
            }
        }

        fun createEmptyDoor(): Door {
            return Door("", 0, 0, 0.0, null)
        }
    }
}
