package org.helios.mythicdoors.viewmodel.tools

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.helios.mythicdoors.presentation.sign_in.SignInResult
import org.helios.mythicdoors.presentation.sign_in.SignInState

class SignInViewModel: ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInErrorMessage = result.errorMessage
        ) }
    }

    fun resetSignInState() { _state.update { SignInState() } }
}