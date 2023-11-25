package org.helios.mythicdoors.utils.calendar

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.utils.extenssions.hasCalendarPermission

class CalendarClientImp(
    private val context: Context
): ICalendarClient {
    override suspend fun insertEvent(event: Event): Boolean {
        if (!context.hasCalendarPermission()) throw ICalendarClient.CalendarException("Calendar permission not granted")
        assert(!event.isEmpty()) { "Event is empty" }

//        val intent = Intent(Intent.ACTION_INSERT)
//            .setData(CalendarContract.Events.CONTENT_URI)
//            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getStartTime())
//            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getEndTime())
//            .putExtra(CalendarContract.Events.TITLE, event.getTitle())
//            .putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription())
//            .putExtra(CalendarContract.Events.EVENT_LOCATION, event.getLocation())
//            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)

        val contentValues: ContentValues = getContentValues(event)

        try {
            withContext(Dispatchers.IO) {
                context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues)
//                context.startActivity(intent)
            }

            return true
        } catch (e: Exception) {
            Log.e("CalendarClientImp", "Error inserting event: ${e.message}")
            throw ICalendarClient.CalendarException("Error inserting event")
        }
    }

    private fun getContentValues(event: Event): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(CalendarContract.Events.CALENDAR_ID, 1)
        contentValues.put(CalendarContract.Events.TITLE, event.getTitle())
        contentValues.put(CalendarContract.Events.DESCRIPTION, event.getDescription())
        contentValues.put(CalendarContract.Events.EVENT_LOCATION, event.getLocation())
        contentValues.put(CalendarContract.Events.DTSTART, event.getStartTime())
        contentValues.put(CalendarContract.Events.DTEND, event.getEndTime())
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid")
        contentValues.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
        return contentValues
    }
}