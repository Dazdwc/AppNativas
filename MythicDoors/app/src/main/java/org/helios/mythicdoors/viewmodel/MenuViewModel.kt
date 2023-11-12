package org.helios.mythicdoors.viewmodel

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import kotlin.system.exitProcess

class MenuViewModel(
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

    fun navigateToOverview(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        scope.launch {
            try {
                navFunctions.navigateToOverviewScreen(scope, snackbarHostState)
            } catch (e: Exception) {
                Log.e("MenuViewModel", "navigateToOverview: $e")
            }
        }
    }

    fun closeApp() {
        MainActivity.setContext(MainActivity.getContext())
        exitProcess(0)
    }
}