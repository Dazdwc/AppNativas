package org.helios.mythicdoors.model.entities

import org.helios.mythicdoors.utils.typeclass.Coordinates
import java.time.LocalDate

data class Location(
    private val id: Long? = null,
    private val userDocumentId: String?,
    private val latitude: Double,
    private val longitude: Double,
    private val createdAt: LocalDate
) {
    val coordinate: Coordinates = Coordinates(latitude, longitude)

    fun getId(): Long? { return id }
    fun getUserDocumentId(): String? { return userDocumentId }
    fun getLatitude(): Double { return latitude }
    fun getLongitude(): Double { return longitude }
    fun getCreatedAt(): LocalDate { return createdAt }

    companion object {
        fun create(
            userDocumentId: String,
            latitude: Double,
            longitude: Double
        ): Location {
            return Location(
                null,
                userDocumentId,
                latitude,
                longitude,
                createdAt = LocalDate.now()
                )
        }

        fun createEmptyLocation(): Location {
            return Location(
                null,
                null,
                0.0,
                0.0,
                createdAt = LocalDate.now()
            )
        }
    }

    fun isEmpty(): Boolean { return this.id == null }

    fun isValid(): Boolean {
        return  !this.userDocumentId.isNullOrEmpty()
                && !this.latitude.isNaN()
                && !this.longitude.isNaN()
    }
}