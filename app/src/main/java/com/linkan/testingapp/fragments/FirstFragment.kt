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
import com.linkan.testingapp.Resource
import com.linkan.testingapp.StateViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
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

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonTwo.setOnClickListener {
            mViewModel.emitData()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mViewModel.integerStateFlow.collect { intValue ->
                Log.d(TAG, "launchWhenStarted Collected Value >> $intValue")
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel
                    .integerStateFlow
                    .collect { intValue ->
                    Log.d(TAG, "repeatOnLifecycle Collected Value >> $intValue")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel
                    .integerSharedFlow
                    .collect { intValue ->
                        Log.d(TAG, "IntegerSharedFlow Collected Value >> $intValue")
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel
                    .integerFlow
                    .collect { intValue ->
                        Log.d(TAG, "IntegerFlow Collected Value >> $intValue")
                    }
            }
        }

        binding.buttonSearch.setOnClickListener {
            binding.etSearch.text?.toString()?.let {
                mViewModel.searchQuery(it)
            }
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        mViewModel.searchResultLiveData.observe(viewLifecycleOwner){ result ->
            when(result){
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.textviewFirst.text = "Searched Query is >> ${result.data}"
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Error -> {
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