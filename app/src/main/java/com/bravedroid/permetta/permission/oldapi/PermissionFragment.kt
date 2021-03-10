package com.bravedroid.permetta.permission.oldapi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bravedroid.api.DangerousPermission
import com.bravedroid.api.PermissionStatus
import com.bravedroid.api.fragmentpermission.OldCorePermissionFragment
import com.bravedroid.permetta.databinding.FragmentPermissionBinding
import com.bravedroid.permetta.base.LogHelper.logInformation


class PermissionFragment : OldCorePermissionFragment() {
    private var binding: FragmentPermissionBinding? = null

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
        (context as PermissionActivity).lifecycle.addObserver(PermissionActivityObserver(context))
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
            requestPermission(
                listOf(
                    DangerousPermission.ACCESS_FINE_LOCATION,
                    DangerousPermission.CAMERA,
                ),
                ::onPermissionsResponse,
            )
        }
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
