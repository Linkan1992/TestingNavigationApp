package com.linkan.testingapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.atilsamancioglu.artbookhilttesting.R
import com.atilsamancioglu.artbookhilttesting.databinding.ActivityMainBinding
import com.atilsamancioglu.artbookhilttesting.databinding.ActivityOneBinding

class ActivityOne : AppCompatActivity() {

    private lateinit var binding: ActivityOneBinding
    companion object{
        fun navigateToActivity(context : Context){
            val intent = Intent(context, ActivityOne::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSecond.setOnClickListener {
            ActivityTwo.navigateToActivity(this)
        }

    }
}