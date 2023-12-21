package org.helios.mythicdoors.ui.fragments

import android.content.Context
import android.content.res.Resources.Theme
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.navigation.navigateSingleTopTo
import org.helios.mythicdoors.presentation.sign_in.SignInState
import org.helios.mythicdoors.utils.AppConstants
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants
import org.helios.mythicdoors.viewmodel.MainActivityViewModel

@Composable
fun GoogleSignInScreen(
    controller: MainActivityViewModel,
    state: SignInState,
    onSignInClick: () -> Unit,
) {
    val context: Context = LocalContext.current
    val scroll = rememberScrollState()

    val isLoading by controller.isLoading.observeAsState(initial = false)

    LaunchedEffect(key1 = state.signInErrorMessage) {
        state.signInErrorMessage?.let {
            Toast.makeText(
                context,
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Box(
        modifier = Modifier
            .padding(ScreenConstants.AVERAGE_PADDING.dp),
        contentAlignment = Alignment.Center
    ) {
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
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(ScreenConstants.AVERAGE_PADDING.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Icon"
            )
            Spacer(modifier = Modifier.height(ScreenConstants.AVERAGE_PADDING.dp.times(2)))
            Button(
                onClick = onSignInClick,
            ) {
                Text(
                    text = context.getString(R.string.sign_in_with_google),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(modifier = Modifier.height(ScreenConstants.AVERAGE_PADDING.dp))
        }

    }
}