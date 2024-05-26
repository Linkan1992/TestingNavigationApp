package com.linkan.testingapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.atilsamancioglu.artbookhilttesting.R
import com.atilsamancioglu.artbookhilttesting.databinding.FragmentSecondBinding
import com.atilsamancioglu.artbookhilttesting.databinding.ItemLayoutMergeBinding
import com.atilsamancioglu.artbookhilttesting.databinding.ItemLayoutViewStubBinding
import com.linkan.testingapp.applyPermissionIf
import com.linkan.testingapp.onClick
import com.linkan.testingapp.viewmodels.FragmentSecondViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    val TAG = "SecondFragment"

    private var _binding: FragmentSecondBinding? = null
    private var _mergeBinding: ItemLayoutMergeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val mergeBinding get() = _mergeBinding!!

    private val viewModel : FragmentSecondViewModel by viewModels()

    private val inflatedViewStub : View by lazy {
        val view = binding.viewStub.inflate()
        view.findViewById(R.id.inflatedStub)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        _mergeBinding = ItemLayoutMergeBinding.bind(_binding!!.root)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        simulateCorrectSupervisorScope()
        simulateWrongSupervisorScope()
        simulateExtensionFunction()

        inflatedViewStub.findViewById<TextView>(R.id.tv_colorView).setOnClickListener {
            Toast.makeText(context, "Hello inflated viewStub", Toast.LENGTH_SHORT).show()
            inflatedViewStub.visibility = View.GONE
        }

        mergeBinding.tvMerge1.setOnClickListener {
            mergeBinding.tvMerge2.text = "Hello Merge View - 2"
        }

        val onBackPressCallBack = object : OnBackPressedCallback(enabled = true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressCallBack)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_thirdFragment)
        }
    }

    private fun simulateExtensionFunction() {
        binding.buttonFive.onClick(300){
            Log.d(TAG, "OnClick method called >> ${viewModel.applyIfExtensionFun}")

            requireActivity().applyPermissionIf(requireContext(), false, "Internet is not available",
                success = {
                    Log.d(TAG, "Permission available")
            }, failure = {
                    Log.d(TAG, "Request permission")
            })

        }
    }

    val simulateSuperVisorScope : (View) -> Unit = {
        Log.d(TAG, "Lambda method called >> $it")
        viewModel.simulateCorrectSupervisorScope()
    }
    private fun simulateCorrectSupervisorScope() {
        binding.buttonThree.onClick(300, simulateSuperVisorScope)  // correct way -2
    }

    private fun simulateWrongSupervisorScope() {
        binding.buttonFour.onClick(300){
            Log.d(TAG, "OnClick method called >> $it")
            viewModel.simulateWrongSupervisorScope()
        } // correct way -1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}