package org.helios.mythicdoors.viewmodel.gamelogic
import kotlin.random.Random

enum class DoorType {
    LOW, EQUAL, HIGH
}
class GameLogicViewModelDZafra {
    /*//Esta funcion genera un numero aleatorio dependiendo del nivel del personaje
        // despues resta uno porque es la puerta fácil haciendo que haya un 3/1 de victoria.
        fun lowdoor(number: Int): Int{
            return randomizeEnemy(number) - 25
        }
        //Esta funcion genera un numero aleatorio dependiendo del nivel del personaje
        // despues resta uno porque es la puerta fácil haciendo que haya un 2/2 de victoria.
        fun equaldoor(number: Int): Int{
            return randomizeEnemy(number)
        }
        //Esta funcion genera un numero aleatorio dependiendo del nivel del personaje
        // despues resta uno porque es la puerta fácil haciendo que haya un 1/3 de victoria.
        fun highdoor(number: Int): Int{
            return randomizeEnemy(number) + 25
        }*/

    // Genero una funcion Randomize para testear, esto se generará en otra clase
    fun randomizeEnemyLevel(playerLevel: Int): Int{
        return Random.nextInt(playerLevel -10,playerLevel + 10)
    }
    //Se estabilizan los niveles de los enemigos para que el minimo sea 0 y no numeros negativos.
    fun adjustLevel(levelEnemy: Int): Int{
        //return if (lvlenemy < 0) 0 else lvlenemy
        return levelEnemy.coerceAtLeast(0) //principio cleancode(?)
    }


    //Funcion mejorada en clean code, donde en una sola funcion calculamos la dificultad dependiendo
    // de la puerta que se escoja en funcion al nivel.
    fun generateEnemyAccordingToDoor(levelPlayer: Int, doorType: DoorType): Int{
        val adjustment = when (doorType){
            DoorType.LOW -> -5
            DoorType.EQUAL -> 0
            DoorType.HIGH -> 5
        }
        return randomizeEnemyLevel(levelPlayer) + adjustment
    }

    //Compara el nivel del jugador y enemigo y determina si hay victoria
    fun IsWin(levelPlayer: Int, levelEnemy: Int): Boolean{
        return levelPlayer >= levelEnemy
    }

    //Se calcula la recompensa en funcion de la puerta elegiga
    fun calculoApuesta(bet: Int, doorType: DoorType): Int{
        val multipler = when (doorType){
            DoorType.LOW -> 1.5
            DoorType.EQUAL -> 2.0
            DoorType.HIGH -> 3.0
        }
        return (multipler * bet).toInt()
    }

    //Se calcula la experiencia en funcion de las apuestas.
    fun calculateNewExperience(experiencia: Int, bet: Int): Int{
        return experiencia + bet
    }
    //Para subir un nivel es necesario multiplicalo por mil,
    fun calculateLevel(experience: Int): Int{
        return (experience/1000).coerceAtLeast(1)
    }

    fun experienceToNextLevel(levelPlayer: Int): Int{
        return (levelPlayer+1)*1000
    }



    }
fun main(){
    EjemploPartida()
}

fun EjemploPartida(){
    //Creamos una instancia para llamar a las funciones como buena práctica.
        val gameLogic = GameLogicViewModelDZafra()
    //Declaramos variables (normalmente se accederá desde la base de datos)
        var currentExperience = 0
        var experienceToNextLevel = 0
        var playerLevel = 1
        var enemyLevel = 0
        var coins = 20000

        var continuePlaying = true // Variable para controlar el bucle

    while (coins > 0 && continuePlaying) {
        //Se hace una apuesta
        print("Apuesta: \n ")
        val bet = readLine()?.toIntOrNull() ?: 0

        //se controla de forma primitiva la apuesta
        if (bet > coins) {
            println("No tienes suficientes monedas para realizar esa apuesta.")
        } else {
            coins -= bet
        }

        // Se elige la puerta de forma primitiva, se ha credao previamente un class enum
        print("Puerta: \n ")
        val userSelection = readLine()
        val doorType = when (userSelection?.lowercase()) {
            "low" -> DoorType.LOW
            "equal" -> DoorType.EQUAL
            "high" -> DoorType.HIGH
            else -> {
                //si no se elige una puerta correcta se asigna, esto no pasará porque se realizará
                //a través de un onclick()
                println("Selección no válida. Se utilizará la puerta por defecto (EQUAL).")
                DoorType.EQUAL
            }
        }

        enemyLevel = gameLogic.generateEnemyAccordingToDoor(playerLevel, doorType)
        enemyLevel = gameLogic.adjustLevel(enemyLevel)
        print("\nTu nivel: " + playerLevel + "\nNivel Enemigo:" + enemyLevel)
        if (gameLogic.IsWin(playerLevel, enemyLevel)) {
            currentExperience = gameLogic.calculateNewExperience(currentExperience, bet)
            coins += gameLogic.calculoApuesta(bet, doorType)
            playerLevel = gameLogic.calculateLevel(currentExperience)
            experienceToNextLevel = gameLogic.experienceToNextLevel(playerLevel)
            // Creariamos el objeto de user y se actualizaría en la bbd
            print(" \nExperiencia actual:" + currentExperience + "\nCoins:" + coins + "\nplayerLevel: " + playerLevel + "\nExperiencia para el siguiente Nivel:" + experienceToNextLevel)
        } else {
            print(" \nTu pierdes")
            print(" \nExperiencia actual:" + currentExperience + "\nCoins:" + coins + "\nplayerLevel: " + playerLevel + "\nExperiencia para el siguiente Nivel:" + experienceToNextLevel)
        }

        if(coins >0){
            print("\nSeguir jugando? 1. si 2. no")
            val opcionSeguirJugando = readLine()?.toIntOrNull() ?: 0
            when(opcionSeguirJugando){
                1-> continuePlaying = true
                2-> continuePlaying = false
            }}

    }
    /*when(userSelection){
                    "1" -> {   println("Seleccionaste la Puerta Fácil")
        }
            "2" -> {
                println("Seleccionaste la normal")
                lvlenemy = logica.equaldoor(lvlplayer)
                win = logica.IsWin(lvlplayer,lvlenemy)
                if(win){
                    money += (money * 2)
                }else{
                    money = 0
                }
        }
            "3" -> {
                println("Seleccionaste la dificil")
                lvlenemy = logica.highdoor(lvlplayer)
                win = logica.IsWin(lvlplayer,lvlenemy)
                if(win){
                    money += (money * 3)
                }else{
                    money * 0
                }
        }
        }*/

}