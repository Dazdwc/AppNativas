package org.helios.mythicdoors.viewmodel

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.presentation.sign_in.FirebaseBaseAuthClient
import org.helios.mythicdoors.presentation.sign_in.IFirebaseBaseAuthClient
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
    private val firebaseAuth: IFirebaseBaseAuthClient by lazy { FirebaseBaseAuthClient.getInstance(FirebaseAuth.getInstance()) }

    val loginSuccessful: MutableLiveData<Boolean> = MutableLiveData(false)
    fun resetLoginSuccessful() { loginSuccessful.value = false }

    // Password must contain at least one digit [0-9], at least one uppercase Latin character [A-Z], at least one lowercase Latin character [a-z], at least one special character like ! @ # & ( ), at least 6 characters
    private val passwordPattern: Regex = Regex("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$")

    val loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val isGoogleSignInRequested: MutableLiveData<Boolean> = MutableLiveData(false)

    suspend fun login(userEmail: String,
                      userPassword: String,
                      scope: CoroutineScope,
                      snackbarHostState: SnackbarHostState
    ) {
        try {
            loading.value = true

            scope.launch {
                firebaseAuth.signInWithEmailAndPassword(
                    email = userEmail,
                    password = userPassword
                ).let {result ->
                    result.data?.let { user ->
                        val actualUser = dataController.getOneFSUser(user.email ?: throw Exception("Error getting email"))
                        actualUser?.let {
                            store.updateActualUser(it)
                            store.setAuthType(org.helios.mythicdoors.utils.AppConstants.AuthType.BASE)
                            loginSuccessful.postValue(true)
                        } ?: Log.e("LoginScreenViewModel", "login: ${result.errorMessage}")
                            .also {
                                firebaseAuth.signOut()
                                loginSuccessful.postValue(false)
                            }
                } ?: Log.e("LoginScreenViewModel", "login: ${result.errorMessage}")
                    .also {
                        firebaseAuth.signOut()
                        loginSuccessful.postValue(false)
                    }
                }
            }.join()
        } catch (e: Exception) {
            Log.e("LoginScreenViewModel", "Error logging in: ${e.message}").also { loginSuccessful.value = false }
        }
        finally {
            loading.value = false
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

    fun callForGoogleSignInRequest() {
        MainActivityViewModel.GoogleSignInRequestSetter.requestGoogleSignIn()
    }

    fun resetGoogleSignInRequest() {
        isGoogleSignInRequested.value = false
    }
}