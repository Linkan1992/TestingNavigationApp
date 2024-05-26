package com.linkan.testingapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.atilsamancioglu.artbookhilttesting.R
import com.atilsamancioglu.artbookhilttesting.databinding.FragmentFirstBinding
import com.google.firebase.appdistribution.ktx.appDistribution
import com.google.firebase.ktx.Firebase
import com.linkan.testingapp.UiState
import com.linkan.testingapp.viewmodels.StateViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val TAG = "MainActivity"
    private val TAG1 = "FirstFragment"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mViewModel : StateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSendFeedback.setOnClickListener {
            Firebase.appDistribution.startFeedback("Submit Feedback")
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }



        buttonEmitFlowData()
        buttonEmitStateFlow()
        buttonSearchData()

       // collectIntegerFlow()
        collectColdToHotFlow()
        collectStateLaunchWhenStarted()

        collectIntegerStateFlow()
        collectIntegerSharedFlow()

        observeLiveData()
    }

    private fun buttonSearchData(){

        binding.buttonSearch.setOnClickListener {
            binding.etSearch.text?.toString()?.let {
                mViewModel.searchQuery(it)
            }
        }
    }

    private fun buttonEmitFlowData(){

        binding.buttonTwo.setOnClickListener {
            mViewModel.emitData()

            //mViewModel.experimentCoroutineWithUnhandledException()
            mViewModel.experimentAsyncWithUnhandledException()

            mViewModel.experimentInheritance()
        }
    }

    private fun buttonEmitStateFlow(){

        binding.buttonEmitFlow.setOnClickListener {
            mViewModel.emitStateFlowData()
        }

    }

    private fun collectColdToHotFlow(){

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel
                    .emitStateFlowData()
                    .collect { intValue ->
                        Log.d(TAG1, "Flow Pipe cold >> hot Collected Value >> $intValue")
                    }
            }
        }
    }

    private fun collectStateLaunchWhenStarted(){

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mViewModel.integerStateFlow.collect { intValue ->
                Log.d(TAG, "launchWhenStarted Collected Value >> $intValue")
            }
        }
    }

    private fun collectIntegerStateFlow(){

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel
                    .integerStateFlow
                    .collect { intValue ->
                        Log.d(TAG, "IntegerStateFlow >> repeatOnLifecycle Collected Value >> $intValue")
                    }
            }
        }
    }

    private fun collectIntegerSharedFlow(){

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel
                    .integerSharedFlow
                    .collect { intValue ->
                        Log.d(TAG, "IntegerSharedFlow Collected Value >> $intValue")
                    }
            }
        }
    }

    private fun collectIntegerFlow(){

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel
                    .integerFlow
                    .collect { intValue ->
                        Log.d(TAG, "IntegerFlow Collected Value >> $intValue")
                    }
            }
        }
    }

    private fun observeLiveData() {
        mViewModel.searchResultLiveData.observe(viewLifecycleOwner){ result ->
            when(result){
                is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is UiState.Success -> {
                    binding.textviewFirst.text = "Searched Query is >> ${result.data}"
                    binding.progressBar.visibility = View.GONE
                }
                is UiState.Error -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}