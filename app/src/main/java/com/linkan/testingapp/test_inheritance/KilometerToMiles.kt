package com.linkan.testingapp.test_inheritance

import android.util.Log

open class KilometerToMiles(val data : String) {

    init {
        Log.d("KilometerToMiles", data)
    }
    constructor() : this("Good work"){
        Log.d("KilometerToMiles", "How was work ?")
    }

    open fun convertKmToMiles(km : Double) : Double{
        return 1.69
    }


    fun convertKmToMiles() : Double{
        return this.convertKmToMiles(1.0)
    }

}



