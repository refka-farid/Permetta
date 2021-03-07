package com.bravedroid.permetta.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bravedroid.permetta.base.BaseFragment
import com.bravedroid.permetta.databinding.FragmentPermissionBinding
import com.bravedroid.permetta.permission.LogHelper.logInformation


/**
 * A simple [Fragment] subclass.
 * Use the [PermissionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PermissionFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentPermissionBinding? = null

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PermissionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PermissionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        @JvmStatic
        fun newInstance() = PermissionFragment()

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            grantResults.forEachIndexed { index, element ->
                if (element == PackageManager.PERMISSION_GRANTED) {
                    Log.d(
                        "PermissionOld",
                        " PERMISSION_GRANTED ${permissions[index]} from fragment ${hashCode()}"
                    )
                } else {
                    Log.d(
                        "PermissionOld",
                        " PERMISSION_DENIED ${permissions[index]} from fragment ${hashCode()}"
                    )
                }
            }
        }
    }
}
