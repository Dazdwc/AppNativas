package org.helios.mythicdoors.viewmodel.tools

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import androidx.lifecycle.ViewModel
import org.helios.mythicdoors.R
import org.helios.mythicdoors.store.StoreManager

class SoundManagementViewModel(): ViewModel() {
    companion object {
        @Volatile
        private var instance : SoundManagementViewModel? = null

        fun getInstance(): SoundManagementViewModel {
            return instance ?: synchronized(this) {
                instance ?: buildSoundManagementViewModel().also { instance = it }
            }
        }

        private fun buildSoundManagementViewModel(): SoundManagementViewModel {
            return SoundManagementViewModel()
        }
    }

    private val store: StoreManager by lazy { StoreManager.getInstance() }
    private val context: Context? by lazy { store.getContext() }

    private val soundPool: SoundPool
    private val soundMap: MutableMap<Int, Int> = mutableMapOf()

    private var isSoundsLoaded: Boolean = false

    init {
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            . setMaxStreams(2)
            . setAudioAttributes(audioAttributes)
            .build()

        loadListeners()

        try {
            loadSounds(context!!)
        } catch (e: Exception) {
            Log.e("SOUND", "Error loading sounds")
        }
    }

    private fun loadSounds(context: Context) {
        store.getAppStore().gameSoundsList.forEach { sound ->
            val resourceId = soundPool.load(context, sound, 1)
            soundMap[sound] = resourceId
        }

        isSoundsLoaded = true
    }

    private fun loadListeners() {
        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status != 0) {
                Log.e("SoundManagementViewModel", "Error loading sounds: $status")
            }
        }
    }

    fun loadSoundsIfNeeded() {
        if (!isSoundsLoaded) {
            try {
                loadSounds(context!!)
            } catch (e: Exception) {
                Log.e("SOUND", "Error loading sounds")
            }
        }
    }

    fun playSound(sound: Int) {
        val soundId: Int? = soundMap[sound]

        soundId?.let {
            try {
                soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
            } catch (e: Exception) {
                Log.e("SOUND", "Error playing sound")
            }
        }
    }

    fun playSoundInLoop(sound: Int) {
        val soundId: Int? = soundMap[sound]

        soundId?.let {
            try {
                soundPool.play(soundId, 0.7f, 0.7f, 1, -1, 1f)
            } catch (e: Exception) {
                Log.e("SOUND", "Error playing sound")
            }
        }
    }

    fun stopPlayingSounds() {
        soundPool.autoPause()
    }

    override fun onCleared() {
        super.onCleared()
        soundPool.release()
    }
}