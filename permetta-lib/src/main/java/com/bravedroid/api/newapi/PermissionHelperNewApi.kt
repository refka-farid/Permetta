package com.bravedroid.api.newapi

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus

class PermissionHelperNewApi {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    private var permissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit)? =
        null

    private val statusPermissionsMap: MutableMap<DangerousPermission, PermissionStatus> =
        mutableMapOf()


    fun register(activity: AppCompatActivity) {
        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { allPermissions ->
            allPermissions.forEach { (s, b) ->
                val fromPermissionName = DangerousPermission.fromPermissionName(s)
                if (b) {
                    statusPermissionsMap[fromPermissionName] = PermissionStatus.GRANTED
                } else {
                    statusPermissionsMap[fromPermissionName] = PermissionStatus.DENIED
                }
            }

            permissionResponse?.invoke(statusPermissionsMap)
            permissionResponse = null
            statusPermissionsMap.clear()
        }
    }

    fun requestPermission(
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
            requestPermissionDirectly(permissions)
            return
        }

        when {
            areAllPermissionsGranted(permissions, activity) -> {
                for (permission in permissions) {
                    statusPermissionsMap[permission] = PermissionStatus.GRANTED
                }
                permissionResponse?.invoke(statusPermissionsMap)
                permissionResponse = null
                statusPermissionsMap.clear()
            }

            onUserExplanation != null && containsAnyRequestPermissionRational(
                permissions,
                activity,
            ) -> {
                onUserExplanation.invoke(true)
            }
            else -> requestPermissionDirectly(permissions)
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
    ) {
        requestPermissionLauncher.launch(
            permissions.map {
                it.permissionName
            }.toTypedArray()
        )
    }
}
