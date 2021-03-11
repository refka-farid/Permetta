package com.bravedroid.permetta.permission.oldapi

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus
import com.bravedroid.api.old.PermissionHelper

class PermissionViewModel(
    private val permissionHelper: PermissionHelper,
) : ViewModel() {

    private val _statusPermissionsMap: MutableLiveData<Map<DangerousPermission, PermissionStatus>> =
        MutableLiveData()

    val statusPermissionsMap: LiveData<(Map<DangerousPermission, PermissionStatus>)> =
        _statusPermissionsMap

    fun requestPermission(
        activity: AppCompatActivity,
        permissions: Collection<DangerousPermission>,
    ) {
        permissionHelper.requestPermission(
            activity,
            permissions,
            ::onPermissionsResponse,
        )
    }

    fun requestPermission(
        fragment: Fragment,
        permissions: Collection<DangerousPermission>,
    ) {
        permissionHelper.requestPermission(
            fragment,
            permissions,
            ::onPermissionsResponse,
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun onPermissionsResponse(statusPermissionsMap: Map<DangerousPermission, PermissionStatus>) {
        _statusPermissionsMap.value = statusPermissionsMap
        statusPermissionsMap.filter {
            it.value == PermissionStatus.GRANTED
        }.forEach {
            Log.d("PERMISSION_GRANTED", "${it.key} ")
        }

        statusPermissionsMap.filter {
            it.value == PermissionStatus.DENIED
        }.forEach {
            Log.d("PERMISSION_DENIED", "${it.key} ")
        }
    }
}
