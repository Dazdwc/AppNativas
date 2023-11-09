package org.helios.mythicdoors.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.navigation.navigateSingleTopTo

//Login Screen

fun navigateToLoginScreen(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    try {
        navController.navigateSingleTopTo("login_screen")
    } catch (e: Exception) {
        e.printStackTrace()
        scope.launch {
            snackbarHostState.showSnackbar("Error: Impossible to navigate to Login Screen")
        }
    }
}

//Register Screen
fun navigateRegisterScreen(navController: NavController,
                           scope: CoroutineScope,
                           snackbarHostState: SnackbarHostState
) {
    try {
        navController.navigate("register_screen")
    } catch (e: Exception) {
        e.printStackTrace()
        scope.launch {
            snackbarHostState.showSnackbar("Error: Impossible to navigate to Register Screen")
        }
    }
}

//Overview Screen

fun navigateToOverviewScreen(navController: NavController,
                             scope: CoroutineScope,
                             snackbarHostState: SnackbarHostState
) {
    try {
        navController.navigate("overview_screen")
    } catch (e: Exception) {
        e.printStackTrace()
        scope.launch {
            snackbarHostState.showSnackbar("Error: Impossible to navigate to Overview Screen")
        }
    }
}

//Scores Screen

fun navigateToScoreScreen(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    try {
        navController.navigateSingleTopTo("scores_screen")
    } catch (e: Exception) {
        e.printStackTrace()
        scope.launch {
            snackbarHostState.showSnackbar("Error: Impossible to navigate to Score Screen")
        }
    }
}


//Action Result Screen

fun navigateToResultScreen(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    try {
        navController.navigateSingleTopTo("action_result_screen")
    } catch (e: Exception) {
        e.printStackTrace()
        scope.launch {
            snackbarHostState.showSnackbar("Error: Impossible to navigate to Result Screen")
        }
    }
}


//Game Action Screen

fun navigateToActionScreen(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    try {
        navController.navigateSingleTopTo("game_action_screen")
    } catch (e: Exception) {
        e.printStackTrace()
        scope.launch {
            snackbarHostState.showSnackbar("Error: Impossible to navigate to Action Screen")
        }
    }
}


//Game Opts Screen

fun navigateToOptsScreen(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    try {
        navController.navigateSingleTopTo("game_opts_screen")
    } catch (e: Exception) {
        e.printStackTrace()
        scope.launch {
            snackbarHostState.showSnackbar("Error: Impossible to navigate to Options Screen")
        }
    }
}

