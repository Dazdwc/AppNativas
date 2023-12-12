package org.helios.mythicdoors.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import org.helios.mythicdoors.R

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    Splash()
    LaunchedEffect(key1 = true) {
        delay(5000L)
        onTimeout()
    }
}

@Composable
fun Splash() {
    val scale = remember { Animatable(0f) }
    val targetScale = 0.4f

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = targetScale,
            animationSpec = tween(
                durationMillis = 3000,
                delayMillis = 500,
                easing = FastOutSlowInEasing
            )
        )
    }

    Scaffold(topBar = {},
        bottomBar = {},
        snackbarHost = {}
    ) {innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(R.color.charcoal))
                .padding(innerPadding)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(R.color.charcoal))
                    .padding(20.dp)
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = R.mipmap.ic_logo_helios),
                        contentDescription = "Helios Logo",
                        modifier = Modifier
                            .size(500.dp)
                            .padding(10.dp)
                            .scale(scale.value)
                    )
                    Text(
                        text = stringResource(id = R.string.helios_game_studio),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
            }
        }
    }


}