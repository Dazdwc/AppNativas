package org.helios.mythicdoors.utils.connection.firestone

class Contracts {
    object UsersDocumentContract {
        const val FIELD_NAME_ID = "documentId"
        const val FIELD_NAME_NAME = "name"
        const val FIELD_NAME_EMAIL = "email"
        const val FIELD_NAME_PASSWORD = "password"
        const val FIELD_NAME_SCORE = "score"
        const val FIELD_NAME_LEVEL = "level"
        const val FIELD_NAME_EXPERIENCE = "experience"
        const val FIELD_NAME_COINS = "coins"
        const val FIELD_NAME_GOLD_COINS = "goldCoins"
        const val FIELD_NAME_IS_ACTIVE = "isActive"
        const val FIELD_NAME_CREATED_AT = "createdAt"
    }

    object GamesDocumentContract {
        const val FIELD_NAME_ID = "documentId"
        const val FIELD_NAME_USER = "user"
        const val FIELD_NAME_COIN = "coin"
        const val FIELD_NAME_LEVEL = "level"
        const val FIELD_NAME_SCORE = "score"
        const val FIELD_NAME_MAX_ENEMY_LEVEL = "maxEnemyLevel"
        const val FIELD_NAME_GAME_DATE_TIME = "gameDateTime"
    }
}