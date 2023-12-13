package org.helios.mythicdoors

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.debug.internal.DebugAppCheckProvider
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp
import org.helios.mythicdoors.utils.AppConstants.NotificationChannels.CALENDAR_NOTIFICATION_CHANNEL
import org.helios.mythicdoors.utils.AppConstants.NotificationChannels.GAMEWON_NOTIFICATION_CHANNEL
import org.helios.mythicdoors.utils.AppConstants.NotificationChannels.IMAGES_NOTIFICATION_CHANNEL
import org.helios.mythicdoors.utils.AppConstants.NotificationChannels.LOCATION_NOTIFICATION_CHANNEL

/* Esta clase permite la futura implementación de DI.
* La anotación @HiltAndroidApp permite que Hilt genere un contenedor de dependencias para la app.
* Este contenedor se puede usar en toda la app para inyectar dependencias.
* Para ello, hay que anotar la clase con @AndroidEntryPoint.
*/
@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )

        val channelsList: List<String> = listOf(
            LOCATION_NOTIFICATION_CHANNEL,
            CALENDAR_NOTIFICATION_CHANNEL,
            IMAGES_NOTIFICATION_CHANNEL,
            GAMEWON_NOTIFICATION_CHANNEL
        )
        channelsList.forEach { channel -> createNotificationChannel(channel) }
    }

    private fun createNotificationChannel(channel: String) {
        val notificationChannel = NotificationChannel(
            channel,
            channel,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}
