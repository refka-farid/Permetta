package com.bravedroid.api.old.fragmentpermission

import android.content.pm.PackageManager
import com.bravedroid.api.BaseCoreFragment
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus
import kotlin.random.Random


open class OldCorePermissionFragment : BaseCoreFragment() {

    private var mOnPermissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit)? =
        null

    private var requestCodeRandom: Int = -1

    fun requestPermission(
        permissions: Collection<DangerousPermission>,
        onPermissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit),
        onError: ((Exception) -> Unit)? = null,
    ) {
        requestCodeRandom = Random.nextInt(100)
        mOnPermissionResponse = onPermissionResponse
        if (permissions.isNotEmpty()) {
            requestPermissions(
                permissions.map {
                    it.permissionName
                }.toTypedArray(),
                requestCodeRandom
            )
        } else {
            onError?.invoke(IllegalArgumentException(" param permissions should not be empty you have to declare your permissions when calling this method  "))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        mOnPermissionResponse?.invoke(statusPermissionsMap)
    }

    companion object {
        @JvmStatic
        fun newInstance() = OldCorePermissionFragment()
    }
}
