package org.helios.mythicdoors.utils.notifications

import android.app.Notification
import androidx.core.app.NotificationCompat

interface INotificationFabric {
    fun create(channel: String): Notification
    fun send(notification: Notification)
    fun createNotificationBuilder(channel: String): NotificationCompat.Builder
}