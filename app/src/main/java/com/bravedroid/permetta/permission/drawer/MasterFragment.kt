package com.bravedroid.permetta.permission.drawer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bravedroid.permetta.databinding.FragmentMasterBinding


class MasterFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = MasterFragment()
    }

    private var callbacks: Callbacks? = null

    internal interface Callbacks {
        fun onMasterItemClicked(masterItemId: Int)
    }

    var binding: FragmentMasterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is Callbacks) {
            throw RuntimeException("Context must implement callbacks")
        }
        callbacks = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMasterBinding.inflate(inflater)

        binding?.masterItem1?.setOnClickListener {
            callbacks?.onMasterItemClicked(1);
        }

        binding?.masterItem2?.setOnClickListener {
            callbacks?.onMasterItemClicked(2);
        }

        binding?.masterItem3?.setOnClickListener {
            callbacks?.onMasterItemClicked(3);
        }

        return binding!!.root
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

}

