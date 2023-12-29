package com.linkan.testingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.atilsamancioglu.artbookhilttesting.R
import com.atilsamancioglu.artbookhilttesting.databinding.FragmentSecondBinding
import com.atilsamancioglu.artbookhilttesting.databinding.FragmentSixBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SixFragment : Fragment() {

    private var _binding: FragmentSixBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSixBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onBackPressCallBack = object : OnBackPressedCallback(enabled = true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressCallBack)

        binding.buttonSecond.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}