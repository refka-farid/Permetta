package com.bravedroid.api

import android.util.Log

object LogCoreHelper {
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
