package org.helios.mythicdoors

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.helios.mythicdoors.store.StoreManager

/* Esta clase permite la futura implementación de DI.
* La anotación @HiltAndroidApp permite que Hilt genere un contenedor de dependencias para la app.
* Este contenedor se puede usar en toda la app para inyectar dependencias.
* Para ello, hay que anotar la clase con @AndroidEntryPoint.
*/
@HiltAndroidApp
class App(): Application() {}
