package com.bravedroid.permetta.permission.oldapi

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bravedroid.api.old.PermissionHelper

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PermissionViewModel::class.java)) {
            val permissionHelper: PermissionHelper = PermissionHelper()
            return PermissionViewModel(permissionHelper) as T
        } else {
            throw RuntimeException("Cannot create an instance of $modelClass")
        }
    }
}
