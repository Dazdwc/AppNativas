package org.helios.mythicdoors.utils.screenshot

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.View
import org.helios.mythicdoors.utils.locales.Locales
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface IScreenshot {
    suspend fun makeScreenshotFile(
        context: Context,
        activity: Activity,
        bitmap: Bitmap
    ): Uri?

    class ScreenshotException(message: String): Exception(message)
}