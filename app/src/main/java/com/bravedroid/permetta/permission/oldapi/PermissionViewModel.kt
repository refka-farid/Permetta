package com.bravedroid.permetta.permission.oldapi

import android.app.Activity
import android.content.Context
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

    private val _canShowUserExplanation = SingleLiveEvent<Boolean>()
    val canShowUserExplanation: SingleLiveEvent<Boolean> = _canShowUserExplanation

    fun requestPermission(
        activity: AppCompatActivity,
        permissions: Collection<DangerousPermission>,
    ) {
        permissionHelper.requestPermission(
            activity,
            permissions,
            ::onPermissionsResponse,
            ::onUserExplanation,
        )
    }

    fun requestPermission(
        fragment: Fragment,
        context: Context,
        permissions: Collection<DangerousPermission>,
    ) {
        permissionHelper.requestPermission(
            fragment,
            context,
            permissions,
            ::onPermissionsResponse,
            ::onUserExplanation,
        )
    }

    fun requestPermissionDirectly(
        fragment: Fragment,
        permissions: Collection<DangerousPermission>,
    ) {
        permissionHelper.requestPermissionDirectly(fragment, permissions)
    }


fun requestPermissionDirectly(
        activity: AppCompatActivity,
        permissions: Collection<DangerousPermission>,
    ) {
        permissionHelper.requestPermissionDirectly(activity, permissions)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
