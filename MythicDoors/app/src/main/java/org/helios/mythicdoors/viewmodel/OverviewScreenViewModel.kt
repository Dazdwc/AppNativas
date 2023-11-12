package org.helios.mythicdoors.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.navigation.navigateSingleTopTo
import javax.inject.Inject


@HiltViewModel
class OverviewScreenViewModel @Inject constructor(
    private val dataController: DataController,
): ViewModel() {
    private val navController: NavController
        get() { return _navController }
    private val navFunctions: INavFunctions by lazy { NavFunctionsImp.getInstance(navController) }

    private lateinit var _navController: NavController
    fun setNavController(navController: NavController) {
        _navController = navController
    }

    fun navigateToLoginScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        navFunctions.navigateToLoginScreen(scope, snackbarHostState)
    }
}