package org.helios.mythicdoors.utils.screenshot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.utils.AppConstants.NotificationChannels.IMAGES_NOTIFICATION_CHANNEL
import org.helios.mythicdoors.utils.locales.Locales
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScreenshotService(
    view: View,
    activity: Activity
) {
    companion object {
        fun build(view: View, activity: Activity): ScreenshotService {
            return ScreenshotService(view, activity)
        }

        private val screenshotClient: IScreenshot = ScreenshotClientImp()

        suspend fun makeScreenshotFile(
            context: Context,
            activity: Activity,
            bitmap: Bitmap
        ): Uri? = withContext(Dispatchers.IO) {
            return@withContext try {
                screenshotClient.makeScreenshotFile(
                    context = context,
                    activity = activity,
                    bitmap = bitmap)
            } catch (e: Exception) {
                Log.e("ScreenshotClientImp", "Error getting screenshot path: ${e.message}")
                null
            }
        }
    }
}