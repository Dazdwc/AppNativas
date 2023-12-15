package org.helios.mythicdoors.viewmodel.gamelogic

import org.helios.mythicdoors.utils.AppConstants
import kotlin.math.ceil
import kotlin.random.Random

private class JackpotGameLogic {

    // TODO: Llamada a Firebase para obtener el jackpot en lugar de tener uno fijo
    private var jackpot = 1000L
    private var randomWin = 23

    fun winJackpot(): Int {
        return Random.nextInt(1, 1001)
    }


    private fun multiPlayerGame(isWin: Boolean, bet: Long, door: String): Long {
        return if (isWin) {
            if (winJackpot() == randomWin) {
                handleJackpotBet(bet, door)
            } else {
                //lógica adicional para el caso de victoria sin jackpot, la que ya tenemos
                0
            }
        } else {
            updateJackpotOnLose(bet)
        }
    }

    // Si se gana, se determina el premio en función de la puerta
    private fun handleJackpotBet(bet: Long, door: String): Long {

        // Determinamos un tipo de condición
        val jackpotMultiplier = when (door) {
            AppConstants.EASY_DOOR -> 0.25f
            AppConstants.AVERAGE_DOOR -> 0.5f
            AppConstants.HARD_DOOR -> 1f
            else -> throw IllegalArgumentException("Door type not recognized: $door")
        }

        // Devolvemos el resultado de la apuesta
        return updateBetOnWin(getJackpot(), jackpotMultiplier, bet)
    }

    // Función estándar para recoger el jackpot de la base de datos
    private fun getJackpot(): Long {
        // TODO: Recoger el jackpot desde la base de datos para retornarlo
        return jackpot
    }

    // Función para determinar la cuantía del premio y llamada a la función de actualizar el jackpot
    private fun updateBetOnWin(jackpot: Long, multiplier: Float, bet: Long): Long {
        val difference = (jackpot * multiplier).toLong()

        updateJackpotOnWin(difference)

        return difference + bet

    }

    // Función que actualiza el jackpot cuando se gana
    private fun updateJackpotOnWin(difference: Long): Long {
        // TODO: Actualizamos el resultado en la base de datos
        // He visto que sería algo como variable.setValue( jackpot -= diference) (conviene recoger
        //antes la variable)
        jackpot -= difference
        //TODO retornar
        return 0
    }

    // Función que actualiza el jackpot cuando se pierde
    private fun updateJackpotOnLose(bet: Long): Long {
        return jackpot + ceil(bet * 0.5).toLong()
    }
}

