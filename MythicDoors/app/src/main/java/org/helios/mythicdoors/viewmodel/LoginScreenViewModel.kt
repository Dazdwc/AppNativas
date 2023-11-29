package org.helios.mythicdoors.viewmodel

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.store.StoreManager

class LoginScreenViewModel(
    private val dataController: DataController
): ViewModel() {
    private val navController: NavController
        get() { return _navController }
    private val navFunctions: INavFunctions by lazy { NavFunctionsImp.getInstance(navController) }

    private lateinit var _navController: NavController
    fun setNavController(navController: NavController) {
        _navController = navController
    }

    private val store: StoreManager by lazy { StoreManager.getInstance() }

    val loginSuccessful: MutableLiveData<Boolean> = MutableLiveData(false)
    fun resetLoginSuccessful() { loginSuccessful.value = false }

    // El password debe contener al menos un número, una mayúscula y un carácter especial
    private val passwordPattern: Regex = Regex("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$")

    suspend fun login(userEmail: String,
                      userPassword: String,
                      scope: CoroutineScope,
                      snackbarHostState: SnackbarHostState
    ) {
        try {
            scope.launch {
                dataController.getAllUsers().orEmpty()
                    .find { it.getEmail() == userEmail && it.getPassword() == userPassword }
                    ?.let {
                        loginSuccessful.value = true
                        store.updateActualUser(it)
                }
            }.join()
        } catch (e: Exception) {
            Log.e("LoginScreenViewModel", "Error logging in: ${e.message}").also { loginSuccessful.value = false }
        }
        scope.launch {
            loginSuccessful.value?.takeIf { it }?.let { snackbarHostState.showSnackbar("Login successful!") }
                ?: snackbarHostState.showSnackbar("Login failed: User not found!")
        }
    }

    fun navigateToOverviewScreen(
        scope: CoroutineScope,
        snackbarHostState: SnackbarHostState
    ) {
        navFunctions.navigateToOverviewScreen(scope, snackbarHostState)
    }

    fun navigateRegisterScreen(
        scope: CoroutineScope,
        snackbarHostState: SnackbarHostState
    ) {
        navFunctions.navigateRegisterScreen(scope, snackbarHostState)
    }

    fun navigateToGameOptsScreen(
        scope: CoroutineScope,
        snackbarHostState: SnackbarHostState
    ) {
        navFunctions.navigateGameOptsScreen(scope, snackbarHostState)
    }

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 6 && password.contains(passwordPattern)
    }
}