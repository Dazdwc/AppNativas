package org.helios.mythicdoors.viewmodel

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.User
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.navigation.navigateSingleTopTo
import org.helios.mythicdoors.presentation.sign_in.GoogleAuthUiClient
import org.helios.mythicdoors.store.StoreManager
import org.helios.mythicdoors.utils.AppConstants

class MainActivityViewModel(
    private val dataController: DataController
): ViewModel() {
    private val navController: NavController
        get() { return _navController }
    private val navFunctions: INavFunctions by lazy { NavFunctionsImp.getInstance(navController) }

    private lateinit var _navController: NavController
    fun setNavController(navController: NavController) {
        _navController = navController
    }

    private val store by lazy { StoreManager.getInstance() }

    val visiblePermissionDialogQueue = mutableStateListOf<String>()
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    object GoogleSignInRequestSetter {
        val isGoogleSignInRequested: MutableLiveData<Boolean> = MutableLiveData(false)
        val isGoogleSignInSucceded: MutableLiveData<Boolean> = MutableLiveData(false)
        fun requestGoogleSignIn() {
            isGoogleSignInRequested.value = true
        }
    }

    fun dismissDialog() {
        if (!visiblePermissionDialogQueue.isEmpty()) visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) visiblePermissionDialogQueue.add(permission)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun updateLogedUser(
        googleAuthUiClient: GoogleAuthUiClient
    ) {
        val context = store.getContext()?: MainActivity.getContext()
        isLoading.value = true

        try {
            googleAuthUiClient.getSignedInUser()?.let { userData ->
                viewModelScope.launch {
                    val logedUser = dataController.getOneFSUser(userData.email ?: throw UserNotFoundException("No valid email"))

                    logedUser?.let { user ->
                        store.updateActualUser(user)
                        successToast()
                    } ?: User.create(
                        name = userData.name ?: context.getString(R.string.default_user_name),
                        email = userData.email,
                        password = context.getString(R.string.default_user_password),
                        ).run {
                            dataController.saveOneFSUser(this)
                            store.updateActualUser(this)
                            defaultPasswordToast()
                        }

                }

                GoogleSignInRequestSetter.isGoogleSignInSucceded.value = true
            } ?: userNotFoundToast()
        } catch (e: UserNotFoundException) {
            Log.e("MainActivityViewModel", "Error getting user: ${e.message}")
            userNotFoundToast()
        } catch (e: Exception) {
            Log.e("MainActivityViewModel", "Error getting user: ${e.message}")
            userNotFoundToast()
        } finally {
            isLoading.value = false
        }
    }

    private fun userNotFoundToast() {
        val context = store.getContext()
        Toast.makeText(
            context,
            context?.getString(R.string.user_not_found),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun defaultPasswordToast() {
        val context = store.getContext()
        Toast.makeText(
            context,
            context?.getString(R.string.default_password),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun successToast() {
        val context = store.getContext()
        Toast.makeText(
            context,
            context?.getString(R.string.success_google_login),
            Toast.LENGTH_SHORT
        ).show()
    }
}

class UserNotFoundException(message: String): Exception(message)