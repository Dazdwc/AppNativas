package org.helios.mythicdoors.store

import android.Manifest
import android.content.Context
import android.net.Uri
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.entities.Enemy
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.model.entities.Song
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.AppConstants.Languages
import org.helios.mythicdoors.utils.AppConstants.GameMode
import org.helios.mythicdoors.utils.AppConstants.AuthType
import org.helios.mythicdoors.utils.typeclass.Language
import java.lang.ref.WeakReference

data class AppStore(
    val combatResults: CombatResults = CombatResults(),
    val playerAction: PlayerAction = PlayerAction(),
    val playerInitialStats: PlayerInitialStats = PlayerInitialStats(),
    var contextReference: WeakReference<Context>? = null,
    var actualUser: User? = null,
    var gameMode: GameMode = GameMode.SINGLE_PLAYER,
    var gameScore: Long = 0L,
    var userLocation: Location = Location.createEmptyLocation(),
    var authType: AuthType = AuthType.DEFAULT,
    val gameSongsList: List<Song> = listOf(
        Song.create(R.raw.guardians_of_the_sword, "Guardians of The Sword", "Dark Fantasy Studio", Uri.parse("android.resource://org.helios.mythicdoors/" + R.raw.guardians_of_the_sword)),
        Song.create(R.raw.the_girl_and_the_sword, "The Girl and The Sword", "Dark Fantasy Studio", Uri.parse("android.resource://org.helios.mythicdoors/" + R.raw.the_girl_and_the_sword)),
        Song.create(R.raw.i_feel_the_power, "I Feel The Power", "Dark Fantasy Studio", Uri.parse("android.resource://org.helios.mythicdoors/" + R.raw.i_feel_the_power)),
    ),
    val gameSoundsList: List<Int> = listOf(
        R.raw.door_open,
        R.raw.wolf,
        R.raw.werewolf,
        R.raw.castle_door,
        R.raw.rain,
        R.raw.scores_screen_sound,
        R.raw.door_select,
    ),
    val languages: Map<String, Language> = LanguagesMap,
)

data class CombatResults(
    var isPlayerWinner: Boolean = false,
    var enemy: Enemy? = null,
    var resultCoinAmount: Long = 0,
    var resultXpAmount: Long = 0
)

data class PlayerAction(
    var bet : Int = 0,
    var selectedDoorId: String = ""
)

data class PlayerInitialStats(
    var level: Long = 1,
    var experience: Long = 0,
    var coins: Long = 0,
    var score: Long = 0
)

object LanguagesMap: Map<String, Language> by mapOf(
    Languages.ENGLISH to Language.ENGLISH,
    Languages.SPANISH to Language.SPANISH,
    Languages.CATALAN to Language.CATALAN
)


class StoreManager {
    private var appStore: AppStore = AppStore()

    /* Patrón Singleton */
    companion object {
        @Volatile
        private var instance : StoreManager? = null

        fun getInstance(): StoreManager {
            return instance ?: synchronized(this) {
                instance ?: buildStoreManager().also { instance = it }
            }
        }

        private fun buildStoreManager(): StoreManager {
            return StoreManager()
        }
    }

    fun getAppStore(): AppStore { return appStore }

    fun getContext(): Context? { return appStore.contextReference?.get() }

    fun setContext(context: Context) { appStore.contextReference = WeakReference(context) }

    fun releaseContext() { appStore.contextReference?.clear().also { appStore.contextReference = null } }

    fun updateCombatResults(
        isPlayerWinner: Boolean,
        enemy: Enemy?,
        resultCoinAmount: Long,
        resultXpAmount: Long
    ) {
        appStore.combatResults.isPlayerWinner = isPlayerWinner
        appStore.combatResults.enemy = enemy
        appStore.combatResults.resultCoinAmount = resultCoinAmount
        appStore.combatResults.resultXpAmount = resultXpAmount
    }

    fun updatePlayerAction(bet: Int, selectedDoorId: String) {
        appStore.playerAction.bet = bet
        appStore.playerAction.selectedDoorId = selectedDoorId
    }

    fun updateActualUser(user: User) { appStore.actualUser = user }

    fun updateGameMode(gameMode: String) { (gameMode == GameMode.MULTI_PLAYER.toString()).let { if(it) appStore.gameMode = GameMode.MULTI_PLAYER else GameMode.SINGLE_PLAYER } }

    fun updateOriginalPlayerStats(user: User?) {
        appStore.playerInitialStats.level = user?.getLevel() ?: 1
        appStore.playerInitialStats.experience = user?.getExperience() ?: 0
        appStore.playerInitialStats.coins = user?.getCoins() ?: 0
        appStore.playerInitialStats.score = user?.getScore() ?: 0
    }

    fun updatePlayerCoins(coins: Long) = appStore.actualUser?.setCoins(coins)

    fun updateGameScore(score: Long) { appStore.gameScore = score }

    fun clearCombatResults() {
        appStore.combatResults.isPlayerWinner = false
        appStore.combatResults.enemy = null
        appStore.combatResults.resultCoinAmount = 0
        appStore.combatResults.resultXpAmount = 0
    }

    fun clearCombatData() {
        clearCombatResults()
        clearPlayerAction()
    }

    fun logout() { appStore.actualUser = null }

    fun resetPlayerCoins() { appStore.actualUser?.setCoins(100) }

    fun getLocation(): Location { return appStore.userLocation }

    fun setLocation(location: Location) { appStore.userLocation = location }

    private fun clearPlayerAction() {
        appStore.playerAction.bet = 0
        appStore.playerAction.selectedDoorId = ""
    }

    fun getLanguage(languageName: String): Language? { return appStore.languages[languageName] }

    fun getLanguages(): Map<String, Language> { return appStore.languages }

    fun getAuthType(): AuthType { return appStore.authType }
    fun setAuthType(authType: AuthType) { appStore.authType = authType }
}