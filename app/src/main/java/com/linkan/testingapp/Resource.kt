package com.linkan.testingapp

sealed class Resource<out T> {
    data class Loading(val message : String? = null) : Resource<Nothing>()
    data class Success<T>(val data : T) : Resource<T>()
    data class Error(val message : String? = null) : Resource<Nothing>()
}