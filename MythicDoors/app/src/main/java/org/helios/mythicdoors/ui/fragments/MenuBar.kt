package org.helios.mythicdoors.ui.fragments

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.MENU_BAR_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.viewmodel.MenuViewModel

@Composable
fun MenuBar(navController: NavController) {
    val controller: MenuViewModel = (MainActivity.viewModelsMap[MENU_BAR_SCREEN_VIEWMODEL] as MenuViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isMenuOpen by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()

            .padding(ScreenConstants.AVERAGE_PADDING.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier
            .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                modifier = Modifier
                    .padding(ScreenConstants.AVERAGE_PADDING.dp)
                    .clickable { isMenuOpen = !isMenuOpen }
            )
            AnimatedVisibility(visible = isMenuOpen) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ScreenConstants.AVERAGE_PADDING.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { controller.navigateToOverview(scope, snackbarHostState) },
                        elevation = ButtonDefaults.buttonElevation(2.dp),) {
                        Text(text = "Main",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    Button(
                        onClick = { controller.closeApp() },
                        elevation = ButtonDefaults.buttonElevation(2.dp),) {
                        Text(text = "Exit",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }

        }
    }
}

