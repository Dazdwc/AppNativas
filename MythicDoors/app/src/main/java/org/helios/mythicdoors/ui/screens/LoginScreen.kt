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
import org.helios.mythicdoors.viewmodel.LoginScreenViewModel
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun LoginScreen(navController: NavController) {
    val controller: LoginScreenViewModel = MainActivity.viewModelsMap["login-screen-viewmodel"] as LoginScreenViewModel
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var userEmail: String by remember { mutableStateOf("") }
    var isEmailValid: Boolean by remember { mutableStateOf(true) }
    var password: String by remember { mutableStateOf("") }
    var passwordVisibilityOption: Boolean by remember { mutableStateOf(false) }
    val passwordVisibilityIcon: ImageVector = if (passwordVisibilityOption) {
        ImageVector.vectorResource(R.drawable.eye_500)
    } else {
        ImageVector.vectorResource(R.drawable.eye_off_500)
    }
    val loginSuccessful by controller.loginSuccessful.observeAsState(false)


    Scaffold(
        topBar = {
            // TODO: Add top bar
        },
        bottomBar = {
            // TODO: Add bottom bar
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
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
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val maxWidth = this.maxWidth
                Column(
                    modifier = Modifier
                        .width(maxWidth.minus(maxWidth * 0.20f))
                        .padding(top = 30.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 50.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(40.dp, 40.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.user_check_500),
                            contentDescription = "User account icon",
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                        TextField(
                            modifier = Modifier
                                .padding(end = 48.dp)
                                .background(MaterialTheme.colorScheme.primary)
                                .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                                .weight(1f),
                            value = userEmail,
                            onValueChange = {
                                userEmail = it
                                isEmailValid = controller.validateEmail(userEmail)
                            },
                            label = { Text("Email") },
                            placeholder = {
                                Text(
                                    "Your Email",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            isError = !isEmailValid,
                        )
                    }
                    isEmailValid.takeIf { !it }?.run { Text(
                        text = "Please enter a valid email address",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(40.dp, 40.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.key_500),
                            contentDescription = "Key icon",
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                        TextField(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary)
                                .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                                .weight(1f),
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            visualTransformation = passwordVisibilityOption.takeIf { it }
                                ?.let { VisualTransformation.None } ?: PasswordVisualTransformation(),
                        )
                        Icon(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(40.dp, 40.dp)
                                .clickable { passwordVisibilityOption = !passwordVisibilityOption },
                            imageVector = passwordVisibilityIcon,
                            contentDescription = "Eye icon",
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 30.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(40.dp, 40.dp)
                                .clickable { controller.navigateRegisterScreen(navController, scope, snackbarHostState) },
                            imageVector = ImageVector.vectorResource(id = R.drawable.user_add_500),
                            contentDescription = "Add user account icon",
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                        Text(
                            text = "New User",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .clickable { controller.navigateRegisterScreen(navController, scope, snackbarHostState) }
                                .padding(bottom = 20.dp),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                controller.login(userEmail, password, scope, snackbarHostState)
                                if (loginSuccessful) controller.navigateToGameOptsScreen(navController, scope, snackbarHostState)
                            },
                            enabled = userEmail.isNotEmpty() && password.length >= 6,
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "LOGIN",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Button(
                            onClick = { controller.navigateToOverviewScreen(navController, scope, snackbarHostState) },
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "CANCEL",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current))
}