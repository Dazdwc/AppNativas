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
import org.helios.mythicdoors.navigation.AppNavigation
import org.helios.mythicdoors.ui.theme.MythicDoorsTheme
import org.helios.mythicdoors.utils.Connection
import org.helios.mythicdoors.viewmodel.*

class MainActivity : ComponentActivity() {

    companion object {
        private lateinit var appContext: Context

        private val dbHelper: Connection by lazy {
            Connection(appContext)
        }

        /* DataController se inicia con el patrón Singleton */
        private val dataController: DataController by lazy {
            DataController.getInstance(dbHelper)
        }

        val viewModelsMap: Map<String, Any> by lazy {
            mapOf(
                "mainactivity-screen-viewmodel" to MainActivityViewModel(dataController),
                "overview-screen-viewmodel" to OverviewScreenViewModel(dataController),
                "action-result-screen-viewmodel" to ActionResultScreenViewModel(dataController),
                "game-action-screen-viewmodel" to GameActionScreenViewModel(dataController),
                "game-opts-screen-viewmodel" to GameOptsScreenViewModel(dataController),
                "login-screen-viewmodel" to LoginScreenViewModel(dataController),
                "register-screen-viewmodel" to RegisterScreenViewModel(dataController),
                "scores-screen-viewmodel" to ScoresScreenViewModel(dataController)
            )
        }

        fun setContext(context: Context) { appContext = context }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContext(applicationContext)

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