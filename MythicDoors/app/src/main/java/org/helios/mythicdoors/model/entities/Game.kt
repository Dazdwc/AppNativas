package org.helios.mythicdoors.model.entities

import com.google.firebase.Timestamp

data class Game(
    private val user: User,
    private val coin: Long,
    private val level: Long,
    private val score: Long,
    private val maxEnemyLevel: Long,
    private val gameDateTime: Timestamp,
) {
    fun getUser(): User { return user}
    fun getCoin(): Long { return coin}
    fun getLevel(): Long { return level}
    fun getScore(): Long { return score}
    fun getMaxEnemyLevel(): Long { return maxEnemyLevel}
    fun getDateTime(): Timestamp { return gameDateTime}

    fun setDateTime(gameDateTime: Timestamp) { this.gameDateTime }

    companion object {
        fun createEmptyGame(): Game {
            return Game(
                User.createEmptyUser(),
                -1,
                -1,
                -1,
                -1,
                gameDateTime = Timestamp.now())
        }

        fun create(
            user: User,
            coin: Long,
            level: Long,
            score: Long,
            maxEnemyLevel: Long,
        ): Game {
            return Game(
                user,
                coin,
                level,
                score,
                maxEnemyLevel,
                gameDateTime = Timestamp.now())
        }
    }

    fun isNotEmpty(): Boolean { return this.user.isNotEmpty() }

    fun isValid(): Boolean {
        return this.user.getEmail().isNotEmpty()
                && this.coin >= 0
                && this.level >= 0
                && this.score >= 0
                && this.maxEnemyLevel >= 0
    }
}
