package org.helios.mythicdoors.model.entities

import Enemy
import java.time.LocalDate
import java.time.LocalDateTime

data class Game(
    private val idPartida: Long?,
    private val idUser: Long?,
    private var coin: Int,
    private var level: Int,
    private var score: Int,
    private var maxEnemy: Int,
    private var dateTime: LocalDateTime,
) {
    fun getIdPartida(): Long? { return idPartida }
    fun getIdUser(): Long? { return idUser}
    fun getCoin(): Int { return coin}
    fun getLevel(): Int { return level}
    fun getScore(): Int { return score}
    fun getMaxEnemy(): Int { return maxEnemy}
    fun getDateTime(): LocalDateTime { return dateTime}

    fun setCoin(coin: Int) { this.coin = coin }
    fun setLevel(level: Int) { this.level = level }
    fun setScore(score: Int) { this.score = score }
    fun setMaxEnemy(maxEnemy: Int) { this.maxEnemy = maxEnemy}
    fun setDateTime(dateTime: LocalDateTime) { this.dateTime = dateTime}

    companion object {
        fun createEmptyGame(): Game {
            return Game(
                null,
                0,
                0,
                0,
                0,
                0,
                dateTime = LocalDateTime.now())
        }

        fun createGame(
            idUser: Long?,
            coin: Int,
            level: Int,
            score: Int,
            maxEnemy: Int,
        ): Game {
            return Game(
                null,
                idUser,
                coin,
                level,
                score,
                maxEnemy,
                dateTime = LocalDateTime.now())
        }
    }

    fun isEmpty(): Boolean { return this.idPartida == null }
}
