package org.helios.mythicdoors.model

import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.services.EnemyServiceImp
import org.helios.mythicdoors.services.UserServiceImp
import org.helios.mythicdoors.services.interfaces.IEnemyService
import org.helios.mythicdoors.services.interfaces.IUserService
import java.sql.Connection

class DataController(
    dbHelper: org.helios.mythicdoors.utils.Connection
) {
    private val userService: IUserService = UserServiceImp(dbHelper)
    /*
     * Aplicamos un patrón Singgleton para crear el controlador de datos.
     */
    companion object {
        private var instance: DataController? = null
        fun getInstance(dbHelper: org.helios.mythicdoors.utils.Connection): DataController {
            if (instance == null) {
                instance = DataController(dbHelper)
            }
            return instance!!
        }
    }

    fun getUserService(): IUserService {
        return userService
    }

    suspend fun getAllUsers(): List<User>? { return userService.getUsers() }

    suspend fun getUser(id: Long): User? { return userService.getUser(id = id) }

    suspend fun getLastUser(): User? { return userService.getLastUser() }

    suspend fun saveUser(user: User): Boolean { return userService.saveUser(user) }

    suspend fun deleteUser(id: Long): Boolean { return userService.deleteUser(id) }

    suspend fun countUsers(): Int { return userService.countUsers() }


}