package com.linkan.testingapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.atilsamancioglu.artbookhilttesting.R
import com.atilsamancioglu.artbookhilttesting.databinding.ActivityOneBinding
import com.atilsamancioglu.artbookhilttesting.databinding.ActivityTwoBinding

class ActivityTwo : AppCompatActivity() {

    private lateinit var binding: ActivityTwoBinding
    companion object{
        fun navigateToActivity(context : Context){
            val intent = Intent(context, ActivityTwo::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSecond.setOnClickListener {
            ActivityThree.navigateToActivity(this)
        }
    }
}