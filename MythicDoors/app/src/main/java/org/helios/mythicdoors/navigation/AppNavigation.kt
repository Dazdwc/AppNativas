package org.helios.mythicdoors.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.helios.mythicdoors.ui.screens.OverviewScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestination.Overview.route
    ) {
        composable(AppDestination.Overview.route)  { OverviewScreen(navController = navController) }
        composable(AppDestination.Login.route)  {}
        composable(AppDestination.Register.route)  {}
        composable(AppDestination.GameOpts.route)  {}
        composable(AppDestination.GameAction.route)  {}
        composable(AppDestination.ActionResult.route) {}
        composable(AppDestination.Scores.route) {}
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