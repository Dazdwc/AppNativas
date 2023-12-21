package org.helios.mythicdoors.model

import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.entities.Jackpot
import org.helios.mythicdoors.model.entities.Location
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.services.LocationServiceImp
import org.helios.mythicdoors.services.api.IJackpotService
import org.helios.mythicdoors.services.api.JackpotApiService
import org.helios.mythicdoors.services.firestore.FSGameServiceImp
import org.helios.mythicdoors.services.firestore.FSUserServiceImp
import org.helios.mythicdoors.services.firestore.IDataService
import org.helios.mythicdoors.services.interfaces.ILocationService
import org.helios.mythicdoors.utils.connection.Connection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataController @Inject constructor(
    dbHelper: Connection
) {
    private val locationService: ILocationService = LocationServiceImp.getInstance(dbHelper)
    private val fsUserServiceImp: IDataService<User> = FSUserServiceImp.getInstance()
    private val fsGameServiceImp: IDataService<Game> = FSGameServiceImp.getInstance()
    private val jackpotApiService: IJackpotService = JackpotApiService.getInstance()

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

    suspend fun saveLocation(location: Location): Boolean { return locationService.saveLocation(location) }
    suspend fun getLastLocation(): Location? { return locationService.getLastLocation() }

    suspend fun getAllFSUsers(): List<User>? { return fsUserServiceImp.getAll() }
    suspend fun getOneFSUser(email: String): User? { return fsUserServiceImp.getOne(email) }
    suspend fun saveOneFSUser(user: User): Result<Boolean> { return fsUserServiceImp.saveOne(user) }
    suspend fun updateOneFSUser(user: User): Result<Boolean> { return fsUserServiceImp.updateOne(user) }
    suspend fun deleteOneFSUser(user: User): Result<Boolean> { return fsUserServiceImp.deleteOne(user) }
    suspend fun countFSUsers(): Int { return fsUserServiceImp.count() }
    suspend fun getLastFSUser(): User? { return fsUserServiceImp.getLast() }

    suspend fun getAllFSGames(): List<Game>? { return fsGameServiceImp.getAll() }
    suspend fun getOneFSGame(email: String): Game? { return fsGameServiceImp.getOne(email) }
    suspend fun saveOneFSGame(game: Game): Result<Boolean> { return fsGameServiceImp.saveOne(game) }
    suspend fun deleteOneFSGame(game: Game): Result<Boolean> { return fsGameServiceImp.deleteOne(game) }
    suspend fun countFSGames(): Int { return fsGameServiceImp.count() }
    suspend fun getLastFSGame(): Game? { return fsGameServiceImp.getLast() }

    suspend fun getJackpots(): Result<List<Jackpot>> { return jackpotApiService.getJackpots() }
    suspend fun getLastJackpot(): Result<Jackpot> { return jackpotApiService.getLastJackpot() }
    suspend fun postJackpot(jackpot: Jackpot): Result<String> { return jackpotApiService.postJackpot(jackpot) }
    suspend fun putJackpot(
        jackpotId: String,
        jackpot: Jackpot
    ): Result<String> {
        return jackpotApiService.putJackpot(
        jackpotId,
        jackpot
        )
    }
    suspend fun getJackpotPot(): Long = getLastJackpot().getOrNull()?.getPot() ?: 0L
}