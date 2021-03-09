package com.bravedroid.permetta.permission.oldapi

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bravedroid.api.DangerousPermission
import com.bravedroid.api.activitypermission.OldCorePermissionActivity
import com.bravedroid.api.PermissionStatus
import com.bravedroid.permetta.R
import com.bravedroid.permetta.databinding.ActivityPermissionBinding


class PermissionActivity : OldCorePermissionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}
