package org.helios.mythicdoors.ui.fragments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.entities.Song
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.AUDIO_PLAYER_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.tools.AudioPlayerViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AudioPlayer() {
    val controller: AudioPlayerViewModel = (MainActivity.viewModelsMap[AUDIO_PLAYER_SCREEN_VIEWMODEL] as AudioPlayerViewModel)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val currentSong: LiveData<Song> = controller.actualSong
    LaunchedEffect(currentSong) {
        controller.actualSong.value ?: return@LaunchedEffect
    }

    controller.playInGameMusic(context, scope, snackbarHostState)

    Box{
        Row(modifier = Modifier
            .height(25.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { controller.playMusic(context, scope, snackbarHostState) }) {
                Icon(
                    painter = painterResource(id = R.drawable.play_500),
                    contentDescription = "Play button for the audio player.",
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = { controller.pauseMusic() }) {
                Icon(
                    painter = painterResource(id = R.drawable.pause_500),
                    contentDescription = "Pause button for the audio player.",
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                modifier = Modifier
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        animationMode = MarqueeAnimationMode.Immediately,
                        delayMillis = 1000,
                        initialDelayMillis = 1000,
                        spacing = MarqueeSpacing(10.dp),
                        velocity = 100.dp
                ),
                text = "${currentSong.value?.name} by ${currentSong.value?.artist}" ?: "No song selected",
                style = MaterialTheme.typography.labelSmall,
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun AudioPlayerPreview() {
    AudioPlayer()
}