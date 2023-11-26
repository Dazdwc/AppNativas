package org.helios.mythicdoors

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.AppNavigation
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.ui.fragments.AudioPlayer
import org.helios.mythicdoors.ui.fragments.MenuBar
import org.helios.mythicdoors.ui.theme.MythicDoorsTheme
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels
import org.helios.mythicdoors.utils.Connection
import org.helios.mythicdoors.viewmodel.*
import org.helios.mythicdoors.viewmodel.tools.AudioPlayerViewModel
import org.helios.mythicdoors.viewmodel.tools.GameMediaPlayer
import org.helios.mythicdoors.viewmodel.tools.SoundManagementViewModel



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private lateinit var appContext: Context

        private val dbHelper: Connection by lazy { Connection(appContext) }
        private val navController: NavController by lazy { NavController(appContext) }
        private val snackbarHostState: SnackbarHostState by lazy { SnackbarHostState() }
        lateinit var dataController: DataController

        /* DataController se iniciará con el patrón Singleton
        * Hay que asegurarse que no se inicialice hasta que no se haya inicializado dataController
        */
        val viewModelsMap: Map<String, Any> by lazy {
            val map = mutableMapOf<String, Any>()
            dataController.let {
                map[ScreensViewModels.MAINACTIVITY_SCREEN_VIEWMODEL] = MainActivityViewModel(it)
                map[ScreensViewModels.OVERVIEW_SCREEN_VIEWMODEL] = OverviewScreenViewModel(it)
                map[ScreensViewModels.ACTION_RESULT_SCREEN_VIEWMODEL] = ActionResultScreenViewModel(it)
                map[ScreensViewModels.GAME_ACTION_SCREEN_VIEWMODEL] = GameActionScreenViewModel(it)
                map[ScreensViewModels.GAME_OPTS_SCREEN_VIEWMODEL] = GameOptsScreenViewModel(it)
                map[ScreensViewModels.LOGIN_SCREEN_VIEWMODEL] = LoginScreenViewModel(it)
                map[ScreensViewModels.REGISTER_SCREEN_VIEWMODEL] = RegisterScreenViewModel(it)
                map[ScreensViewModels.SCORES_SCREEN_VIEWMODEL] = ScoresScreenViewModel(it)
                map[ScreensViewModels.MENU_BAR_SCREEN_VIEWMODEL] = MenuViewModel(it)
                map[ScreensViewModels.AUDIO_PLAYER_SCREEN_VIEWMODEL] = AudioPlayerViewModel.getInstance(it)
                map[ScreensViewModels.SOUND_MANAGEMENT_SCREEN_VIEWMODEL] = SoundManagementViewModel.getInstance()
                // ...otros view models que dependen de dataController
            }
            map.toMap()
        }

        fun getContext(): Context {
            return appContext
        }

        fun setContext(context: Context) {
            appContext = context
            dataController = DataController.getInstance(dbHelper)
        }
    }

    private val controller: MainActivityViewModel by lazy {
        viewModelsMap["mainactivity-screen-viewmodel"] as MainActivityViewModel
    }

    private val storeManager: StoreManager by lazy {
        StoreManager.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContext(applicationContext)

        storeManager.setContext(applicationContext)
        storeManager.updateActualUser(User.createEmptyUser())

        setContent {
            MythicDoorsTheme {
                Scaffold(topBar = {
                    AudioPlayer()
                },
                    bottomBar = {
                        MenuBar(navController)
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
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
        GameMediaPlayer.resetMediaPlayer()
        dbHelper.close()
        storeManager.releaseContext()
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