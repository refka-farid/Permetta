package com.bravedroid.permetta.permission.oldapi

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus
import com.bravedroid.api.old.activitypermission.OldCorePermissionActivity
import com.bravedroid.permetta.R
import com.bravedroid.permetta.databinding.ActivityPermissionBinding


class PermissionActivity : OldCorePermissionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)


        Log.d("PermissionOld", " Activity onCreate  ${hashCode()}")
        binding.button.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, PermissionFragment.newInstance())
                .commit()
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
                requestPermission(
                    listOf(
                        DangerousPermission.ACCESS_FINE_LOCATION,
                        DangerousPermission.CAMERA,
                    ),
                    ::onPermissionsResponse,
                    ::showDialogForRationalDialog,
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
        }.apply {
            if (isNotEmpty()) {
                Toast.makeText(
                    this@PermissionActivity,
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

    private fun showDialogForRationalDialog() {
        AlertDialog
            .Builder(this)
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
}
