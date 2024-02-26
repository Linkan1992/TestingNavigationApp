package com.linkan.testingapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.atilsamancioglu.artbookhilttesting.R
import com.atilsamancioglu.artbookhilttesting.databinding.ActivityMainBinding
import com.google.firebase.appdistribution.InterruptionLevel
import com.google.firebase.appdistribution.ktx.appDistribution
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            ActivityOne.navigateToActivity(this)
        }

        //enableInAppTesterNotificationFeedback()

        binding.fabSendFeedback.setOnClickListener {
            Firebase.appDistribution.startFeedback("Submit Feedback")
        }
    }

    private fun enableInAppTesterNotificationFeedback() {
        Firebase.appDistribution.showFeedbackNotification(
            // Text providing notice to your testers about collection and
            // processing of their feedback data
            "Submit Feedback",
            // The level of interruption for the notification
            InterruptionLevel.HIGH)
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Activity Callback : onStart")
    }

    override fun onResume() {
        Log.d(TAG, "Activity Callback : onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "Activity Callback : onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "Activity Callback : onStop")
        super.onStop()
    }

    override fun onRestart() {
        Log.d(TAG, "Activity Callback : onRestart")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.d(TAG, "Activity Callback : onDestroy")
        super.onDestroy()
    }

}