package com.bravedroid.permetta.permission.oldapi

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bravedroid.api.newapi.PermissionHelperNewApi
import com.bravedroid.api.old.PermissionHelper
import com.bravedroid.permetta.permission.newapi.PermissionViewModelNewApi

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PermissionViewModel::class.java) -> {
                val permissionHelper: PermissionHelper = PermissionHelper()
                PermissionViewModel(permissionHelper) as T
            }
            modelClass.isAssignableFrom(PermissionViewModelNewApi::class.java) -> {
                val permissionHelper: PermissionHelperNewApi = PermissionHelperNewApi()
                PermissionViewModelNewApi(permissionHelper) as T
            }
            else -> {
                throw RuntimeException("Cannot create an instance of $modelClass")
            }
        }
    }
}
