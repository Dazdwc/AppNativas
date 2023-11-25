package org.helios.mythicdoors.utils.screenshot

import android.app.Activity
import android.graphics.Bitmap
import android.view.View

interface IScreenshot {
    suspend fun takeScreenshot(view: View, activity: Activity): Boolean

    class ScreenshotException(message: String): Exception(message)
}