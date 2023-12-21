package org.helios.mythicdoors.ui.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.ui.fragments.LoadingIndicator
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.LOGIN_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.MAINACTIVITY_SCREEN_VIEWMODEL
import org.helios.mythicdoors.viewmodel.LoginScreenViewModel
import org.helios.mythicdoors.viewmodel.MainActivityViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LoginScreen(navController: NavController) {
    val controller: LoginScreenViewModel = (MainActivity.viewModelsMap[LOGIN_SCREEN_VIEWMODEL] as LoginScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scroll = rememberScrollState()

    var userEmail: String by remember { mutableStateOf("") }
    var isEmailValid: Boolean by remember { mutableStateOf(true) }
    var password: String by remember { mutableStateOf("") }
    var isPasswordValid: Boolean by remember { mutableStateOf(false) }

    var passwordVisibilityOption: Boolean by remember { mutableStateOf(false) }
    val passwordVisibilityIcon: ImageVector = if (passwordVisibilityOption) {
        ImageVector.vectorResource(R.drawable.eye_500)
    } else {
        ImageVector.vectorResource(R.drawable.eye_off_500)
    }

    val loginSuccessful by controller.loginSuccessful.observeAsState(false)
    LaunchedEffect(loginSuccessful, snackbarHostState) {
        if (loginSuccessful) {
            Toast.makeText(
                context,
                context.getString(R.string.login_successful),
                Toast.LENGTH_LONG
            ).show()

            controller.resetLoginSuccessful()
            controller.resetGoogleSignInRequest()
            controller.navigateToGameOptsScreen(scope, snackbarHostState)
        }
    }

    val isLoading by controller.loading.observeAsState(initial = false)

    val isGoogleSignInRequested by controller.isGoogleSignInRequested.observeAsState(initial = false)
    LaunchedEffect(key1 = isGoogleSignInRequested) {
        if (isGoogleSignInRequested) {
            controller.callForGoogleSignInRequest()
        }
    }

    val isGoogleSignInSucceded by MainActivityViewModel.GoogleSignInRequestSetter.isGoogleSignInSucceded.observeAsState(initial = false)
    LaunchedEffect(key1 = isGoogleSignInSucceded) {
        if (isGoogleSignInSucceded) {
            controller.resetGoogleSignInRequest()
            controller.navigateToGameOptsScreen(scope, snackbarHostState)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val maxWidth = this.maxWidth

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

            Column(
                modifier = Modifier
                    .width(maxWidth.minus(maxWidth * 0.20f))
                    .padding(bottom = ScreenConstants.AVERAGE_PADDING.dp)
                    .verticalScroll(scroll),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    text = stringResource(id = R.string.login),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 50.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = ScreenConstants.AVERAGE_PADDING.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
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
                        label = { Text(stringResource(id = R.string.email)) },
                        placeholder = {
                            Text(
                                stringResource(id = R.string.email_helper),
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        isError = !isEmailValid,
                    )
                }
                isEmailValid.takeIf { !it }?.run { Text(
                    text = stringResource(id = R.string.email_validator),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 10.dp)
                ) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = ScreenConstants.AVERAGE_PADDING.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
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
                        onValueChange = {
                            password = it
                            isPasswordValid = controller.validatePassword(password)
                        },
                        label = { Text(stringResource(id = R.string.password)) },
                        visualTransformation = passwordVisibilityOption.takeIf { it }
                            ?.let { VisualTransformation.None } ?: PasswordVisualTransformation(),
                        isError = !isPasswordValid,
                    )
                    Icon(
                        modifier = Modifier
                            .padding(start = ScreenConstants.AVERAGE_PADDING.dp)
                            .size(40.dp, 40.dp)
                            .clickable { passwordVisibilityOption = !passwordVisibilityOption },
                        imageVector = passwordVisibilityIcon,
                        contentDescription = "Eye icon",
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }
                isPasswordValid.takeIf { !it }?.run { Text(
                    text =  stringResource(id = R.string.password_requirements).trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = ScreenConstants.AVERAGE_PADDING.dp)
                ) }

                GoogleIdLoginRow(
                    controller = controller
                )

                NewUserRow(
                    controller = controller,
                    scope = scope,
                    snackbarHostState = snackbarHostState
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ScreenConstants.DOUBLE_PADDING.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    controller.login(userEmail, password, scope, snackbarHostState)

                                    withContext(Dispatchers.Main) {
                                        if (!loginSuccessful) {
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.login_failed),
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.login_failed),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        },
                        enabled = isEmailValid && isPasswordValid,
                        elevation = ButtonDefaults.buttonElevation(2.dp),
                        modifier = Modifier
                            .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.login).uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    Button(
                        onClick = { controller.navigateToOverviewScreen(scope, snackbarHostState) },
                        elevation = ButtonDefaults.buttonElevation(2.dp),
                        modifier = Modifier
                            .padding(start = ScreenConstants.AVERAGE_PADDING.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel).uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NewUserRow(
    controller: LoginScreenViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = ScreenConstants.AVERAGE_PADDING.dp,
                bottom = ScreenConstants.DOUBLE_PADDING.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
                .size(40.dp, 40.dp)
                .clickable {
                    controller.navigateRegisterScreen(scope, snackbarHostState)
                },
            imageVector = ImageVector.vectorResource(id = R.drawable.user_add_500),
            contentDescription = "Add user account icon",
            tint = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.width(ScreenConstants.AVERAGE_PADDING.dp))
        Text(
            text = stringResource(id = R.string.new_user),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable {
                    controller.navigateRegisterScreen(scope, snackbarHostState)
                }
                .padding(bottom = ScreenConstants.AVERAGE_PADDING.dp),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun GoogleIdLoginRow(
    controller: LoginScreenViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ScreenConstants.AVERAGE_PADDING.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = "Google Icon",
            modifier = Modifier
                .size(ScreenConstants.IMAGE_SIZE.dp)
                .clickable { controller.isGoogleSignInRequested.value = true }
        )
        Spacer(modifier = Modifier.width(ScreenConstants.AVERAGE_PADDING.dp))
        Text(
            text = stringResource(id = R.string.login_with_google),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.clickable { controller.isGoogleSignInRequested.value = true }
        )
    }
}