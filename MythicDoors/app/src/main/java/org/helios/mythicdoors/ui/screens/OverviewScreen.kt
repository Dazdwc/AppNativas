package org.helios.mythicdoors.ui.screens

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.OVERVIEW_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.SOUND_MANAGEMENT_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.OverviewScreenViewModel
import org.helios.mythicdoors.viewmodel.tools.SoundManagementViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OverviewScreen(navController: NavController) {
    val controller: OverviewScreenViewModel = (MainActivity.viewModelsMap[OVERVIEW_SCREEN_VIEWMODEL] as OverviewScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

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
                Button(
                    onClick = {
                        soundManager.stopPlayingSounds().also {
                            soundManager.playSound(R.raw.castle_door)
                            controller.navigateToLoginScreen(scope, snackbarHostState)
                        }
                    },
                    modifier = Modifier.padding(top = 10.dp, start = 30.dp, end = 30.dp),
                ) {
                    Text(text = stringResource(id = R.string.play_button),
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