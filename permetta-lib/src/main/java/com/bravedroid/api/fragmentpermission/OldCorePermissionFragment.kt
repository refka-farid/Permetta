package com.bravedroid.api.fragmentpermission

import android.content.pm.PackageManager
import android.os.Bundle
import com.bravedroid.api.BaseCoreFragment
import com.bravedroid.api.DangerousPermission
import com.bravedroid.api.PermissionStatus
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

open class OldCorePermissionFragment : BaseCoreFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var mOnPermissionResponse: ((Map<DangerousPermission, PermissionStatus>) -> Unit)? =
        null

    private var requestCodeRandom: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        fun newInstance(param1: String, param2: String) =
            OldCorePermissionFragment()
    }
}
