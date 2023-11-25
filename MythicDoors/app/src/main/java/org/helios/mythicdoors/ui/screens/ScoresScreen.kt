package org.helios.mythicdoors.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.ui.fragments.AudioPlayer
import org.helios.mythicdoors.ui.fragments.MenuBar
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.SCORES_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.ScoresScreenViewModel
import org.helios.mythicdoors.viewmodel.tools.SoundManagementViewModel

/*
* Habilitamos la API ExperimentalFoundationApi para poder usar el encabezado fijo de LazyColumn
*/
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScoresScreen(navController: NavController) {
    val controller: ScoresScreenViewModel = (MainActivity.viewModelsMap[SCORES_SCREEN_VIEWMODEL] as ScoresScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val soundManager: SoundManagementViewModel = (MainActivity.viewModelsMap[AppConstants.ScreensViewModels.SOUND_MANAGEMENT_SCREEN_VIEWMODEL] as SoundManagementViewModel)
        .apply { loadSoundsIfNeeded() }

    var singlePlayerGamesCollection by remember { mutableStateOf(emptyList<Game>()) }
    LaunchedEffect(controller) {
        controller.loadPlayerGamesList()
    }
    /*
    * A implementar en la fase 3
    val multiPlayerGamesCollection: Game by lazy { controller.loadMultiPlayerGames() }
    */

    DisposableEffect(Unit) {
        onDispose {
            soundManager.stopPlayingSounds()
        }
    }

    soundManager.playSound(R.raw.scores_screen_sound)
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background) {
        BoxWithConstraints {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .zIndex(999f)
                    .padding(ScreenConstants.AVERAGE_PADDING.dp),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator(controller)
            }


            Column {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(
                            top = ScreenConstants.DOUBLE_PADDING.dp,
                            bottom = ScreenConstants.DOUBLE_PADDING.dp
                        )
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                )
                Text(
                    text = stringResource(id = R.string.ladder),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(
                            bottom = ScreenConstants.DOUBLE_PADDING.dp
                        )
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = R.string.single_player_stats),
                        style = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp,
                                shadow = Shadow(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    offset = Offset(1.5f, 1.5f),
                                    blurRadius = 1f
                                )
                        ),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .weight(1f)
                            .padding(
                                bottom = ScreenConstants.AVERAGE_PADDING.dp,
                                start = ScreenConstants.AVERAGE_PADDING.dp
                            ),
                    )
                    Image(painter = painterResource(id = R.drawable.clapvalentine),
                        contentDescription = "Image of a Claptrap to decorate the single-player table",
                        modifier = Modifier
                            .height(ScreenConstants.SMALL_IMAGE_HEIGHT.dp)
                            .padding(start = ScreenConstants.AVERAGE_PADDING.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(top = ScreenConstants.AVERAGE_PADDING.dp))
                LazyColumn {
                    stickyHeader { Header() }

                    singlePlayerGamesCollection = controller.userGamesList.value.orEmpty()

                    items(singlePlayerGamesCollection) { item ->
                        ItemRow(item)
                    }
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = ScreenConstants.AVERAGE_PADDING.dp,
                        bottom = ScreenConstants.AVERAGE_PADDING.dp
                    ),
                    horizontalArrangement = Arrangement.Center,
                ){
                    Button(onClick = { controller.returnToGameOptionsScreen(scope, snackbarHostState) },
                        elevation = ButtonDefaults.buttonElevation(2.dp),
                        modifier = Modifier
                            .width(ScreenConstants.BUTTON_WIDTH.dp)
                    ) {
                        Text(text = stringResource(id = R.string.back),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Header() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary)
        .border(1.dp, MaterialTheme.colorScheme.tertiary)
        .padding(ScreenConstants.AVERAGE_PADDING.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.ladder_hd_player),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
                .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
        )
        Text(text = stringResource(id = R.string.ladder_hd_level),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
                .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
        )
        Text(text = stringResource(id = R.string.ladder_hd_score),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
                .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
        )
    }
}

@Composable
fun ItemRow(item: Game?){
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary)
        .border(1.dp, MaterialTheme.colorScheme.tertiary)
        .padding(ScreenConstants.AVERAGE_PADDING.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item?.getUser()?.getName() ?: "Unknown",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
                .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
        )
        Text(text = item?.getLevel()?.toString() ?: "0",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
                .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
        )
        Text(text = item?.getScore()?.toString() ?: "0",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
                .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
        )
    }
}

@Composable
private fun LoadingIndicator(controller: ScoresScreenViewModel) {
    val isLoading by controller.loading.observeAsState(false)

    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .padding(ScreenConstants.AVERAGE_PADDING.dp),
            color = MaterialTheme.colorScheme.secondary,
        )
        Text(text = stringResource(id = R.string.loading_indicator_msg),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .wrapContentSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScoresScreenPreview() {
    ScoresScreen(navController = NavController(LocalContext.current))
}