package org.helios.mythicdoors.utils.calendar

import android.content.Context
import android.util.Log
import org.helios.mythicdoors.model.entities.Location

class CalendarService(
    context: Context,
    location: Location
) {
    private val calendarCliente: ICalendarClient = CalendarClientImp(context)
    private val event: Event by lazy { createEvent(location) }

    suspend fun insertEvent(): Boolean {
        return try {
            return calendarCliente.insertEvent(event)
        } catch (e: ICalendarClient.CalendarException) {
            Log.e("CalendarService", "Error inserting event: ${e.message}")
            false
        }
    }

    private fun createEvent(
        location: Location
    ): Event {
        return Event.create(
            "Mythic Doors",
            "Mythic Doors: You has won a game!",
            location.toString()
        )
    }
}