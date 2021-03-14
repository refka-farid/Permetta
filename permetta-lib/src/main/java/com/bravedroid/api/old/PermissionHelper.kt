package com.bravedroid.api.old

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
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

    private val statusPermissionsMap: MutableMap<DangerousPermission, PermissionStatus> =
        mutableMapOf()

    fun requestPermission(
        activity: AppCompatActivity,
        permissions: Collection<DangerousPermission>,
        onPermissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit),
        onUserExplanation: ((Boolean) -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null,
    ) {
        requestCodeRandom = Random.nextInt(100)
        permissionResponse = onPermissionResponse

        if (permissions.isEmpty()) {
            onError?.invoke(IllegalArgumentException(" param permissions should not be empty you have to declare your permissions when calling this method  "))
            return
        }

        if (VERSION.SDK_INT < VERSION_CODES.M) {
            requestPermissionDirectly(activity, permissions)
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
            else -> requestPermissionDirectly(activity, permissions)
        }
    }


    private fun requestPermissionDep(
        activity: AppCompatActivity,
        permissions: Collection<DangerousPermission>,
        onPermissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit),
        onError: ((Exception) -> Unit)? = null,
    ) {
        requestPermissionBlockDep(
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

    private fun requestPermissionBlockDep(
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

    fun requestPermission(
        fragment: Fragment,
        context: Context,
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
            requestPermissionDirectly(fragment, permissions)
            return
        }

        when {
            areAllPermissionsGranted(permissions, context) -> {
                for (permission in permissions) {
                    statusPermissionsMap[permission] = PermissionStatus.GRANTED
                }
                permissionResponse?.invoke(statusPermissionsMap)
                statusPermissionsMap.clear()
            }
            onUserExplanation != null && containsAnyRequestPermissionRational(
                permissions,
                (fragment.requireActivity()) as AppCompatActivity,
            ) -> {
                onUserExplanation.invoke(true)
            }
            else -> requestPermissionDirectly(fragment, permissions)
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
        fragment: Fragment,
        permissions: Collection<DangerousPermission>,
    ) {
        fragment.requestPermissions(
            permissions.map {
                it.permissionName
            }.toTypedArray(),
            requestCodeRandom
        )
    }

    fun requestPermissionDirectly(
        activity: AppCompatActivity,
        permissions: Collection<DangerousPermission>,
    ) {
        ActivityCompat.requestPermissions(
            activity,
            permissions.map {
                it.permissionName
            }.toTypedArray(),
            requestCodeRandom
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
        statusPermissionsMap.clear()
    }
}
