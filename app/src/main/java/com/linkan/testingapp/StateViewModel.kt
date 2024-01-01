package com.linkan.testingapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StateViewModel : ViewModel() {

    private val _integerStateFlow = MutableStateFlow<Int>(0)

    val integerStateFlow : StateFlow<Int>
        get() = _integerStateFlow




    fun emitData(){
        viewModelScope.launch {
            repeat(100){ intValue ->
                 delay(100)
                _integerStateFlow.value = intValue
            }
        }
    }


    private val queryLiveData = MutableLiveData<String>()

    fun searchQuery(stringQuery : String){
        queryLiveData.value = stringQuery
    }

    val searchResultLiveData = queryLiveData.switchMap { searchQuery ->
        executeSearch(searchQuery)
    }

    private fun executeSearch(searchString : String) : LiveData<Resource<String>> {

        val livedata = MutableLiveData<Resource<String>>()

        viewModelScope.runOnBackgroundDispatcher(startLoader = {
            livedata.value = Resource.Loading(null)
        }, backgroundFunc = {
            delay(2000)
            return@runOnBackgroundDispatcher searchString
        }, callback = { response ->
            livedata.value = Resource.Success(response)
        })

        return livedata
    }


}