package com.bravedroid.permetta.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.bravedroid.permetta.R
import com.bravedroid.permetta.base.BaseActivity
import com.bravedroid.permetta.databinding.ActivityPermissionBinding

class PermissionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("PermissionOld", " Activity onCreate  ${hashCode()}")
        binding.button.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, PermissionFragment.newInstance())
                .commit()
        }
        binding.buttonPermission.setOnClickListener {
            ActivityCompat.requestPermissions(
                this,
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
                        " PERMISSION_GRANTED ${permissions[index]} from Activity ${hashCode()}"
                    )
                } else {
                    Log.d(
                        "PermissionOld",
                        " PERMISSION_DENIED ${permissions[index]} from Activity ${hashCode()}"
                    )
                }
            }
        }
    }
}
