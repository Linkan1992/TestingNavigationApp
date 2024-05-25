package com.linkan.testingapp

sealed class UiState<out T> {
    data class Loading(val message : String? = null) : UiState<Nothing>()
    data class Success<T>(val data : T) : UiState<T>()
    data class Error(val message : String? = null) : UiState<Nothing>()
}