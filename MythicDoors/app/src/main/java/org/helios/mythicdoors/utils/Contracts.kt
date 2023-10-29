package org.helios.mythicdoors.utils

import android.provider.BaseColumns

class Contracts: BaseColumns {
    companion object DatabaseContract {
        const val DATABASE_NAME = "mythicdoors.db"
        const val DATABASE_VERSION = 1
    }

    object UserTableContract {}
    object EnemyTableContract {}
    object DoorTableContract {}
    object GameTableContract {}
}