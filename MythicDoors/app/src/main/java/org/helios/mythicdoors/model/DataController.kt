package org.helios.mythicdoors.model

import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.services.GameServiceImp
import org.helios.mythicdoors.services.UserServiceImp
import org.helios.mythicdoors.services.interfaces.IGameService
import org.helios.mythicdoors.services.interfaces.IUserService
import org.helios.mythicdoors.utils.Connection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataController @Inject constructor(
    dbHelper: Connection
) {
    private val userService: IUserService = UserServiceImp(dbHelper)
    private val gameService: IGameService = GameServiceImp(dbHelper)

    /*
     * Aplicamos un patrón Singleton para crear el controlador de datos.
     */
    companion object {
        @Volatile
        private var instance: DataController? = null

        fun getInstance(dbHelper: Connection): DataController {
            return instance ?: synchronized(this) {
                instance ?: buildDataController(dbHelper).also { instance = it }
            }
        }

        private fun buildDataController(dbHelper: Connection): DataController {
            return DataController(dbHelper)
        }
    }

    fun getUserService(): IUserService { return userService }
    fun getGameService(): IGameService { return gameService }

    // suspend fun initDataLoader(): Boolean { return defaultDataLoader(dbHelper) }

    suspend fun getAllUsers(): List<User>? { return userService.getUsers() }

    suspend fun getUser(id: Long): User? { return userService.getUser(id = id) }

    suspend fun getLastUser(): User? { return userService.getLastUser() }

    suspend fun saveUser(user: User): Boolean { return userService.saveUser(user) }

    suspend fun deleteUser(id: Long): Boolean { return userService.deleteUser(id) }

    suspend fun countUsers(): Int { return userService.countUsers() }

    /* ... */
    suspend fun getAllGames(): List<Game>? { return gameService.getGames() }

    suspend fun getGame(id: Long): Game? { return gameService.getGame(id) }

    suspend fun getLastGame(): Game? { return gameService.getLastGame() }

    suspend fun saveGame(game: Game): Boolean { return gameService.saveGame(game) }

    suspend fun deleteGame(id: Long): Boolean { return gameService.deleteGame(id) }

    suspend fun countGames(): Int { return gameService.countGames() }
}