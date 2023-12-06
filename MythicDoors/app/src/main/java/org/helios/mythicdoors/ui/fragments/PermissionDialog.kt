package org.helios.mythicdoors.ui.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission

@Composable
fun PermissionDialog(
    permission: String,
    permissionTextProvider: IPermissionTextProvider,
    isPermanentlyDecline: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if ((checkSelfPermission(LocalContext.current, permission)) == PackageManager.PERMISSION_GRANTED) {
        onOkClick()
        return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Permission required",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(isPermanentlyDecline = isPermanentlyDecline),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isPermanentlyDecline) onSettingsClick() else onOkClick()
                }
            ) {
                Text(
                    text = if (isPermanentlyDecline) "Settings" else "OK",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        modifier = modifier
    )
}

interface IPermissionTextProvider { fun getDescription(isPermanentlyDecline: Boolean): String }

class LocationPermissionTextProvider: IPermissionTextProvider {
    override fun getDescription(isPermanentlyDecline: Boolean): String {
        return if (isPermanentlyDecline) "This app needs to access your location to get your location. You can change this in the settings" else "This app needs to access your location to get your location"
    }
}

class InternalImageStorePermissionTextProvider: IPermissionTextProvider {
    override fun getDescription(isPermanentlyDecline: Boolean): String {
        return if (isPermanentlyDecline) "This app needs to access your internal storage to save the images you create. You can change this in the settings" else "This app needs to access your internal storage to save the images you create"
    }
}

class CalendarPermissionTextProvider: IPermissionTextProvider {
    override fun getDescription(isPermanentlyDecline: Boolean): String {
        return if (isPermanentlyDecline) "This app needs to access your calendar to save the events you create. You can change this in the settings" else "This app needs to access your calendar to save the events you create"
    }
}

class ForegroundServicePermissionTextProvider: IPermissionTextProvider {
    override fun getDescription(isPermanentlyDecline: Boolean): String {
        return if (isPermanentlyDecline) "This app needs to access your notifications service in order to show your usage notifications. You can change this in the settings" else "This app needs to access your notifications service in order to show you usage notifications"
    }
}