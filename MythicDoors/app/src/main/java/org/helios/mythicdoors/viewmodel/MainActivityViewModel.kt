package org.helios.mythicdoors.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp

class MainActivityViewModel(
    private val dataController: DataController
<<<<<<< HEAD
): ViewModel() {}
=======
): ViewModel() {
    private val navController: NavController
        get() { return _navController }
    private val navFunctions: INavFunctions by lazy { NavFunctionsImp.getInstance(navController) }

    private lateinit var _navController: NavController
    fun setNavController(navController: NavController) {
        _navController = navController
    }


    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        if (!visiblePermissionDialogQueue.isEmpty()) visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) visiblePermissionDialogQueue.add(permission)
    }
}

>>>>>>> ba1b62bf32fc7c0a2ed6bab98a51b52ccd7bb1e3
