package com.bravedroid.api.entities

import android.Manifest

enum class DangerousPermission(val permissionName: String) {
    CAMERA(Manifest.permission.CAMERA),
    ACCESS_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE);

    companion object {
        fun fromPermissionName(permissionName: String) =
            values().first {
                it.permissionName == permissionName
            }
    }
}
