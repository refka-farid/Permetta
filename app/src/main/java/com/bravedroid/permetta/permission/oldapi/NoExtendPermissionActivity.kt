package com.bravedroid.permetta.permission.oldapi

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus
import com.bravedroid.permetta.R
import com.bravedroid.permetta.databinding.ActivityPermissionBinding

class NoExtendPermissionActivity : AppCompatActivity() {

    private lateinit var viewModel: PermissionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectViewModel()
        val binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        Log.d("PermissionOld", " Activity onCreate  ${hashCode()}")
        binding.button.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, PermissionFragment.newInstance())
                .commit()
        }

        viewModel.statusPermissionsMap.observe(this) { observedMap: Map<DangerousPermission, PermissionStatus>? ->
            observedMap?.filter {
                it.value == PermissionStatus.GRANTED
            }.apply {
                if (this != null && isNotEmpty()) {
                    Toast.makeText(
                        this@NoExtendPermissionActivity,
                        "${this.keys}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }

        viewModel.canShowUserExplanation.observe(this) {
            if (it) {
                showDialogForRationalDialog()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.permisson_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.one_permission_old -> {
                return true
            }
            R.id.one_permission_new -> {
                return true
            }
            R.id.multiple_permission_old -> {
                viewModel.requestPermission(
                    this,
                    listOf(
                        DangerousPermission.ACCESS_FINE_LOCATION,
                        DangerousPermission.CAMERA,
                    ),
                )
                return true
            }
            R.id.multiple_permission_new -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun injectViewModel() {
        val factory = ViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[PermissionViewModel::class.java]
    }

    private fun showDialogForRationalDialog() {
        AlertDialog
            .Builder(this)
            .setTitle(getString(R.string.rationale_title))
            .setMessage(getString(R.string.rationale_desc))
            .setPositiveButton(" Ask Permissions ")
            { _, _ ->
                viewModel.requestPermissionDirectly(
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

}
