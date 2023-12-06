package org.helios.mythicdoors.utils.calendar

import java.time.LocalDateTime

data class Event(
    private val title: String,
    private val description: String,
    private val location: String,
    private var startTime: Long,
    private var endTime: Long
) {


    fun getTitle(): String { return title }
    fun getDescription(): String { return description }
    fun getLocation(): String { return location }
    fun getStartTime(): Long { return startTime }
    fun getEndTime(): Long { return endTime }

    fun setStartTime(time: Long) { this.startTime = time }
    fun setEndTime(time: Long) { this.endTime = time }

    companion object {
        fun create(
            title: String,
            description: String,
            location: String,
        ): Event {
            val zoneOffSet = LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).offset
            return Event(
                title,
                description,
                location,
                LocalDateTime.now().toEpochSecond(zoneOffSet),
                LocalDateTime.now().plusMinutes(1).toEpochSecond(zoneOffSet)
            )
        }
    }

    fun isEmpty(): Boolean {
        return title.isEmpty() && description.isEmpty() && location.isEmpty()
    }
}
