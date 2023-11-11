package org.helios.mythicdoors.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.viewmodel.ScoresScreenViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color



@Composable
fun ScoresScreen(navController: NavController) {
 //   val controller: ScoresScreenViewModel =
 //       MainActivity.viewModelsMap["scores-screen-viewmodel"] as ScoresScreenViewModel
 //   val scope = rememberCoroutineScope()
 //   val snackbarHostState = remember { SnackbarHostState() }

    Surface(
        modifier = Modifier,

        color = Color.DarkGray
    ){ Column(
        modifier = Modifier
            .size(300.dp)
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {Box(
        modifier = Modifier
            .background(Color.White)
            .width(300.dp)
            .height(500.dp)
        ){Text(
        text = "Aquí va el Score")
            }
        Spacer(modifier = Modifier.height(35.dp))
        Button(onClick = {
         //   controller.navigateToOptsScreen(navController, scope, snackbarHostState)
        }) {Text(text = "Main Menu")
        }

    }
}


}

@Preview(showSystemUi = true)
@Composable
fun PreviewScoresScreen(){
    ScoresScreen(navController = NavController(LocalContext.current))

}