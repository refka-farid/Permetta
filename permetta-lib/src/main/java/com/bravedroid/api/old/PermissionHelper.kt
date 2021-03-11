package com.bravedroid.api.old

import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus
import kotlin.random.Random

class PermissionHelper {
    private var permissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit)? =
        null

    private var requestCodeRandom: Int = -1

    fun requestPermission(
        activity: AppCompatActivity,
        permissions: Collection<DangerousPermission>,
        onPermissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit),
        onError: ((Exception) -> Unit)? = null,
    ) {
        requestPermissionBlock(
            permissions,
            onPermissionResponse,
            onError,
            {
                ActivityCompat.requestPermissions(
                    activity,
                    permissions.map {
                        it.permissionName
                    }.toTypedArray(),
                    requestCodeRandom
                )
            }
        )
    }


    fun requestPermission(
        fragment: Fragment,
        permissions: Collection<DangerousPermission>,
        onPermissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit),
        onError: ((Exception) -> Unit)? = null,
    ) {
        requestPermissionBlock(
            permissions,
            onPermissionResponse,
            onError,
            {
                fragment.requestPermissions(
                    permissions.map {
                        it.permissionName
                    }.toTypedArray(),
                    requestCodeRandom
                )
            }
        )
    }

    private fun requestPermissionBlock(
        permissions: Collection<DangerousPermission>,
        onPermissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit),
        onError: ((Exception) -> Unit)? = null,
        action: () -> Unit
    ) {
        requestCodeRandom = Random.nextInt(100)
        permissionResponse = onPermissionResponse
        if (permissions.isNotEmpty()) {
            action()
        } else {
            onError?.invoke(IllegalArgumentException(" param permissions should not be empty you have to declare your permissions when calling this method  "))
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val statusPermissionsMap: MutableMap<DangerousPermission, PermissionStatus> = mutableMapOf()
        if (requestCode == requestCodeRandom) {
            grantResults.forEachIndexed { index, element ->
                val permission = DangerousPermission.fromPermissionName(permissions[index])
                if (element == PackageManager.PERMISSION_GRANTED) {
                    statusPermissionsMap[permission] = PermissionStatus.GRANTED
                } else {
                    statusPermissionsMap[permission] = PermissionStatus.DENIED
                }
            }
        }
        permissionResponse?.invoke(statusPermissionsMap)
        permissionResponse = null
    }
}
