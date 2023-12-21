package org.helios.mythicdoors.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.entities.Game
import org.helios.mythicdoors.ui.fragments.LoadingIndicator
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.SCORES_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.ScoresScreenViewModel
import org.helios.mythicdoors.viewmodel.tools.SoundManagementViewModel

/*
* Habilitamos la API ExperimentalFoundationApi para poder usar el encabezado fijo de LazyColumn
*/
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScoresScreen(navController: NavController) {
    val controller: ScoresScreenViewModel = (MainActivity.viewModelsMap[SCORES_SCREEN_VIEWMODEL] as ScoresScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val soundManager: SoundManagementViewModel = (MainActivity.viewModelsMap[AppConstants.ScreensViewModels.SOUND_MANAGEMENT_SCREEN_VIEWMODEL] as SoundManagementViewModel)
        .apply { loadSoundsIfNeeded() }

    val isLoading by controller.loading.observeAsState(false)

    var singlePlayerGamesCollection by remember { mutableStateOf(emptyList<Game>()) }
    LaunchedEffect(controller) {
        controller.loadPlayerGamesList()
    }

    var multiPlayerGamesCollection by remember { mutableStateOf(emptyList<Game>()) }
    LaunchedEffect(controller) {
        controller.loadAllPlayersgameList()
    }

    DisposableEffect(Unit) {
        onDispose {
            soundManager.stopPlayingSounds()
        }
    }

    var isSinglePlayerSelected by remember { mutableStateOf(true) }
    var isMultiPlayerSelected by remember { mutableStateOf(false) }

    soundManager.playSound(R.raw.scores_screen_sound)
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background) {
        BoxWithConstraints {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .zIndex(999f)
                    .padding(ScreenConstants.AVERAGE_PADDING.dp),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator(
                    isLoading = isLoading,
                )
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ScreenConstants.AVERAGE_PADDING.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        isSinglePlayerSelected = true
                        isMultiPlayerSelected = false
                    }
                    ) {
                        Text(text = "Single Player",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    Button(onClick = {
                        isSinglePlayerSelected = false
                        isMultiPlayerSelected = true
                    }
                    ) {
                        Text(text = "Multi Player",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }

                if (isSinglePlayerSelected) {
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

                        singlePlayerGamesCollection = controller.actualUserGamesList.value.orEmpty()

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

                if (isMultiPlayerSelected) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.multi_player_stats),
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
                        Image(painter = painterResource(id = R.drawable.clapwub),
                            contentDescription = "Image of a Claptrap to decorate the multi-player table",
                            modifier = Modifier
                                .height(ScreenConstants.SMALL_IMAGE_HEIGHT.dp)
                                .padding(start = ScreenConstants.AVERAGE_PADDING.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(top = ScreenConstants.AVERAGE_PADDING.dp))
                    LazyColumn {
                        stickyHeader { Header() }

                        multiPlayerGamesCollection = controller.allUsersGameList.value.orEmpty()

                        items(multiPlayerGamesCollection) { item ->
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
