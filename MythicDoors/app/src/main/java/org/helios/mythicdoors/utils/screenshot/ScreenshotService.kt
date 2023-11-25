package org.helios.mythicdoors.utils.screenshot

import android.app.Activity
import android.app.NotificationManager
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import org.helios.mythicdoors.utils.AppConstants.NotificationChannels.IMAGES_NOTIFICATION_CHANNEL

class ScreenshotService(
    private val view: View,
    private val activity: Activity
) {
    private val screenshotClient: IScreenshot = ScreenshotClientImp()

    suspend fun takeScreenshot(): Boolean {
        val notification = createNotification()

        return try {
            if (screenshotClient.takeScreenshot(view, activity)) {
                notification.build().let { it ->
                    activity.getSystemService(Activity.NOTIFICATION_SERVICE)?.let { notificationManager ->
                        (notificationManager as NotificationManager).notify(1, it)
                    }
                }
            }
            true
        } catch (e: IScreenshot.ScreenshotException) {
            Log.e("ScreenshotService", "Error taking screenshot: ${e.message}")
            false
        }
    }

    private fun createNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(activity, IMAGES_NOTIFICATION_CHANNEL)
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setContentTitle("Mythic Doors has taken a screenshot!")
            .setContentText("You has won a game!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }
}