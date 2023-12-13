package org.helios.mythicdoors.model

import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.services.LocationServiceImp
import org.helios.mythicdoors.services.firestore.FSGameServiceImp
import org.helios.mythicdoors.services.firestore.FSUserServiceImp
import org.helios.mythicdoors.services.firestore.IDataService
import org.helios.mythicdoors.services.interfaces.IGameService
import org.helios.mythicdoors.services.interfaces.ILocationService
import org.helios.mythicdoors.services.interfaces.IUserService
import org.helios.mythicdoors.utils.connection.Connection
import org.helios.mythicdoors.utils.connection.firestone.FirestoreClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataController @Inject constructor(
    dbHelper: Connection
) {
    /*private val userService: IUserService = UserServiceImp(dbHelper)
    private val gameService: IGameService = GameServiceImp(dbHelper)*/
    private val locationService: ILocationService = LocationServiceImp(dbHelper)

    private val fsUserServiceImp: IDataService<User> = FSUserServiceImp()
    private val fsGameServiceImp: IDataService<Game> = FSGameServiceImp()

    /*
     * We apply a Singleton pattern to create the data controller.
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

    /*suspend fun getAllUsers(): List<User>? { return userService.getUsers() }

    suspend fun getUser(id: Long): User? { return userService.getUser(id = id) }

    suspend fun getLastUser(): User? { return userService.getLastUser() }

    suspend fun saveUser(user: User): Boolean { return userService.saveUser(user) }

    suspend fun deleteUser(id: Long): Boolean { return userService.deleteUser(id) }

    suspend fun countUsers(): Int { return userService.countUsers() }


    suspend fun getAllGames(): List<Game>? { return gameService.getGames() }

    suspend fun getGame(id: Long): Game? { return gameService.getGame(id) }

    suspend fun getLastGame(): Game? { return gameService.getLastGame() }

    suspend fun saveGame(game: Game): Boolean { return gameService.saveGame(game) }

    suspend fun deleteGame(id: Long): Boolean { return gameService.deleteGame(id) }

    suspend fun countGames(): Int { return gameService.countGames() }*/


    suspend fun saveLocation(location: Location): Boolean { return locationService.saveLocation(location) }
    suspend fun getLastLocation(): Location? { return locationService.getLastLocation() }

    suspend fun getAllFSUsers(): List<User>? { return fsUserServiceImp.getAll() }
    suspend fun getOneFSUser(email: String): User? { return fsUserServiceImp.getOne(email) }
    suspend fun saveOneFSUser(user: User): Result<Boolean> { return fsUserServiceImp.saveOne(user) }
    suspend fun deleteOneFSUser(user: User): Result<Boolean> { return fsUserServiceImp.deleteOne(user) }
    suspend fun countFSUsers(): Int { return fsUserServiceImp.count() }
    suspend fun getLastFSUser(): User? { return fsUserServiceImp.getLast() }

    suspend fun getAllFSGames(): List<Game>? { return fsGameServiceImp.getAll() }
    suspend fun getOneFSGame(email: String): Game? { return fsGameServiceImp.getOne(email) }
    suspend fun saveOneFSGame(game: Game): Result<Boolean> { return fsGameServiceImp.saveOne(game) }
    suspend fun deleteOneFSGame(game: Game): Result<Boolean> { return fsGameServiceImp.deleteOne(game) }
    suspend fun countFSGames(): Int { return fsGameServiceImp.count() }
    suspend fun getLastFSGame(): Game? { return fsGameServiceImp.getLast() }
}