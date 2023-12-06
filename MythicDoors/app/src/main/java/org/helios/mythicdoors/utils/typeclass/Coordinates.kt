package org.helios.mythicdoors.utils.typeclass

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

fun Coordinates.toMap(): Map<String, Double> {
    return mapOf(
        CoordinateValues.LATITUDE to this.latitude,
        CoordinateValues.LONGITUDE to this.longitude
    )
}

fun Map<String, Double>.toCoordinates(): Coordinates {
    return Coordinates(
        this[CoordinateValues.LATITUDE] ?: 0.0,
        this[CoordinateValues.LONGITUDE] ?: 0.0
    )
}

class CoordinateValues {
    companion object {
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }
}