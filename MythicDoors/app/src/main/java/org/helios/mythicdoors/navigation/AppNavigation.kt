package org.helios.mythicdoors.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestination.Overview.route
    ) {
        composable(AppDestination.Overview.route)  {}
        composable(AppDestination.Login.route)  {}
        composable(AppDestination.Register.route)  {}
        composable(AppDestination.GameOpts.route)  {}
        composable(AppDestination.GameAction.route)  {}
        composable(AppDestination.ActionResult.route) {}
        composable(AppDestination.Scores.route) {}
    }
}