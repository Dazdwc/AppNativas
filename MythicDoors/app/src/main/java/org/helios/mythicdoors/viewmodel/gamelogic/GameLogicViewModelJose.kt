package org.helios.mythicdoors.viewmodel.gamelogic

import org.helios.mythicdoors.model.entities.User
import kotlin.random.Random


enum class door {
    UNO, DOS, TRES }
class GameLogicViewModelJose {

    fun calculateRamdomLevel(user : User): Int {
      var floor: Int
      var ceiling: Int
      val nDoor = door.UNO
      val userLevel = user.getLevel()

       floor = userLevel - when (nDoor) {
           door.UNO -> 3
          door.DOS -> 4
          door.TRES -> 5
       }

        ceiling = userLevel + when (nDoor) {
            door.UNO -> 3
            door.DOS -> 4
            door.TRES -> 5
        }

        val randomLevel = Random.nextInt(floor, ceiling)
        return randomLevel
    }

    fun advantage(user: User): Int {
        val firstLevel = calculateRamdomLevel(user)
        val secondLevel = calculateRamdomLevel(user)

        val level = minOf(firstLevel, secondLevel)

        return level
    }
    fun neutral(user: User): Int {
        val level = calculateRamdomLevel(user)
        return level
    }
    fun disAdvantage(user: User): Int {
        val firstLevel = calculateRamdomLevel(user)
        val secondLevel = calculateRamdomLevel(user)

        val level = maxOf(firstLevel, secondLevel)

        return level
    }

    fun doorElection(user: User, Door: door): Int {
        return when (Door) {
            door.UNO -> advantage(user)
            door.DOS -> neutral(user)
            door.TRES -> disAdvantage(user)
        }
    }
    fun IsWin(user: User, door: door): Boolean{
        var UserLevel = user.getLevel()
        var enemyLevel = doorElection(user, door)

        return UserLevel >= enemyLevel
    }

}