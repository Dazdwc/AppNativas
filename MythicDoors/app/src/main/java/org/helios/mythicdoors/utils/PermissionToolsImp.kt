package org.helios.mythicdoors.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.utils.interfaces.IPermissionTools

class PermissionToolsImp(): IPermissionTools {
    override fun isPermissionGranted(context: Context, permission: String): Boolean { return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED }

    override fun arePermissionsGranted(context: Context, permissions: Array<String>): Boolean { return permissions.all { isPermissionGranted(context, it) } }

    override fun requestPermission(context: Context, permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(context as MainActivity, arrayOf(permission), requestCode)
    }

    override fun requestPermissions(context: Context, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(context as MainActivity, permissions, requestCode)
    }

    override fun verifyPermissionsGranted(
        grantResults: IntArray,
        permissions: Array<String>,
        requiredPermissions: Set<String>
    ): Boolean {
        val grantedPermissions = mutableSetOf<String>()

        grantResults.forEachIndexed { index, result ->
            if (result == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permissions[index])
            }
        }

        return grantedPermissions.containsAll(requiredPermissions)
    }
}