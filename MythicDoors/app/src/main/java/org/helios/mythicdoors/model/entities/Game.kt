package org.helios.mythicdoors.model.entities

import java.time.LocalDateTime

data class Game(
    private val user: User,
    private val coin: Long,
    private val level: Long,
    private val score: Long,
    private val maxEnemyLevel: Long,
    private val gameDateTime: LocalDateTime,
) {
    fun getUser(): User { return user}
    fun getCoin(): Long { return coin}
    fun getLevel(): Long { return level}
    fun getScore(): Long { return score}
    fun getMaxEnemyLevel(): Long { return maxEnemyLevel}
    fun getDateTime(): LocalDateTime { return gameDateTime}

    fun setDateTime(gameDateTime: LocalDateTime) { this.gameDateTime }

    companion object {
        fun createEmptyGame(): Game {
            return Game(
                User.createEmptyUser(),
                -1,
                -1,
                -1,
                -1,
                gameDateTime = LocalDateTime.now())
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
                gameDateTime = LocalDateTime.now())
        }
    }

/*    fun isEmpty(): Boolean { return this.id == null }*/

    fun isFirestoreEmpty(): Boolean { return this.user.isFirestoreEmpty() }

    fun isValid(): Boolean {
        return !this.user.isFirestoreEmpty()
                && this.coin >= 0
                && this.level >= 0
                && this.score >= 0
                && this.maxEnemyLevel >= 0
    }
}
