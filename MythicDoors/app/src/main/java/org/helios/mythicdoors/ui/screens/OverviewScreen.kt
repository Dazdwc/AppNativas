package org.helios.mythicdoors.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.ui.fragments.AudioPlayer
import org.helios.mythicdoors.ui.fragments.MenuBar
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.OVERVIEW_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.SOUND_MANAGEMENT_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.OverviewScreenViewModel
import org.helios.mythicdoors.viewmodel.tools.SoundManagementViewModel


@Composable
fun OverviewScreen(navController: NavController) {
    val controller: OverviewScreenViewModel = (MainActivity.viewModelsMap[OVERVIEW_SCREEN_VIEWMODEL] as OverviewScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context: Context = LocalContext.current

    val soundManager: SoundManagementViewModel = (MainActivity.viewModelsMap[SOUND_MANAGEMENT_SCREEN_VIEWMODEL] as SoundManagementViewModel)
        .apply { loadSoundsIfNeeded() }

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
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Mythic Doors",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 30.dp))
            Image(modifier = Modifier
                .padding(top = 50.dp, bottom = 50.dp),
                painter = painterResource(id = R.drawable.castle),
                contentDescription = "Main image of the game app, a gothic castle.",
            )
            Row {
                Button(
                    onClick = {
                        soundManager.stopPlayingSounds().also {
                            soundManager.playSound(R.raw.castle_door)
                            controller.navigateToLoginScreen(scope, snackbarHostState)
                        }
                    },
                    modifier = Modifier.padding(top = 30.dp, start = 30.dp, end = 30.dp),
                ) {
                    Text(text = "Play",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewScreenPreview() {
    OverviewScreen(navController = NavController(LocalContext.current))
}