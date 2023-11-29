package org.helios.mythicdoors.model.entities

import java.time.LocalDateTime

data class Game(
    private val id: Long?,
    private val user: User,
    private val coin: Int,
    private val level: Int,
    private val score: Int,
    private val maxEnemyLevel: Int,
    private val gameDateTime: LocalDateTime,
) {
    fun getId(): Long? { return id }
    fun getUser(): User { return user}
    fun getCoin(): Int { return coin}
    fun getLevel(): Int { return level}
    fun getScore(): Int { return score}
    fun getMaxEnemyLevel(): Int { return maxEnemyLevel}
    fun getDateTime(): LocalDateTime { return gameDateTime}

    companion object {
        fun createEmptyGame(): Game {
            return Game(
                null,
                User.createEmptyUser(),
                -1,
                -1,
                -1,
                -1,
                gameDateTime = LocalDateTime.now())
        }

        fun create(
            user: User,
            coin: Int,
            level: Int,
            score: Int,
            maxEnemyLevel: Int,
        ): Game {
            return Game(
                null,
                user,
                coin,
                level,
                score,
                maxEnemyLevel,
                gameDateTime = LocalDateTime.now())
        }
    }

    fun isEmpty(): Boolean { return this.id == null }
    fun isValid(): Boolean {
        return !this.user.isEmpty()
                && this.coin >= 0
                && this.level >= 0
                && this.score >= 0
                && this.maxEnemyLevel >= 0
    }
}
