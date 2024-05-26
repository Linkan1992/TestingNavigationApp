package com.linkan.testingapp

import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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


inline fun <T> CoroutineScope.runOnBackgroundDispatcher(
    startLoader: () -> Unit,
    noinline backgroundFunc: suspend () -> T,
    crossinline callback: (T) -> Unit
) {

    startLoader.invoke()
    this.launch(Dispatchers.IO) {
        Log.d("safeApiCall", "I'm working on Thread = ${Thread.currentThread().name}")
        val result = backgroundFunc.invoke()
        withContext(Dispatchers.Main) {
            Log.d("safeApiCall", "I'm working on Thread = ${Thread.currentThread().name}")
            callback.invoke(result)
        }
    }
}


public inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (prevValue == nextValue) {
            return
        } else
            if (compareAndSet(prevValue, nextValue)) {
                value = nextValue
                return
            }
    }
}

inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T {
    return if (condition) {
        this.apply(block)
    } else
        this
}


inline fun <T> T.applyPermissionIf(
    context: Context,
    condition: Boolean,
    errorMessage: String,
    success: T.() -> Unit,
    failure: T.() -> Unit
): T {
    return if (condition) {
        this.apply(success)
    } else {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        this.apply(failure)
    }
}