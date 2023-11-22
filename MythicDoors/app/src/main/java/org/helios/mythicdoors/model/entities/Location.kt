package org.helios.mythicdoors.model.entities

import org.helios.mythicdoors.model.entities.CoordinateValues.Companion.LATITUDE
import org.helios.mythicdoors.model.entities.CoordinateValues.Companion.LONGITUDE

data class Location(
    private val id: Long? = null,
    private val user: User,
    private val latitude: Double,
    private val longitude: Double,
) {
    val coordinates: Map<String, Double> = mapOf(LATITUDE to latitude, LONGITUDE to longitude)

    fun getId(): Long? { return id }
    fun getUser(): User { return user }
}

class CoordinateValues {
    companion object {
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }
}
