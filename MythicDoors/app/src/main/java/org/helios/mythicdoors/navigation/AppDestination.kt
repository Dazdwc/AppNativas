package org.helios.mythicdoors.navigation

import org.helios.mythicdoors.R

sealed interface IAppDestination {
    val route: String
    val screenName: String
}

sealed class AppDestination(
    override val route: String,
    override val screenName: String
): IAppDestination {
    data object Overview: AppDestination(route = "overview_screen", screenName = R.string.app_name.toString())
    data object Login: AppDestination(route ="login_screen", screenName = "Login")
    data object Register: AppDestination(route ="register_screen", screenName = "Register")
    data object GameOpts: AppDestination(route ="game_opts_screen", screenName = "Game Options")
    data object GameAction: AppDestination(route ="game_action_screen", screenName = "Game Action")
    data object ActionResult: AppDestination(route ="action_result_screen", screenName = "Action Result")
    data object Scores: AppDestination(route ="scores_screen", screenName = "Scores")
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