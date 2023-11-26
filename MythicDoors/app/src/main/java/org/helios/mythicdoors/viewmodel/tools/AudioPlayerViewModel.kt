package org.helios.mythicdoors.viewmodel.tools

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.Song
import org.helios.mythicdoors.store.StoreManager

class AudioPlayerViewModel(
    dataController: DataController
): ViewModel() {
    // Usamos un Singleton para que se reproduzca la misma canción en todas las pantallas
    companion object {
        @Volatile
        private var instance : AudioPlayerViewModel? = null

        fun getInstance(dataController: DataController): AudioPlayerViewModel {
            return instance ?: synchronized(this) {
                instance ?: buildAudioPlayerViewModel(dataController).also { instance = it }
            }
        }

        private fun buildAudioPlayerViewModel(dataController: DataController): AudioPlayerViewModel {
            return AudioPlayerViewModel(dataController)
        }
    }

    private val store: StoreManager by lazy { StoreManager.getInstance() }
    private val context: Context? by lazy { store.getContext() }

    private val gameSongsList: List<Song> by lazy { getSongList() }
    private val player: GameMediaPlayer? by lazy { GameMediaPlayer.getInstance() }

    private val audioManager: AudioManager by lazy { context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager }

    private val isPaused: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    private val isPlaying: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val actualSong: MutableLiveData<Song> by lazy { MutableLiveData<Song>() }

    init {
        Log.e("AudioPlayerViewModel", "init: ${context.toString()}")
        setupAudioFocusListener()
    }

    // Implementando recursividad podemos reproducir una lista de canciones en bucle
    fun playInGameMusic(context: Context, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        if (gameSongsList.isEmpty()) makeSnackBar(scope, snackbarHostState, "No songs found").also { return }

        actualSong.value = gameSongsList[GameMediaPlayer.currentSongIndex]

        scope.launch {
            try {
                player?.apply {
                    setDataSource(context, gameSongsList[GameMediaPlayer.currentSongIndex].path)
                    prepare()
                    start()
                }.let { isPlaying.value = true }

                player?.setOnCompletionListener {
                    player?.reset()
                    isPlaying.value = false
                    GameMediaPlayer.currentSongIndex = (GameMediaPlayer.currentSongIndex + 1) % gameSongsList.size
                    playInGameMusic(context, scope, snackbarHostState)
                }
            } catch (e: Exception) {
                Log.e("AudioPlayerViewModel", "playInGameMusic: ${e.printStackTrace()}")
                makeSnackBar(scope, snackbarHostState, "Error playing song").also { return@launch }
            }
        }
    }

    fun pauseMusic() {
        if (isPlaying.value == true) {
            player?.pause()
            isPaused.value = true
        }
    }

    fun playMusic(context: Context, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        if (player == null )  if (!isPlaying.value!!) playInGameMusic(context, scope, snackbarHostState).also {
            isPaused.value = false
            return
        }
        if (isPaused.value == true) {
            player?.start()
            isPaused.value = false
        }
    }

    private fun getSongList(): List<Song> {
        return try {
            store.getAppStore().gameSongsList
        } catch (e: Exception) {
            Log.e("AudioPlayerViewModel", "getSongList: $e")
            listOf()
        }
    }

    private fun makeSnackBar(scope: CoroutineScope, snackbarHostState: SnackbarHostState, message: String) {
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
    }

    private fun setupAudioFocusListener() {
        val audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setOnAudioFocusChangeListener { focusChange ->
                when (focusChange) {
                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                        player?.setVolume(0.3f, 0.3f)
                    }

                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                        player?.pause()
                    }

                    AudioManager.AUDIOFOCUS_GAIN -> {
                        player?.setVolume(1f, 1f)
                        player?.start()
                    }

                    AudioManager.AUDIOFOCUS_LOSS -> {
                        player?.stop()
                        player?.release()
                    }
                }
            }
            .build()

        audioManager.requestAudioFocus(audioFocusRequest)
    }
}
// Inner class -> Solo la usmos aquí, por eso la ponemos como inner
class GameMediaPlayer: MediaPlayer() {
    companion object {
        @Volatile
        private var instance : GameMediaPlayer? = null
        var currentSongIndex: Int = 0

        fun getInstance(): GameMediaPlayer {
            return instance ?: synchronized(this) {
                instance ?: buildGameMediaPlayer().also { instance = it }
            }
        }

        private fun buildGameMediaPlayer(): GameMediaPlayer {
            return GameMediaPlayer()
        }

        fun resetMediaPlayer() {
                instance?.reset()
                instance?.release()
                instance = null
            }
        }
}