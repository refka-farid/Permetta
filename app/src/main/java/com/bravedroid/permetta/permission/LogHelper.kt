package com.bravedroid.permetta.permission

import android.util.Log


object LogHelper {
    private const val TAG = "LIFE_CYCLE"

    fun Any.logLifeCycle(methodName: String, componentName: String? = null) {
        Log.d(
            TAG,
            "$methodName ${componentName ?: this.javaClass.simpleName} ${this.hashCode()} } "
        )
    }

    fun logInformation(info: String, tag: String = TAG) {
        Log.i(tag, "$info ")
    }
}
