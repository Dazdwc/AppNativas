package org.helios.mythicdoors.utils.connection

import android.provider.BaseColumns

class Contracts: BaseColumns {
    object DatabaseContract {
        const val DATABASE_NAME = "mythicdoors.db"
        const val DATABASE_VERSION = 1
    }

    object LocationTableContract {
        const val TABLE_NAME = "location"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_ID_USER = "id_user"
        const val COLUMN_NAME_LATITUDE = "latitude"
        const val COLUMN_NAME_LONGITUDE = "longitude"
        const val COLUMN_NAME_CREATED_AT = "created_at"

        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME(" +
                    "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME_ID_USER INTEGER NOT NULL," +
                    "$COLUMN_NAME_LATITUDE REAL NOT NULL," +
                    "$COLUMN_NAME_LONGITUDE REAL NOT NULL," +
                    "$COLUMN_NAME_CREATED_AT TEXT NOT NULL," +
//                    "FOREIGN KEY ($COLUMN_NAME_ID_USER) REFERENCES user(id)" +
                    ");"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    companion object DatabaseTablesCreator {
        val dbTablesList: List<String> = listOf(
            LocationTableContract.SQL_CREATE_ENTRIES
        )
    }
}