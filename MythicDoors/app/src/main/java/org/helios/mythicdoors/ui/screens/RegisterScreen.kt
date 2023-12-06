package org.helios.mythicdoors.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.services.interfaces.LanguageChangeListener
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.ui.fragments.AudioPlayer
import org.helios.mythicdoors.ui.fragments.MenuBar
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.REGISTER_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.lenguage
import org.helios.mythicdoors.viewmodel.RegisterScreenViewModel

@Composable
fun RegisterScreen(navController: NavController) {
    val controller: RegisterScreenViewModel = (MainActivity.viewModelsMap[REGISTER_SCREEN_VIEWMODEL] as RegisterScreenViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    var userName: String by remember { mutableStateOf("") }
    var userEmail: String by remember { mutableStateOf("") }
    var isEmailValid: Boolean by remember { mutableStateOf(true) }
    var password: String by remember { mutableStateOf("") }
    var isPasswordValid: Boolean by remember { mutableStateOf(false) }
    var retypedPassword: String by remember { mutableStateOf("") }

    var passwordVisibilityOption: Boolean by remember { mutableStateOf(false) }
    val passwordVisibilityIcon: ImageVector = if (passwordVisibilityOption) {
        ImageVector.vectorResource(R.drawable.eye_500)
    } else {
        ImageVector.vectorResource(R.drawable.eye_off_500)
    }

    val registerSuccessful by controller.registerSuccessful.observeAsState(false)
    LaunchedEffect(registerSuccessful) {
        if (registerSuccessful) controller.navigateToGameOptsScreen(scope, snackbarHostState)
        controller.resetRegisterSuccessful()
    }
    var currentLanguage by remember { mutableStateOf("en") }
    val storeManager = StoreManager.getInstance()

    DisposableEffect(Unit) {
        val observer: LanguageChangeListener = object : LanguageChangeListener {
            override fun onLanguageChanged(newLanguage: String) {
                currentLanguage = newLanguage
            }
        }
        storeManager.addObserver(observer)
        onDispose {
            storeManager.removeObserver(observer)
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
            Column(
                modifier = Modifier
                    .width(maxWidth.minus(maxWidth * 0.20f))
                    .scrollable(scrollState, orientation = Orientation.Vertical)
                    .padding(
                        top = ScreenConstants.DOUBLE_PADDING.dp,
                        bottom = ScreenConstants.AVERAGE_PADDING.dp
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Mythic Doors",
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
                    text = lenguage["registernewplayer_$currentLanguage"]?:"Register New Player",
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
                        imageVector = ImageVector.vectorResource(id = R.drawable.user_add_500),
                        contentDescription = "User add icon",
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                    TextField(
                        modifier = Modifier
                            .padding(end = 48.dp)
                            .background(MaterialTheme.colorScheme.primary)
                            .border(1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                            .weight(1f),
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text(lenguage["name_$currentLanguage"]?:"Name") },
                        placeholder = {
                            Text(
                                lenguage["namenewplayer_$currentLanguage"]?:"A name for the new player",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        isError = userName.isBlank(),
                    )
                }
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
                        imageVector = ImageVector.vectorResource(id = R.drawable.user_add_500),
                        contentDescription = "User add icon",
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
                        label = { Text(lenguage["mail_$currentLanguage"]?:"Email") },
                        placeholder = {
                            Text(
                                lenguage["insertmail_$currentLanguage"]?:"Insert your email address",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        isError = !isEmailValid,
                    )
                }
                isEmailValid.takeIf { !it }?.run { Text(
                    text = lenguage["errorvalidmail_$currentLanguage"]?:"Please enter a valid email address",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = ScreenConstants.AVERAGE_PADDING.dp)
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
                        label = { Text(lenguage["insertpass_$currentLanguage"]?:"Insert a password") },
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
                    text = lenguage["messagevalidpassword_$currentLanguage"]?:"""Please enter a valid password:
                                |At least 6 characters
                                |At least one number
                                |At least one uppercase letter
                                |At least one special character
                            """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = ScreenConstants.AVERAGE_PADDING.dp)
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
                        value = retypedPassword,
                        onValueChange = { retypedPassword = it },
                        label = { Text(lenguage["retrypass_$currentLanguage"]?:"Retype your password") },
                        visualTransformation = passwordVisibilityOption.takeIf { it }
                            ?.let { VisualTransformation.None } ?: PasswordVisualTransformation(),
                        isError = !this.equals(password),
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
                    text = lenguage["diferentpass_$currentLanguage"]?:"""The password and the retyped password are different.""".trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = ScreenConstants.AVERAGE_PADDING.dp)
                ) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ScreenConstants.DOUBLE_PADDING.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            controller.register(userName, userEmail, password, scope, snackbarHostState)
                            if (registerSuccessful) controller.navigateToGameOptsScreen(scope, snackbarHostState)
                        },
                        enabled = isEmailValid && isPasswordValid && password == retypedPassword,
                        elevation = ButtonDefaults.buttonElevation(2.dp),
                        modifier = Modifier
                            .padding(end = ScreenConstants.AVERAGE_PADDING.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = lenguage["register_$currentLanguage"]?:"REGISTER",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    Button(
                        onClick = { controller.navigateLoginScreen(scope, snackbarHostState) },
                        elevation = ButtonDefaults.buttonElevation(2.dp),
                        modifier = Modifier
                            .padding(start = ScreenConstants.AVERAGE_PADDING.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = lenguage["back_$currentLanguage"]?:"BACK",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController = NavController(LocalContext.current))
}
