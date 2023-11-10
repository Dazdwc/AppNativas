package org.helios.mythicdoors.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.helios.mythicdoors.ui.screens.*

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,

        //startDestination = AppDestination.Overview.route
        startDestination = AppDestination.Scores.route
    ) {
        composable(AppDestination.Overview.route)  { OverviewScreen(navController = navController) }
        composable(AppDestination.Login.route)  { LoginScreen(navController = navController) }
        composable(AppDestination.Register.route)  { RegisterScreen(navController = navController) }
        composable(AppDestination.GameOpts.route)  { GameOptsScreen(navController = navController) }
        composable(AppDestination.GameAction.route)  { GameActionScreen(navController = navController) }
        composable(AppDestination.ActionResult.route) { ActionResultScreen(navController = navController) }
        composable(AppDestination.Scores.route) { ScoresScreen(navController = navController) }
    }
}

fun NavController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}