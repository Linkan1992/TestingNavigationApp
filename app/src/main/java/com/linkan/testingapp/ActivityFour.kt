package com.linkan.testingapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.atilsamancioglu.artbookhilttesting.R
import com.atilsamancioglu.artbookhilttesting.databinding.ActivityFourBinding
import com.atilsamancioglu.artbookhilttesting.databinding.ActivityThreeBinding

class ActivityFour : AppCompatActivity() {
    private lateinit var binding: ActivityFourBinding
    companion object{
        fun navigateToActivity(context : Context){
            val intent = Intent(context, ActivityFour::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFourBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSecond.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}