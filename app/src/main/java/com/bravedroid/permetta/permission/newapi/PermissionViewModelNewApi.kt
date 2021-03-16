package com.bravedroid.permetta.permission.newapi

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus
import com.bravedroid.api.newapi.PermissionHelperNewApi
import com.bravedroid.permetta.permission.oldapi.SingleLiveEvent

class PermissionViewModelNewApi(
    private val permissionHelperNewApi: PermissionHelperNewApi,
) : ViewModel() {

    private val _statusPermissionsMap: MutableLiveData<Map<DangerousPermission, PermissionStatus>> =
        MutableLiveData()

    val statusPermissionsMap: LiveData<(Map<DangerousPermission, PermissionStatus>)> =
        _statusPermissionsMap

    private val _canShowUserExplanation = SingleLiveEvent<Boolean>()
    val canShowUserExplanation: SingleLiveEvent<Boolean> = _canShowUserExplanation

    fun register(activity: AppCompatActivity) {
        permissionHelperNewApi.register(activity)
    }

    fun requestPermission(
        activity: AppCompatActivity,
        permissions: Collection<DangerousPermission>,
    ) {
        permissionHelperNewApi.requestPermission(
            activity,
            permissions,
            ::onPermissionsResponse,
            ::onUserExplanation,
        )
    }

    fun requestPermissionDirectly(
        permissions: Collection<DangerousPermission>,
    ) {
        permissionHelperNewApi.requestPermissionDirectly(permissions)
    }

    private fun onUserExplanation(canShowUserExplanation: Boolean) {
        _canShowUserExplanation.value = canShowUserExplanation
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
