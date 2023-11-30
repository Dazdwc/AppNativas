package org.helios.mythicdoors.utils.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants.NotificationChannels
import org.helios.mythicdoors.utils.AppConstants.NotificationIds
import org.helios.mythicdoors.utils.extenssions.hasPostNotificationPermission

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class NotificationFabric {
    companion object: INotificationFabric {
        override fun create(channel: String): Notification {
            return when (channel) {
                NotificationChannels.LOCATION_NOTIFICATION_CHANNEL -> LocationNotification.createNotification().build()
                NotificationChannels.CALENDAR_NOTIFICATION_CHANNEL -> CalendarNotification.createNotification().build()
                NotificationChannels.IMAGES_NOTIFICATION_CHANNEL -> ImagesNotification.createNotification().build()
                NotificationChannels.GAMEWON_NOTIFICATION_CHANNEL -> GameWonNotification.createNotification().build()
                else -> throw Exception("Channel not found")
            }
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun send(notification: Notification) {
            when (notification.channelId) {
                NotificationChannels.LOCATION_NOTIFICATION_CHANNEL -> LocationNotification.sendNotification(notification)
                NotificationChannels.CALENDAR_NOTIFICATION_CHANNEL -> CalendarNotification.sendNotification(notification)
                NotificationChannels.IMAGES_NOTIFICATION_CHANNEL -> ImagesNotification.sendNotification(notification)
                NotificationChannels.GAMEWON_NOTIFICATION_CHANNEL -> GameWonNotification.sendNotification(notification)
                else -> throw Exception("Channel not found")
            }
        }

        override fun createNotificationBuilder(channel: String): NotificationCompat.Builder {
            return when (channel) {
                NotificationChannels.LOCATION_NOTIFICATION_CHANNEL -> LocationNotification.createNotification()
                NotificationChannels.CALENDAR_NOTIFICATION_CHANNEL -> CalendarNotification.createNotification()
                NotificationChannels.IMAGES_NOTIFICATION_CHANNEL -> ImagesNotification.createNotification()
                NotificationChannels.GAMEWON_NOTIFICATION_CHANNEL -> GameWonNotification.createNotification()
                else -> throw Exception("Channel not found")
            }
        }
    }
}

/* We can suppress the Missing Permission Check because we have externalized the logic in a util */
class LocationNotification() {
    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun createNotification(): NotificationCompat.Builder {
            val context = MainActivity.getContext()

            return NotificationCompat.Builder(context, NotificationChannels.LOCATION_NOTIFICATION_CHANNEL)
                .setContentTitle(context.getString(R.string.location_notification_title))
                .setContentText(context.getString(R.string.location_notification_content))
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setOngoing(true)
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun sendNotification(notification: Notification) {
            val context = MainActivity.getContext()
            if (!context.hasPostNotificationPermission()) throw Exception("Post notification permission not granted")

            NotificationManagerCompat.from(context).notify(NotificationIds.LOCATION_NOTIFICATION_ID, notification)
        }
    }
}

class CalendarNotification {
    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun createNotification(): NotificationCompat.Builder {
            val context = MainActivity.getContext()

            return NotificationCompat.Builder(context, NotificationChannels.CALENDAR_NOTIFICATION_CHANNEL)
                .setContentTitle(context.getString(R.string.calendar_notification_title))
                .setContentText(context.getString(R.string.calendar_notification_content))
                .setSmallIcon(android.R.drawable.ic_menu_agenda)
                .setOngoing(true)
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun sendNotification(notification: Notification) {
            val context = MainActivity.getContext()
            if (!context.hasPostNotificationPermission()) throw Exception("Post notification permission not granted")

            NotificationManagerCompat.from(context).notify(NotificationIds.CALENDAR_NOTIFICATION_ID, notification)
        }
    }
}

class ImagesNotification {
    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun createNotification(): NotificationCompat.Builder {
            val context = MainActivity.getContext()

            return NotificationCompat.Builder(context, NotificationChannels.IMAGES_NOTIFICATION_CHANNEL)
                .setContentTitle(context.getString(R.string.screenshot_notification_title))
                .setContentText(context.getString(R.string.screenshot_notification_content))
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setOngoing(true)
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun sendNotification(notification: Notification) {
            val context = MainActivity.getContext()
            if (!context.hasPostNotificationPermission()) throw Exception("Post notification permission not granted")

            NotificationManagerCompat.from(context).notify(NotificationIds.IMAGES_NOTIFICATION_ID, notification)
        }
    }
}

class GameWonNotification {
    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun createNotification(): NotificationCompat.Builder {
            val context = MainActivity.getContext()

            return NotificationCompat.Builder(context, NotificationChannels.GAMEWON_NOTIFICATION_CHANNEL)
                .setContentTitle(context.getString(R.string.win_notification_title))
                .setContentText(context.getString(R.string.win_notification_content))
                .setSmallIcon(R.mipmap.ic_castle_round)
                .setOngoing(true)
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun sendNotification(notification: Notification) {
            val context = MainActivity.getContext()
            if (!context.hasPostNotificationPermission()) throw Exception("Post notification permission not granted")

            NotificationManagerCompat.from(context).notify(NotificationIds.GAMEWON_NOTIFICATION_ID, notification)
        }
    }
}