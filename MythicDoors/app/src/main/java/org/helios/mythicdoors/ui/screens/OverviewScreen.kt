package org.helios.mythicdoors.ui.screens

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
<<<<<<< HEAD
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
=======
import androidx.compose.runtime.*
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
<<<<<<< HEAD
import org.helios.mythicdoors.services.interfaces.LanguageChangeListener
import org.helios.mythicdoors.services.location.LocationService
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.ui.fragments.AudioPlayer
import org.helios.mythicdoors.ui.fragments.MenuBar
=======
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.OVERVIEW_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.SOUND_MANAGEMENT_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.lenguage
import org.helios.mythicdoors.viewmodel.OverviewScreenViewModel
import org.helios.mythicdoors.viewmodel.tools.SoundManagementViewModel


<<<<<<< HEAD

=======
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
@Composable
fun OverviewScreen(navController: NavController) {
    val controller: OverviewScreenViewModel = (MainActivity.viewModelsMap[OVERVIEW_SCREEN_VIEWMODEL] as OverviewScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
<<<<<<< HEAD
    val context: Context = LocalContext.current
=======

>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
    val soundManager: SoundManagementViewModel = (MainActivity.viewModelsMap[SOUND_MANAGEMENT_SCREEN_VIEWMODEL] as SoundManagementViewModel)
        .apply { loadSoundsIfNeeded() }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.nocheoverviewscreen) )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    DisposableEffect(Unit) {
        onDispose {
            soundManager.stopPlayingSounds()
        }
    }

    soundManager.playSoundInLoop(R.raw.rain)

    var currentLanguage by remember { mutableStateOf("en") }
    val storeManager = StoreManager.getInstance()

    DisposableEffect(Unit) {
        val observer: LanguageChangeListener = object : LanguageChangeListener {
            override fun onLanguageChanged(newLanguage: String) {
                currentLanguage = newLanguage
            }
        }
        storeManager.addObserver(observer)
        onDispose {
            storeManager.removeObserver(observer)
        }
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.door))
    val progress by animateLottieCompositionAsState(
    composition = composition,
    iterations = 3
    )


    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LottieAnimation(composition = composition, progress = {progress})
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 30.dp))
            Row {
                LottieAnimation(composition = composition, progress = { progress })
                Button(
                    onClick = {
                        soundManager.stopPlayingSounds().also {
                            soundManager.playSound(R.raw.castle_door)
                            controller.navigateToLoginScreen(scope, snackbarHostState)
                        }
                    },
                    modifier = Modifier.padding(top = 10.dp, start = 30.dp, end = 30.dp),
                ) {
<<<<<<< HEAD
                    Text(text = lenguage["play_$currentLanguage"] ?: "Play",
=======
                    Text(text = stringResource(id = R.string.play_button),
>>>>>>> cf39ee32cc3e08e3b52c21d1919e1a3f373d3f67
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
    Image(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(top = 351.dp),
    painter = painterResource(id = R.drawable.castillotest4),
    contentDescription = "Main image of the game app, a gothic castle.",
    )
}