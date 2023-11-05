package org.helios.mythicdoors.model.entities

import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants.EASY_DOOR
import org.helios.mythicdoors.utils.AppConstants.AVERAGE_DOOR
import org.helios.mythicdoors.utils.AppConstants.HARD_DOOR

data class DoorDBoj(
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
        ): DoorDBoj {
            return when(id) {
                EASY_DOOR -> DoorDBoj(id, -3, 1, 1.5, R.drawable.easy_door)
                AVERAGE_DOOR -> DoorDBoj(id, 3, 5, 1.5, R.drawable.average_door)
                HARD_DOOR -> DoorDBoj(id, 5, 7, 2.0, R.drawable.hard_door)
                else -> throw Exception("Invalid door id")
            }
        }
    }
}
