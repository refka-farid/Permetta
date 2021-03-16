package com.bravedroid.permetta.permission.newapi

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus
import com.bravedroid.permetta.R
import com.bravedroid.permetta.permission.oldapi.ViewModelFactory


class A : AppCompatActivity() {

    private lateinit var viewModel: PermissionViewModelNewApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = com.bravedroid.permetta.databinding.ABinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

        injectViewModel()

        viewModel.register(this)

        binding.newApiPermissionButton.setOnClickListener {
            viewModel.requestPermission(
                this,
                listOf(
                    DangerousPermission.ACCESS_FINE_LOCATION,
                    DangerousPermission.CAMERA,
                )
            )

            viewModel.canShowUserExplanation.observe(this) {
                showDialogForRationalDialog()
            }

            viewModel.statusPermissionsMap.observe(this) { allPermissions ->
                allPermissions?.filter {
                    it.value == PermissionStatus.GRANTED
                }.apply {
                    if (this != null && isNotEmpty()) {
                        Toast.makeText(
                            this@A,
                            "${this.keys}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
        }
    }

    private fun injectViewModel() {
        val factory = ViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[PermissionViewModelNewApi::class.java]
    }

    private fun showDialogForRationalDialog() {
        AlertDialog
            .Builder(this)
            .setTitle(getString(R.string.rationale_title))
            .setMessage(getString(R.string.rationale_desc))
            .setPositiveButton(" Ask Permissions ")
            { _, _ ->

                viewModel.requestPermissionDirectly(
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
}
