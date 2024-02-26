package com.linkan.testingapp.test_inheritance

class KilometerToMilesConverter : KilometerToMiles() {

    override fun convertKmToMiles(km: Double): Double {
        return 1.89
    }

}