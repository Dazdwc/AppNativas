package org.helios.mythicdoors.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.services.UserServiceImp
import org.helios.mythicdoors.services.interfaces.IUserService

suspend fun defaultDataLoader(dbHelper: Connection): Boolean = withContext(Dispatchers.IO) {
    val userService: IUserService = UserServiceImp(dbHelper)

    if (dbHelper.checkIfDatabaseIsEmpty()) return@withContext true

    try {
        val flag = false

        flag.apply {
            insertAdminUserInDatabase(userService)
            // insertDoorsInDatabase()
        }

        // TODO -> Crear enemigos y puertas por defecto

        return@withContext flag
    } catch (e: Exception) {
        e.printStackTrace()
        return@withContext false
    }

}

private fun createAdminUser(): User { return User.create("admin", "admin@admin.com", "47m1N") }

private suspend fun insertAdminUserInDatabase(userService: IUserService): Boolean = withContext(Dispatchers.IO) { return@withContext userService.saveUser(createAdminUser()) }

// TODO -> Crear 3 puertas por defecto