package com.bravedroid.permetta.permission.oldapi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus
import com.bravedroid.api.old.PermissionHelper
import com.bravedroid.permetta.base.BaseFragment
import com.bravedroid.permetta.base.LogHelper.logInformation
import com.bravedroid.permetta.databinding.FragmentPermissionBinding


class PermissionFragment : BaseFragment() {
    private var binding: FragmentPermissionBinding? = null
    private val permissionHelper: PermissionHelper = PermissionHelper()

    companion object {
        @JvmStatic
        fun newInstance() = PermissionFragment()
    }

    private class PermissionActivityObserver(private var context: Context?) : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun activityHasBeenCreated() {
            logInformation("activityHasBeenCreated from observer")
            (context as? PermissionActivity)?.lifecycle?.removeObserver(this) ?: return
            context = null
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun activityHasBeenStarted() {
            logInformation("activityHasBeenStarted")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun activityHasBeenResumed() {
            logInformation("activityHasBeenResumed")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun activityHasBeenPaused() {
            logInformation("activityHasBeenPaused")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun activityHasBeenStopped() {
            logInformation("activityHasBeenStopped")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun activityHasBeenDestroyed() {
            logInformation("activityHasBeenDestroyed")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun activityHasBeenANY() {
            logInformation("activityHasBeenAny")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as AppCompatActivity).lifecycle.addObserver(PermissionActivityObserver(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPermissionBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.buttonPermission?.setOnClickListener {
            permissionHelper.requestPermission(
                this,
                listOf(
                    DangerousPermission.ACCESS_FINE_LOCATION,
                    DangerousPermission.CAMERA,
                ),
                ::onPermissionsResponse,
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun onPermissionsResponse(statusPermissionsMap: Map<DangerousPermission, PermissionStatus>) {
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
