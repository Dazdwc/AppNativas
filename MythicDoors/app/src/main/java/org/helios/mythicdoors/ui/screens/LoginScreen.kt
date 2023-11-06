package org.helios.mythicdoors.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    Surface(modifier = Modifier
        .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = "Mythic Doors",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 30.dp, bottom = 30.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally),
        )
        BoxWithConstraints(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Login",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 30.dp))
                Text(text = "Username",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 10.dp))
                Text(text = "Password",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 10.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current))
}
