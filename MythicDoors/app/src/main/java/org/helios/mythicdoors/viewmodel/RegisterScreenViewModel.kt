package org.helios.mythicdoors.viewmodel

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.store.StoreManager

class RegisterScreenViewModel(
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

    // El password debe contener al menos un número, una mayúscula y un carácter especial
    private val passwordPattern: Regex = Regex("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$")

    val registerSuccessful: MutableLiveData<Boolean> = MutableLiveData(false)
    fun resetRegisterSuccessful() { registerSuccessful.value = false }

    fun register(
        name: String,
        email: String,
        password: String,
        scope: CoroutineScope,
        snackbarHostState: SnackbarHostState) {
        try {
            scope.launch {
                dataController.saveUser(User.create(name, email, password))
                    .takeIf { it }?.let {
                    registerSuccessful.postValue(true)
                    store.updateActualUser(dataController.getLastUser() ?: throw Exception("Error getting last user"))
                    snackbarHostState.showSnackbar("Register successful!")
                } ?: snackbarHostState.showSnackbar("Register failed!")
            }
        } catch (e: Exception) {
            Log.e("RegisterScreenViewModel", "register: ${e.message}").also { registerSuccessful.postValue(false) }
        }
    }

    fun navigateLoginScreen(
        scope: CoroutineScope,
        snackbarHostState: SnackbarHostState
    ) {
        navFunctions.navigateToLoginScreen(scope, snackbarHostState)
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