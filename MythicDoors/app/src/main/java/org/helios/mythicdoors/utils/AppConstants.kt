package org.helios.mythicdoors.utils

import org.helios.mythicdoors.R
import org.helios.mythicdoors.navigation.AppDestination

object AppConstants {
    const val APP_NAME = "Mythic Doors"
    const val DATABASE_NAME = "mythic_doors_db"
    const val DATABASE_VERSION = 1

    const val EASY_DOOR = "easy"
    const val AVERAGE_DOOR = "medium"
    const val HARD_DOOR = "hard"

    const val ENEMY_FRANKY = "Franky"
    const val ENEMY_JACKO = "Jack O' Lantern"
    const val ENEMY_MUMMY = "Imhotep"
    const val ENEMY_REAPY = "The Reaper"
    const val ENEY_WOLFIE = "Wolfie"

    const val INITIAL_COINS_AMOUNT = 200

    object Screens {
        const val OVERVIEW_SCREEN = "overview_screen"
        const val LOGIN_SCREEN = "login_screen"
        const val REGISTER_SCREEN = "register_screen"
        const val GAME_OPTS_SCREEN = "game_opts_screen"
        const val GAME_ACTION_SCREEN = "game_action_screen"
        const val ACTION_RESULT_SCREEN = "action_result_screen"
        const val SCORES_SCREEN = "scores_screen"
    }

    object ScreensViewModels {
        const val MAINACTIVITY_SCREEN_VIEWMODEL = "mainactivity-screen-viewmodel"
        const val OVERVIEW_SCREEN_VIEWMODEL = "overview-screen-viewmodel"
        const val LOGIN_SCREEN_VIEWMODEL = "login-screen-viewmodel"
        const val REGISTER_SCREEN_VIEWMODEL = "register-screen-viewmodel"
        const val GAME_OPTS_SCREEN_VIEWMODEL = "game-opts-screen-viewmodel"
        const val GAME_ACTION_SCREEN_VIEWMODEL = "game-action-screen-viewmodel"
        const val ACTION_RESULT_SCREEN_VIEWMODEL = "action-result-screen-viewmodel"
        const val SCORES_SCREEN_VIEWMODEL = "scores-screen-viewmodel"
        const val MENU_BAR_SCREEN_VIEWMODEL = "menu-bar-screen-viewmodel"
        const val AUDIO_PLAYER_SCREEN_VIEWMODEL = "audio-player-screen-viewmodel"
        const val SOUND_MANAGEMENT_SCREEN_VIEWMODEL = "sound-management-screen-viewmodel"
    }

    enum class GameMode {
        SINGLE_PLAYER,
        MULTI_PLAYER
    }

    object ScreenConstants {
        const val IMAGE_HEIGHT = 90
        const val SMALL_IMAGE_HEIGHT = 90
        const val BET_BLOCK_WIDTH_REDUCER = 100
        const val AVERAGE_PADDING = 15
        const val DOUBLE_PADDING = 30
        const val BUTTON_WIDTH = 200
        const val MENU_HEIGHT = 100
    }
}