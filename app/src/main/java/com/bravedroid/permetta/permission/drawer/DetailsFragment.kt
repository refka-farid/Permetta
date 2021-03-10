package com.bravedroid.permetta.permission.drawer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.bravedroid.permetta.R
import com.bravedroid.permetta.databinding.FragmentDeatilsBinding

const val TAG = "DetailsFragment"

class DetailsFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            DetailsFragment()
    }

    var binding: FragmentDeatilsBinding? = null
    private var nonSelectedColor = 0
    private var selectedColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeatilsBinding.inflate(inflater)
//        binding?.detailItem1?.setOnClickListener {
//
//        }
//
//        binding?.detailItem2?.setOnClickListener {
//
//        }
//
//        binding?.detailItem3?.setOnClickListener {
//
//        }

        selectedColor = getColor(requireContext(), R.color.design_default_color_error);
        nonSelectedColor = getColor(requireContext(), R.color.black);

        return binding!!.root
    }

    fun onMasterItemClicked(masterId: Int) {
        // reset colors
        binding?.detailItem1?.setTextColor(nonSelectedColor);
        binding?.detailItem2?.setTextColor(nonSelectedColor);
        binding?.detailItem3?.setTextColor(nonSelectedColor);

        when (masterId) {
            1 -> {
                binding?.detailItem1?.setTextColor(selectedColor);
            }
            2 -> {
                binding?.detailItem2?.setTextColor(selectedColor);

            }
            3 -> {
                binding?.detailItem3?.setTextColor(selectedColor);

            }
            else -> {
                Log.d(TAG, "unknown master ID");
            }
        }
    }
}






