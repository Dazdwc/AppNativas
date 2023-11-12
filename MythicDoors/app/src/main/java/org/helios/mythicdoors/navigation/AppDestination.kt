package org.helios.mythicdoors.navigation

import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants.Screens

sealed interface IAppDestination {
    val route: String
    val screenName: String
}

sealed class AppDestination(
    override val route: String,
    override val screenName: String
): IAppDestination {
    data object Overview: AppDestination(route = Screens.OVERVIEW_SCREEN, screenName = R.string.app_name.toString())
    data object Login: AppDestination(route = Screens.LOGIN_SCREEN, screenName = "Login")
    data object Register: AppDestination(route = Screens.REGISTER_SCREEN, screenName = "Register")
    data object GameOpts: AppDestination(route = Screens.GAME_OPTS_SCREEN, screenName = "Game Options")
    data object GameAction: AppDestination(route = Screens.GAME_ACTION_SCREEN, screenName = "Game Action")
    data object ActionResult: AppDestination(route = Screens.ACTION_RESULT_SCREEN, screenName = "Action Result")
    data object Scores: AppDestination(route = Screens.SCORES_SCREEN, screenName = "Scores")
}

val appDestinations = listOf(
    AppDestination.Overview,
    AppDestination.Login,
    AppDestination.Register,
    AppDestination.GameOpts,
    AppDestination.GameAction,
    AppDestination.ActionResult,
    AppDestination.Scores
)