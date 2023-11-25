package org.helios.mythicdoors.utils.screenshot

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.utils.locales.Locales
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScreenshotClientImp(): IScreenshot {
    override suspend fun takeScreenshot(
        view: View,
        activity: Activity,
    ): Boolean {
        activity.window?.let { window ->
            val imageFile: File = getScreenshotFile(context = activity)
            val outputStream = FileOutputStream(imageFile)
            val screenshot: Bitmap? =  recordScreen(view)

            return try {
                withContext(Dispatchers.IO) {
                    screenshot?.let {
                        PixelCopy.request(window, screenshot, { copyResult ->
                            if (copyResult == PixelCopy.SUCCESS) {
                                saveScreenshot(screenshot, outputStream)
                            }
                        }, Handler(Looper.getMainLooper()))
                    }
                }
                true
            } catch (e: Exception) {
                throw IScreenshot.ScreenshotException("Error taking screenshot: ${e.message}")
            }
        } ?: throw IScreenshot.ScreenshotException("Error taking screenshot: Window is null")
    }

    private fun getDate(): String {
        val dateFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern("dd/MM/yyyy_hh:ss", Locales.spainLocale) }
        return LocalDate.now().format(dateFormatter)
    }

    private fun getScreenshotFile(context: Context): File {
        val date = getDate()
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let { file ->
            return File(file, "mythic-doors-${date}.jpeg")
        } ?: throw IScreenshot.ScreenshotException("Error getting screenshot path")
    }

    private fun recordScreen(view: View): Bitmap? {
        return try {
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        } catch (e: Exception) {
            throw IScreenshot.ScreenshotException("Error recording screen: ${e.message}")
        }
    }

    private fun saveScreenshot(bitmap: Bitmap, outputStream: FileOutputStream) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        outputStream.flush()
        outputStream.close()
    }
}