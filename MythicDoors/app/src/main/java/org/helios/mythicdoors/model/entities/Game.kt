package org.helios.mythicdoors.model.entities

<<<<<<< HEAD
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
=======
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
>>>>>>> ca4d6f96e00f4bf8b637fb2667e64cc28a59993e

    companion object {
        fun createEmptyGame(): Game {
            return Game(
                null,
<<<<<<< HEAD
=======
                User.createEmptyUser(),
>>>>>>> ca4d6f96e00f4bf8b637fb2667e64cc28a59993e
                0,
                0,
                0,
                0,
<<<<<<< HEAD
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
=======
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
>>>>>>> ca4d6f96e00f4bf8b637fb2667e64cc28a59993e
}
