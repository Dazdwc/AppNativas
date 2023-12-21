package org.helios.mythicdoors

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.AppNavigation
import org.helios.mythicdoors.presentation.sign_in.GoogleAuthUiClient
import org.helios.mythicdoors.services.location.LocationService
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.ui.fragments.*
import org.helios.mythicdoors.ui.screens.SplashScreen
import org.helios.mythicdoors.ui.theme.MythicDoorsTheme
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels
import org.helios.mythicdoors.utils.connection.Connection
import org.helios.mythicdoors.utils.permissions.AppPermissionsRequests
import org.helios.mythicdoors.utils.permissions.PermissionsTextProviders
import org.helios.mythicdoors.viewmodel.*
import org.helios.mythicdoors.viewmodel.tools.*

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MainActivity : ComponentActivity() {
    companion object {
        private lateinit var appContext: Context

        private val dbHelper: Connection by lazy { Connection(appContext) }
        private val navController: NavController by lazy { NavController(appContext) }
        private val snackbarHostState: SnackbarHostState by lazy { SnackbarHostState() }
        lateinit var dataController: DataController

        /* DataController will initialize as a Singleton, so it will be initialized only once
        * and will be available for all the view models that need it
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
                map[ScreensViewModels.GAME_GUIDE_SCREEN_VIEWMODEL] = GameGuideWebViewViewModel()
                map[ScreensViewModels.MENU_BAR_SCREEN_VIEWMODEL] = MenuViewModel(it)
                map[ScreensViewModels.AUDIO_PLAYER_SCREEN_VIEWMODEL] = AudioPlayerViewModel.getInstance(it)
                map[ScreensViewModels.SOUND_MANAGEMENT_SCREEN_VIEWMODEL] = SoundManagementViewModel.getInstance()
                map[ScreensViewModels.LANGUAGE_MANAGER_SCREEN_VIEWMODEL] = LanguageManagerViewModel.getInstance()
            }
            map.toMap()
        }

        fun getContext(): Context = appContext

        fun setContext(context: Context) {
            appContext = context
            dataController = DataController.getInstance(dbHelper)
        }
    }

    private val controller: MainActivityViewModel by lazy {
        (viewModelsMap[ScreensViewModels.MAINACTIVITY_SCREEN_VIEWMODEL] as MainActivityViewModel).apply { setNavController(navController) }
    }

    private val storeManager: StoreManager by lazy {
        StoreManager.getInstance()
    }

    private val activity = this

    private val googleAuthClient: GoogleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContext(context = applicationContext)
        storeManager.setContext(context = applicationContext)
        storeManager.updateActualUser(User.createEmptyUser())

        startLocationService()

        lifecycleScope.launch {
            Firebase.initialize(this@MainActivity)
            Firebase.appCheck
                .installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance()
                )
        }

        setContent {
            val isSplashScreenVisible = remember { mutableStateOf(true) }
            val isGoogleSignInRequested by  MainActivityViewModel.GoogleSignInRequestSetter.isGoogleSignInRequested.observeAsState(initial = false)

            if (isSplashScreenVisible.value) {
                SplashScreen(
                    onTimeout = {
                        isSplashScreenVisible.value = false
                    }
                )
            } else {
                MythicDoorsTheme {
                    val dialogQueue = controller.visiblePermissionDialogQueue

                    val foregroundPermissionLauncher =
                        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
                            onResult = { isGranted ->
                                controller.onPermissionResult(
                                    permission = Manifest.permission.FOREGROUND_SERVICE,
                                    isGranted = isGranted
                                )
                            }
                        )

                    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions(),
                        onResult = { permissions ->
                            AppPermissionsRequests.appPermissionRequests.forEach { permission ->
                                controller.onPermissionResult(
                                    permission = permission,
                                    isGranted = permissions[permission] == true
                                )
                            }
                        }
                    )

                    dialogQueue
                        .reversed()
                        .forEach { permission ->
                            PermissionDialog(
                                permission = permission,
                                permissionTextProvider = choosePermissionTextProvider(permission) ?: return@forEach,
                                isPermanentlyDecline = !shouldShowRequestPermissionRationale(permission),
                                onDismiss = controller::dismissDialog,
                                onOkClick = {
                                    controller.dismissDialog()
                                    multiplePermissionResultLauncher.launch(arrayOf(permission))
                                },
                                onSettingsClick = ::openAppSettings,
                            )
                        }

                    Scaffold(topBar = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AudioPlayer()
                            Spacer(modifier = Modifier.width(8.dp))
                            LanguageManager(activity = activity, activityContext = activity.baseContext)
                        }
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
                            foregroundPermissionLauncher.launch(Manifest.permission.FOREGROUND_SERVICE)
                            multiplePermissionResultLauncher.launch(AppPermissionsRequests.appPermissionRequests)

                            val isLoading by controller.isLoading.observeAsState(initial = false)
                            LoadingIndicator(
                                isLoading = isLoading
                            )

                            if (isGoogleSignInRequested) {
                                val viewModel = viewModel<SignInViewModel>()
                                val state by viewModel.state.collectAsStateWithLifecycle()

                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult= {result ->
                                        if (result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult = googleAuthClient.signInWithIntent(
                                                    intent = result.data ?: return@launch
                                                )

                                                viewModel.onSignInResult(signInResult)
                                            }
                                        }
                                    }
                                )

                                LaunchedEffect(key1 = state.isSignInSuccessful) {
                                    if (state.isSignInSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Sign in successful!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        controller.updateLogedUser(
                                            googleAuthUiClient = googleAuthClient
                                        )

                                        isGoogleSignInRequested.let { MainActivityViewModel.GoogleSignInRequestSetter.isGoogleSignInRequested.value = false }
                                    }
                                }

                                Dialog(
                                    onDismissRequest = {
                                        MainActivityViewModel.GoogleSignInRequestSetter.isGoogleSignInRequested.value = false
                                        viewModel.resetSignInState()
                                    },
                                    properties = DialogProperties(
                                        dismissOnBackPress = true,
                                        dismissOnClickOutside = true,
                                        usePlatformDefaultWidth = true,
                                    )
                                ) {
                                    Box(modifier = Modifier
                                        .height(R.dimen.dialog_height.dp)
                                        .background(MaterialTheme.colorScheme.background)
                                    ){
                                        Column(modifier = Modifier
                                            .fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            GoogleSignInScreen(
                                                controller = controller,
                                                state = state
                                            ) {
                                                lifecycleScope.launch {
                                                    val signInIntentSender = googleAuthClient.signIn()
                                                    launcher.launch(
                                                        IntentSenderRequest.Builder(
                                                            signInIntentSender ?: return@launch
                                                        ).build()
                                                    )
                                                }
                                            }
                                        }

                                    }
                                }
                            }

                            AppNavigation()
                        }
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun choosePermissionTextProvider(permission: String): IPermissionTextProvider? {
    val permissionsMap = PermissionsTextProviders.permissionsTextProviders

    return permissionsMap[permission]
}

private fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun startLocationService() {
    try {
        Intent(MainActivity.getContext(), LocationService::class.java).also {
            MainActivity.getContext().startService(it)
        }
    } catch (e: Exception) {
        Log.e("MainActivity", "startLocationService: ${e.message}")
    }
}