package org.helios.mythicdoors.navigation

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope

interface INavFunctions {
    fun navigateToLoginScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState)
    fun navigateToOverviewScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState)
    fun navigateRegisterScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState)
    fun navigateGameOptsScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState)
    fun navigateGameActionScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState)
    fun navigateActionResultScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState)
    fun navigateScoresScreen(scope: CoroutineScope, snackbarHostState: SnackbarHostState)
}