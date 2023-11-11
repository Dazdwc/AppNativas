package org.helios.mythicdoors

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.AppNavigation
import org.helios.mythicdoors.services.UserServiceImp
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.ui.theme.MythicDoorsTheme
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels
import org.helios.mythicdoors.utils.Connection
import org.helios.mythicdoors.viewmodel.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private lateinit var appContext: Context

        private val dbHelper: Connection by lazy { Connection(appContext) }

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
                // ...otros view models que dependen de dataController
            }
            map.toMap()
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
        dbHelper.close()
    }
}

@Composable
fun dbTest(dbHelper: Connection) {
    var name: String = "Dboj"
    var email: String = "dboj-test@helios.com"
    var pass: String = "1234"
    val userServiceImpl: UserServiceImp = UserServiceImp(dbHelper)
    val scope = rememberCoroutineScope()
    var userList: List<User> = mutableListOf()

    Column {
        Button(onClick = {
            scope.launch {
                if (userServiceImpl.saveUser(User.create(name, email, pass))) Log.w("DB", "User saved!")
                else Log.w("DB", "User not saved!")
                userList = userServiceImpl.getUsers() ?: mutableListOf()
                Log.w("DB", "User list: $userList")
            }

        }) {

        }
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