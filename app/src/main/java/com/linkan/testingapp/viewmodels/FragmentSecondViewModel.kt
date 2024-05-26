package com.linkan.testingapp.viewmodels

import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope


fun View.onClick(debounceDuration: Long = 300L, action: (View) -> Unit) {
    setOnClickListener(DebouncedOnClickListener(debounceDuration) {
        action(it)
    })
}

private class DebouncedOnClickListener(
    private val debounceDuration: Long,
    private val clickAction: (View) -> Unit
) : View.OnClickListener {

    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val now = SystemClock.elapsedRealtime()
        if (now - lastClickTime >= debounceDuration) {
            lastClickTime = now
            clickAction(v)
        }
    }
}

class FragmentSecondViewModel : ViewModel() {


    val TAG = "FragmentTwoViewModel"

    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d(TAG, " coroutineContext >> $coroutineContext, throwable >> $throwable")
    }

    val customScope = CoroutineScope(SupervisorJob() + handler + Dispatchers.IO)


    fun simulateCorrectSupervisorScope() {
        // Correct code-1
        with(customScope) {
            launch {
                getData1().also { println(it) }
            }
            launch {
                getData2().also { println(it) }
            }
            launch {
                getData3().also { println(it) }
            }
        }

        // Correct code-2

        /*with(viewModelScope) {
            launch(handler) {
                getData1().also { println(it) }
            }
            launch(handler) {
                getData2().also { println(it) }
            }
            launch(handler) {
                getData3().also { println(it) }
            }
        }*/
    }

    fun simulateWrongSupervisorScope() {
        // Wrong code-1
        customScope.launch {
            launch {
                getData1().also { println(it) }
            }
            launch {
                getData2().also { println(it) }
            }
            launch {
                getData3().also { println(it) }
            }
        }

        // Wrong code-2
        /*customScope.launch {
            launch(handler) {
                getData1().also { println(it) }
            }
            launch(handler) {
                getData2().also { println(it) }
            }
            launch(handler) {
                getData3().also { println(it) }
            }
        }*/
    }


    private suspend fun getData1(): String {
        delay(1000)
        return "data 1"
    }

    private suspend fun getData2(): String? {
        delay(300)
        throw RuntimeException("While getting Data 2 exception happened")
    }

    private suspend fun getData3(): String {
        delay(800)
        return "data 3"
    }

}