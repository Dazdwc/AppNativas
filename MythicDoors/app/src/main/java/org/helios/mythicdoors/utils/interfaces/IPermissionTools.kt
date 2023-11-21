package org.helios.mythicdoors.utils.interfaces

import android.content.Context

interface IPermissionTools {
    fun isPermissionGranted(context: Context, permission: String): Boolean
    fun arePermissionsGranted(context: Context, permissions: Array<String>): Boolean
    fun requestPermission(context: Context, permission: String, requestCode: Int)
    fun requestPermissions(context: Context, permissions: Array<String>, requestCode: Int)
    fun verifyPermissionsGranted(grantResults: IntArray, permissions: Array<String>, requiredPermissions: Set<String>): Boolean
}