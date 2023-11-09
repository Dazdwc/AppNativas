package org.helios.mythicdoors.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.navigation.goToOverviewScreen
import org.helios.mythicdoors.navigation.goToRegisterScreen
import org.helios.mythicdoors.navigation.goToOptsScreen

class LoginScreenViewModel(
    private val dataController: DataController
) {
    val loginSuccessful: MutableLiveData<Boolean> = MutableLiveData(false)
    private val store: StoreManager by lazy { StoreManager.getInstance() }

    fun login(userEmail: String,
              userPassword: String,
              scope: CoroutineScope,
              snackbarHostState: SnackbarHostState
    ) {
        try {
            scope.launch { dataController.getAllUsers()?.find { it.getEmail() == userEmail && it.getPassword() == userPassword }?.let {
                loginSuccessful.postValue(true)
                store.updateActualUser(it)
            } ?: loginSuccessful.postValue(false)  }

        } catch (e: Exception) {
            e.printStackTrace().also { loginSuccessful.postValue(false) }
        } finally {
            scope.launch {
                loginSuccessful.value?.takeIf { it }?.let { snackbarHostState.showSnackbar("Login successful!") }
                    ?: snackbarHostState.showSnackbar("Login failed: User not found!")
            }
        }
    }

    fun navigateToOverviewScreen(navController: NavController,
                                 scope: CoroutineScope,
                                 snackbarHostState: SnackbarHostState
    ) {
        goToOverviewScreen(navController,scope,snackbarHostState)
    }

    fun navigateRegisterScreen(navController: NavController,
                               scope: CoroutineScope,
                               snackbarHostState: SnackbarHostState
    ) {
        goToRegisterScreen(navController, scope, snackbarHostState)
    }




    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun navigateToGameOptsScreen(navController: NavController,
                                 scope: CoroutineScope,
                                 snackbarHostState: SnackbarHostState) {
        goToOptsScreen(navController, scope, snackbarHostState)

    }
}