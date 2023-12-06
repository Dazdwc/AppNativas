package org.helios.mythicdoors.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.utils.AppConstants.Screens

class NavFunctionsImp(
    private val navController: NavController,
): INavFunctions {

    /* Aplicamos el patrón Singleton */
    companion object {
        @Volatile
        private var instance : NavFunctionsImp? = null

        fun getInstance(navController: NavController): NavFunctionsImp {
            return instance ?: synchronized(this) {
                instance ?: buildNavFunctionsImp(navController).also { instance = it }
            }
        }

        private fun buildNavFunctionsImp(navController: NavController): NavFunctionsImp {
            return NavFunctionsImp(navController)
        }
    }

    override fun navigateToLoginScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navController.navigateSingleTopTo(Screens.LOGIN_SCREEN)
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Login Screen")
            }
        }
    }

    override fun navigateToOverviewScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navController.navigateSingleTopTo(Screens.OVERVIEW_SCREEN)
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Overview Screen")
            }
        }
    }

    override fun navigateRegisterScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navController.navigateSingleTopTo(Screens.REGISTER_SCREEN)
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Register Screen")
            }
        }
    }

    override fun navigateGameOptsScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navController.navigateSingleTopTo(Screens.GAME_OPTS_SCREEN)
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Game Options Screen")
            }
        }
    }

    override fun navigateGameActionScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navController.navigateSingleTopTo(Screens.GAME_ACTION_SCREEN)
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Game Action Screen")
            }
        }
    }

    override fun navigateActionResultScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navController.navigateSingleTopTo(Screens.ACTION_RESULT_SCREEN)
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Action Result Screen")
            }
        }
    }

    override fun navigateScoresScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navController.navigateSingleTopTo(Screens.SCORES_SCREEN)
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Scores Screen")
            }
        }
    }

    override fun navigateGameGuideWebViewScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        try {
            navController.navigateSingleTopTo(Screens.GAME_GUIDE_WEBVIEW_SCREEN)
        } catch (e: Exception) {
            e.printStackTrace()
            scope.launch {
                snackbarHostState.showSnackbar("Error: Impossible to navigate to Game Guide WebView Screen")
            }
        }
    }
}