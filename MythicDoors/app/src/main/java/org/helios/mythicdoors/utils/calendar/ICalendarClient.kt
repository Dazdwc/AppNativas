package org.helios.mythicdoors.utils.calendar

interface ICalendarClient {
    suspend fun insertEvent(event: Event): Boolean

    class CalendarException(message: String): Exception(message)
}