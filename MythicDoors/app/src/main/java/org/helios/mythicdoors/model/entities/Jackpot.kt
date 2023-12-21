package org.helios.mythicdoors.model.entities

import com.google.firebase.Timestamp
import com.squareup.moshi.Json

data class Jackpot(
    @Json(name = "pot")
    private val pot: Long,
    @Json(name = "timestamp")
    private val timestamp: Timestamp
) {
    companion object {
        fun create(
            pot: Long,
        ) = Jackpot(
            pot,
            timestamp = Timestamp.now()
        )

        fun createEmpty() = Jackpot(
            0,
            Timestamp.now()
        )
    }

    fun getPot(): Long = pot
    fun getTimestamp(): Timestamp = timestamp

    fun setPot(pot: Long): Jackpot = Jackpot(
            pot,
            timestamp = Timestamp.now()
        )

    override fun toString(): String {
        return "Jackpot{pot=$pot, timestamp=$timestamp}"
    }
    fun isEmpty() = pot <= 0L

    fun isValid(): Boolean = !this.isEmpty() && timestamp.seconds > 0L
}