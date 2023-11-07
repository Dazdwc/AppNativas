package org.helios.mythicdoors

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.AppNavigation
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.ui.theme.MythicDoorsTheme
import org.helios.mythicdoors.utils.Connection
import org.helios.mythicdoors.viewmodel.*

class MainActivity : ComponentActivity() {

    companion object {
        private lateinit var appContext: Context

        private val dbHelper: Connection by lazy {
            Connection(appContext)
        }

        /* DataController se iniciará con el patrón Singleton */
//        private val dataController: DataController by lazy {
//            DataController.getInstance(dbHelper)
//        }
        private var dataController: DataController? = null

        /* Hay que asegurarse que no se inicialice hasta que no se haya inicializado dataController */
        val viewModelsMap: Map<String, Any> by lazy {
            val map = mutableMapOf<String, Any>()
            dataController?.let {
                map["mainactivity-screen-viewmodel"] = MainActivityViewModel(it)
                map["overview-screen-viewmodel"] = OverviewScreenViewModel(it)
                map["action-result-screen-viewmodel"] = ActionResultScreenViewModel(it)
                map["game-action-screen-viewmodel"] = GameActionScreenViewModel(it)
                map["game-opts-screen-viewmodel"] = GameOptsScreenViewModel(it)
                map["login-screen-viewmodel"] = LoginScreenViewModel(it)
                map["register-screen-viewmodel"] = RegisterScreenViewModel(it)
                map["scores-screen-viewmodel"] = ScoresScreenViewModel(it)
                // ...otros view models que dependen de dataController
            }
            map.toMap()
        }

        fun setContext(context: Context) {
            appContext = context
            dataController = DataController.getInstance(dbHelper)
        }
    }

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        viewModelsMap["mainactivity-screen-viewmodel"] as MainActivityViewModel
    }

    private val storeManager: StoreManager by lazy {
        StoreManager.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContext(applicationContext)
        storeManager.updateActualUser(User.createEmptyUser())

        setContent {
            MythicDoorsTheme {
                Scaffold(
                    topBar = {
                        // TODO: Add top bar
                    },
                    bottomBar = {
                        // TODO: Add bottom bar
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    MythicDoorsTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            AppNavigation()
        }
    }
}