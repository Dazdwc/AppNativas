package org.helios.mythicdoors.utils.connection

import android.provider.BaseColumns

class Contracts: BaseColumns {
    object DatabaseContract {
        const val DATABASE_NAME = "mythicdoors.db"
        const val DATABASE_VERSION = 1
    }

    object UserTableContract {
        const val TABLE_NAME = "user"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_EMAIL = "email"
        const val COLUMN_NAME_PASSWORD = "password"
        const val COLUMN_NAME_SCORE = "score"
        const val COLUMN_NAME_LEVEL = "level"
        const val COLUMN_NAME_EXPERIENCE = "experience"
        const val COLUMN_NAME_COINS = "coins"
        const val COLUMN_NAME_GOLD_COINS = "gold_coins"
        const val COLUMN_NAME_IS_ACTIVE = "is_active"
        const val COLUMN_NAME_CREATED_AT = "created_at"

        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME_NAME TEXT NOT NULL," +
                "$COLUMN_NAME_EMAIL TEXT NOT NULL," +
                "$COLUMN_NAME_PASSWORD TEXT NOT NULL," +
                "$COLUMN_NAME_SCORE INTEGER NOT NULL," +
                "$COLUMN_NAME_LEVEL INTEGER NOT NULL," +
                "$COLUMN_NAME_EXPERIENCE INTEGER NOT NULL," +
                "$COLUMN_NAME_COINS INTEGER NOT NULL," +
                "$COLUMN_NAME_GOLD_COINS INTEGER NOT NULL," +
                "$COLUMN_NAME_IS_ACTIVE INTEGER NOT NULL," +
                "$COLUMN_NAME_CREATED_AT TEXT NOT NULL" +
            ");"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    object GameTableContract {
        const val TABLE_NAME = "game"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_ID_USER = "id_user"
        const val COLUMN_NAME_COIN = "coin"
        const val COLUMN_NAME_LEVEL = "level"
        const val COLUMN_NAME_SCORE = "score"
        const val COLUMN_NAME_MAX_ENEMY_LEVEL = "max_enemy_level"
        const val COLUMN_NAME_GAME_DATE_TIME = "game_date_time"

        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME(" +
                    "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME_ID_USER INTEGER NOT NULL," +
                    "$COLUMN_NAME_COIN INTEGER NOT NULL," +
                    "$COLUMN_NAME_LEVEL INTEGER NOT NULL," +
                    "$COLUMN_NAME_SCORE INTEGER NOT NULL," +
                    "$COLUMN_NAME_MAX_ENEMY_LEVEL INTEGER NOT NULL," +
                    "$COLUMN_NAME_GAME_DATE_TIME TEXT NOT NULL," +
                    "FOREIGN KEY ($COLUMN_NAME_ID_USER) REFERENCES user(id)" +
                    ");"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
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
                    "FOREIGN KEY ($COLUMN_NAME_ID_USER) REFERENCES user(id)" +
                    ");"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    companion object DatabaseTablesCreator {
        val dbTablesList: List<String> = listOf(
            UserTableContract.SQL_CREATE_ENTRIES,
            GameTableContract.SQL_CREATE_ENTRIES,
            LocationTableContract.SQL_CREATE_ENTRIES
        )
    }
}