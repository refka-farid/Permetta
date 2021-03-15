package com.bravedroid.api.newapi

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus

class PermissionHelperNewApi {
    private var permissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit)? =
        null

    private val statusPermissionsMap: MutableMap<DangerousPermission, PermissionStatus> =
        mutableMapOf()

    fun requestPermission(
        requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
        activity: AppCompatActivity,
        permissions: Collection<DangerousPermission>,
        onPermissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit),
        onUserExplanation: ((Boolean) -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null,
    ) {
        permissionResponse = onPermissionResponse

        if (permissions.isEmpty()) {
            onError?.invoke(IllegalArgumentException(" param permissions should not be empty you have to declare your permissions when calling this method  "))
            return
        }

        if (VERSION.SDK_INT < VERSION_CODES.M) {
            requestPermissionDirectly(permissions, requestPermissionLauncher)
            return
        }

        when {
            areAllPermissionsGranted(permissions, activity) -> {
                for (permission in permissions) {
                    statusPermissionsMap[permission] = PermissionStatus.GRANTED
                }
                permissionResponse?.invoke(statusPermissionsMap)
                statusPermissionsMap.clear()
            }

            onUserExplanation != null && containsAnyRequestPermissionRational(
                permissions,
                activity,
            ) -> {
                onUserExplanation.invoke(true)
            }
            else -> requestPermissionDirectly(permissions, requestPermissionLauncher)
        }
    }

    @RequiresApi(VERSION_CODES.M)
    private fun areAllPermissionsGranted(
        permissions: Collection<DangerousPermission>,
        context: Context
    ) = permissions.all {
        context.checkSelfPermission(it.permissionName) == PackageManager.PERMISSION_GRANTED
    }

    private fun containsAnyRequestPermissionRational(
        permissions: Collection<DangerousPermission>,
        activity: AppCompatActivity
    ) = permissions.any {
        ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            it.permissionName
        )
    }

    fun requestPermissionDirectly(
        permissions: Collection<DangerousPermission>,
        requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
    ) {
        requestPermissionLauncher.launch(
            permissions.map {
                it.permissionName
            }.toTypedArray()
        )
    }
}
