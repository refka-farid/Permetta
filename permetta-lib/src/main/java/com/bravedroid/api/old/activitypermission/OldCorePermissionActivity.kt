package com.bravedroid.api.old.activitypermission

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus
import kotlin.random.Random

open class OldCorePermissionActivity : BaseCoreActivity() {
    private var mOnPermissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit)? =
        null

    private var requestCodeRandom: Int = -1
    private val statusPermissionsMap: MutableMap<DangerousPermission, PermissionStatus> = mutableMapOf()

    fun requestPermission(
        permissions: Collection<DangerousPermission>,
        onPermissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit),
        onUserExplanation: (() -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null,
    ) {
        requestCodeRandom = Random.nextInt(100)
        mOnPermissionResponse = onPermissionResponse

        if (permissions.isEmpty()) {
            onError?.invoke(IllegalArgumentException(" param permissions should not be empty you have to declare your permissions when calling this method  "))
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            requestPermissionDirectly(this, permissions)
            return
        }

        when {
            areAllPermissionsGranted(permissions) -> {
                for (permission in permissions) {
                    statusPermissionsMap[permission] = PermissionStatus.GRANTED
                }
                onPermissionResponse.invoke(statusPermissionsMap)
                statusPermissionsMap.clear()
            }
            onUserExplanation != null && containsAnyRequestPermissionRational(
                permissions,
            ) -> {
                onUserExplanation.invoke()
            }
            else -> requestPermissionDirectly(this, permissions)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun areAllPermissionsGranted(
        permissions: Collection<DangerousPermission>,
    ) = permissions.all {
        checkSelfPermission(it.permissionName) == PackageManager.PERMISSION_GRANTED
    }

    private fun containsAnyRequestPermissionRational(
        permissions: Collection<DangerousPermission>,
    ) = permissions.any {
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            it.permissionName
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        mOnPermissionResponse?.invoke(statusPermissionsMap)
        mOnPermissionResponse = null
        statusPermissionsMap.clear()
    }
}
