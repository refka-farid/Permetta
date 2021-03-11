package com.bravedroid.permetta.permission.oldapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bravedroid.api.entities.DangerousPermission
import com.bravedroid.api.entities.PermissionStatus
import com.bravedroid.permetta.base.BaseFragment
import com.bravedroid.permetta.databinding.FragmentPermissionBinding


class PermissionFragment : BaseFragment() {
    private var binding: FragmentPermissionBinding? = null
    private lateinit var viewModel: PermissionViewModel

    companion object {
        @JvmStatic
        fun newInstance() = PermissionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injectViewModel()
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
            viewModel.requestPermission(
                this,
                listOf(
                    DangerousPermission.ACCESS_FINE_LOCATION,
                    DangerousPermission.CAMERA,
                ),
            )
        }

        viewModel.statusPermissionsMap.observe(viewLifecycleOwner) { observedMap: Map<DangerousPermission, PermissionStatus>? ->
            observedMap?.filter {
                it.value == PermissionStatus.GRANTED
            }?.apply {
                Toast.makeText(
                    requireContext(),
                    "${this.keys}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun injectViewModel() {
        val factory = ViewModelFactory(requireContext())
        viewModel = ViewModelProvider(requireActivity(), factory)[PermissionViewModel::class.java]
    }
}
