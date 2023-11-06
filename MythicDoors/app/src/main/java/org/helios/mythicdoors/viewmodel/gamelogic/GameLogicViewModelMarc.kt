package org.helios.mythicdoors.viewmodel.gamelogic

import org.helios.mythicdoors.model.entities.Enemy
import org.helios.mythicdoors.model.entities.User
import kotlin.random.Random

class GameLogicViewModelMarc {

    var lvlRandom: Int? = null
    val bet = 200 //Con click
    var win: Int? = null
    enum class Level {
        LOW,
        MEDIUM,
        HIGH
    }

    fun doorLevel(user: User, enemy: Enemy, level: Level): Int? {//Con click le damos el valor a level según la puerta
        //level = Level.HIGH
        val lvlRandom = when (level) {
            Level.LOW -> Random.nextInt(-3, 2)
            Level.MEDIUM -> Random.nextInt(-1, 4)
            Level.HIGH -> Random.nextInt(1, 6)
        }
        val lvlEnemy = user.getLevel() + lvlRandom
        val bet = 200
        var win: Int? = null

        if (lvlEnemy < user.getLevel()) {
            win = (bet * enemy.getCoinReward())
            println("Has ganado $win monedas!")
        } else {
            win = 0
            println("Has perdido. Mejor suerte la próxima vez.")
        }

        return win
    }
    /*fun doorLow(user: User, enemy: Enemy): Int? {
        val lvlRandom = Random.nextInt(-3, 2)
        val lvlEnemy = user.getLevel() + lvlRandom
        val bet = 200
        var win: Int? = null

        if (lvlEnemy < user.getLevel()) {
            win = (bet * enemy.getCoinReward())
            println("Has ganado $win monedas!")
        }else{
            win = 0
            println("Has perdido. Mejor suerte la próxima vez.")
        }

        return win
    }

    fun doorMedium(user:User, enemy: Enemy): Int? {
        val lvlRandom = Random.nextInt(-2, 3)
        val lvlEnemy = user.getLevel() + lvlRandom
        val bet = 200
        var win: Int? = null

        if (lvlEnemy > user.getLevel()) {
            win = (bet * enemy.getCoinReward())
            println("Has ganado $win monedas!")
        }else{
            win = 0
            println("Has perdido. Mejor suerte la próxima vez.")
        }

        return win
    }

    fun doorHigh(user:User, enemy: Enemy): Int?{
        val lvlRandom = Random.nextInt(-1, 4)
        val lvlEnemy = user.getLevel() + lvlRandom
        val bet = 200
        var win: Int? = null

        if (lvlEnemy > user.getLevel()) {
            win = (bet * enemy.getCoinReward())
            println("Has ganado $win monedas!")
        }else{
            win = 0
            println("Has perdido. Mejor suerte la próxima vez.")
        }

        return win
    }*/

}