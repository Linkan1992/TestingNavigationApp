package com.linkan.testingapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.atilsamancioglu.artbookhilttesting.R
import com.atilsamancioglu.artbookhilttesting.databinding.FragmentFirstBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.appdistribution.ktx.appDistribution
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.messaging.FirebaseMessaging
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

        binding.btnBanking.setOnClickListener {
            // init Banking Firebase instance
            FirebaseApp.initializeApp(
                requireContext(), FirebaseOptions.Builder()
                    .setApplicationId("1:159635962604:android:46368d98283f09e5d943c9") //mobilesdk_app_id
                    .setApiKey("AIzaSyA0NQWumMuSyfTqMaP83QYnSAvG2QsUc40")//current_key
                    .setDatabaseUrl("https://pushnotification-625fd.firebaseio.com")//firebase_url
                    .setGcmSenderId("159635962604")//project_number
                    .setStorageBucket("pushnotification-625fd.appspot.com")//storage_bucket
                    .setProjectId("pushnotification-625fd")//project_id
                    .build(),
                "Bank").also {
                    val firebaseMessaging = it.get(FirebaseMessaging::class.java)
                    /*firebaseMessaging.token.result.apply {
                        Thread.sleep(1000)
                        // Store or send the new FCM token to your server for further use
                        Log.d("MyFirebaseMessagingService", "New Token: $this")
                    }*/

                firebaseMessaging.token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result

                    Log.d("MyFirebaseMessagingService", "Current Token: $token")
                })
            }
        }

        binding.btnCard.setOnClickListener {
            // init Card Firebase instance
            FirebaseApp.initializeApp(
                requireContext(), FirebaseOptions.Builder()
                    .setApplicationId("1:24217414101:android:d9219e258cf21fbfec755c") //mobilesdk_app_id
                    .setApiKey("AIzaSyDO_1HQrQIH_Q2uk6hNsczu_WkZIrywwbI")//current_key
                    .setDatabaseUrl("https://notification-66a8a.firebaseio.com")//firebase_url
                    .setGcmSenderId("24217414101")//project_number
                    .setStorageBucket("notification-66a8a.appspot.com")//storage_bucket
                    .setProjectId("notification-66a8a")//project_id
                    .build(),
                "Card").also {
                val firebaseMessaging = it.get(FirebaseMessaging::class.java)

                firebaseMessaging.token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result

                    Log.d("MyFirebaseMessagingService", "Current Token: $token")
                })
            }
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