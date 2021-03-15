package com.bravedroid.permetta.permission.oldapi

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus
import com.bravedroid.api.old.fragmentpermission.OldCorePermissionFragment
import com.bravedroid.permetta.R

class PermissionFragment : OldCorePermissionFragment() {
    private var binding: com.bravedroid.permetta.databinding.FragmentPermissionBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = com.bravedroid.permetta.databinding.FragmentPermissionBinding.inflate(inflater)
        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = PermissionFragment()
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
                ::showDialogForRationalDialog,
            )
        }
    }

    private fun showDialogForRationalDialog() {
        AlertDialog
            .Builder(requireContext())
            .setTitle(getString(R.string.rationale_title))
            .setMessage(getString(R.string.rationale_desc))
            .setPositiveButton(" Ask Permissions ")
            { _, _ ->
                requestPermissionDirectly(
                    this,
                    listOf(
                        DangerousPermission.ACCESS_FINE_LOCATION,
                        DangerousPermission.CAMERA,
                    )
                )
            }
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun onPermissionsResponse(statusPermissionsMap: Map<DangerousPermission, PermissionStatus>) {
        statusPermissionsMap.filter {
            it.value == PermissionStatus.GRANTED
        }.apply {
            if (isNotEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "${this.keys}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

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
