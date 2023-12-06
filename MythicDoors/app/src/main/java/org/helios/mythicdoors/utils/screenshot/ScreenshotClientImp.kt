package org.helios.mythicdoors.utils.screenshot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.utils.locales.Locales
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScreenshotClientImp(): IScreenshot {
    companion object {
        private const val CREATE_IMAGE_FILE = 1
    }

    override suspend fun makeScreenshotFile(
        context: Context,
        activity: Activity,
        bitmap: Bitmap
    ): Uri? = withContext(Dispatchers.IO) {

        val initialUri: Uri = getRootUri(context)
        val filename= "mythic-doors-${getDate()}.jpeg"
        val screenshotsDir = createScreenshotDirectory(context)
        val bitmapFile = File(screenshotsDir, filename)

        return@withContext try {
            val out = FileOutputStream(bitmapFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
            out.flush()
            out.close()

            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                bitmapFile
            )


            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/jpeg"
                putExtra(Intent.EXTRA_TITLE, filename)
                putExtra(Intent.EXTRA_STREAM, contentUri)
            }

            activity.startActivityForResult(intent, CREATE_IMAGE_FILE)
            contentUri
        } catch (e: Exception) {
            throw IScreenshot.ScreenshotException("Error getting screenshot path: ${e.message}")
        }
    }

    private fun getRootUri(
        context: Context
    ): Uri {
        return Uri.parse(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path)
    }

    private fun getDate(): String {
        val dateFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss", Locales.spainLocale) }
        return LocalDateTime.now().format(dateFormatter)
    }

    private fun createScreenshotDirectory(context: Context): File {

        val screenshotsDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "screenshots")
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs()
        }
        return screenshotsDir
    }
}