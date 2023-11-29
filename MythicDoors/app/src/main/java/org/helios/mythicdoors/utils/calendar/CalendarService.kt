package org.helios.mythicdoors.utils.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.extenssions.hasPostNotificationPermission

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class CalendarService(
    context: Context,
    location: Location
) {
    private val calendarCliente: ICalendarClient = CalendarClientImp(context)
    private val event: Event = createEvent(location)

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