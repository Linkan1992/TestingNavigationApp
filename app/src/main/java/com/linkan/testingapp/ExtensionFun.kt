package com.linkan.testingapp

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

inline fun <T> CoroutineScope.runOnBackgroundDispatcher(
    startLoader : () -> Unit,
    noinline backgroundFunc : suspend () -> T,
    crossinline callback : (T) -> Unit) {

    startLoader.invoke()
    this.launch(Dispatchers.IO) {
        Log.d("safeApiCall", "I'm working on Thread = ${Thread.currentThread().name}")
        val result = backgroundFunc.invoke()
        withContext(Dispatchers.Main){
            Log.d("safeApiCall", "I'm working on Thread = ${Thread.currentThread().name}")
            callback.invoke(result)
        }
    }
}