package org.helios.mythicdoors.utils.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.extenssions.hasPostNotificationPermission

class CalendarService(
    context: Context,
    location: Location
) {
    private val calendarCliente: ICalendarClient = CalendarClientImp(context)
    private val event: Event = createEvent(location)
    private val storeManager: StoreManager by lazy { StoreManager.getInstance() }

    suspend fun insertEvent(): Boolean {
        return try {
            return calendarCliente.insertEvent(event)
                .also { if (it) sendOnWinNotification() }
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

    /* We can suppress the Missing Permission Check because we have externalized the logic in a util */
    @SuppressLint("MissingPermission")
    private fun sendOnWinNotification() {
        val context = storeManager.getContext() ?: MainActivity.getContext()
        if (!context.hasPostNotificationPermission()) throw Exception("Post notification permission not granted")

        val notification = createOnCalendarInsertNotification()
        NotificationManagerCompat.from(context).notify(AppConstants.NotificationIds.GAMEWON_NOTIFICATION_ID, notification.build())
    }

    private fun createOnCalendarInsertNotification(): NotificationCompat.Builder {
        val context = storeManager.getContext() ?: MainActivity.getContext()

        return NotificationCompat.Builder(context, AppConstants.NotificationChannels.CALENDAR_NOTIFICATION_CHANNEL)
            .setContentTitle(context.getString(R.string.calendar_notification_title))
            .setContentText(context.getString(R.string.calendar_notification_content))
            .setSmallIcon(R.drawable.clapganlf)
            .setOngoing(true)
    }
}