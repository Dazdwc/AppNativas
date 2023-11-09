package org.helios.mythicdoors.viewmodel.gamelogic

import org.helios.mythicdoors.model.entities.EnemyDBoj
import org.helios.mythicdoors.model.entities.User
import kotlin.random.Random

class GameLogicViewModelMarc {
    fun doorLow(user: User, enemy: EnemyDBoj): Int? {
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

    fun doorMedium(user:User, enemy: EnemyDBoj): Int? {
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

    fun doorHigh(user:User, enemy: EnemyDBoj): Int?{
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
    }

}