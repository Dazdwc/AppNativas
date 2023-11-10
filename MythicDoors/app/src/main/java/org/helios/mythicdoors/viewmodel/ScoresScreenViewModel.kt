package org.helios.mythicdoors.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.store.StoreManager

class ScoresScreenViewModel(
    private val dataController: DataController
): ViewModel() {
    private val navController: NavController
        get() {
            return _navController
        }
    private val navFunctions: INavFunctions by lazy { NavFunctionsImp.getInstance(navController) }

    private lateinit var _navController: NavController
    fun setNavController(navController: NavController) {
        _navController = navController
    }

    private val store: StoreManager by lazy { StoreManager.getInstance() }

    val game: MutableLiveData<List<Game>> = MutableLiveData(listOf())

    fun createSerGames(){
        val games: MutableList<Game> = mutableListOf()
        val actualUser = User.create("DZafra", "dzafra@helios.com", "1223456")

        games.apply {
            add(Game.create(actualUser, 100, 1, 100, 1))
            add(Game.create(actualUser, 123, 2, 250, 2))
            add(Game.create(actualUser, 180, 3, 156, 3))
            add(Game.create(actualUser, 250, 2, 121, 1))
            add(Game.create(actualUser, 240, 1, 162, 2))
            add(Game.create(actualUser, 100, 1, 171, 1))
            add(Game.create(actualUser, 147, 3, 172, 3))
            add(Game.create(actualUser, 198, 1, 145, 2))
            add(Game.create(actualUser, 321, 2, 173, 1))
            add(Game.create(actualUser, 231, 1, 154, 2))
            add(Game.create(actualUser, 133, 1, 123, 1))
            subList(0, 10)
        }
        games.sortByDescending { it.getScore() }
        game.value = games
    }



}