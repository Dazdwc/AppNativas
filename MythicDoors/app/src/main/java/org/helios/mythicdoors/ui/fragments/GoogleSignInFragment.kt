package org.helios.mythicdoors.ui.fragments

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.helios.mythicdoors.R
import org.helios.mythicdoors.presentation.sign_in.SignInState
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants

@Composable
fun GoogleSignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
) {
    val context: Context = LocalContext.current

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
            .fillMaxSize()
            .padding(ScreenConstants.AVERAGE_PADDING.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onSignInClick
        ) {
            Text(text = context.getString(R.string.sign_in_with_google))
        }
    }
}