package com.linkan.testingapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.linkan.testingapp.UiState
import com.linkan.testingapp.runOnBackgroundDispatcher
import com.linkan.testingapp.test_inheritance.KilometerToMiles
import com.linkan.testingapp.test_inheritance.KilometerToMilesConverter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class StateViewModel : ViewModel() {

    val handler = CoroutineExceptionHandler{ context, throwable ->

    }

    val scope = CoroutineScope(SupervisorJob() + handler + Dispatchers.IO)

    private val _integerStateFlow = MutableStateFlow<Int>(0)

    val integerStateFlow : StateFlow<Int>
        get() = _integerStateFlow


    private val _integerSharedFlow = MutableSharedFlow<Int>()

    val integerSharedFlow : SharedFlow<Int>
        get() = _integerSharedFlow

     val integerFlow = flow<Int>{
        repeat(100){ intValue ->
            delay(1000)
            emit(intValue)
        }
    }




    fun emitData(){
        viewModelScope.launch {
            repeat(100){ intValue ->
                 delay(1000)
              /*  _integerStateFlow.update {
                    return@update intValue
                }*/
                _integerStateFlow.value = intValue
                _integerSharedFlow.emit(intValue)
            }
        }
    }

    fun experimentCoroutineWithUnhandledException(){
        // crash app
        GlobalScope.launch {
            showMessage("Inhale")
            delay(2000)
            throw NullPointerException()
            showMessage("Exhale")
        }
    }


    fun experimentAsyncWithUnhandledException(){
        // does not crash app
        viewModelScope.async {
            showMessage("Inhale")
            delay(2000)
            throw NullPointerException()
            showMessage("Exhale")
        }
    }



    private suspend fun showMessage(message : String){
        Log.d("StateViewModel", "Message is >> $message")
    }


    private val queryLiveData = MutableLiveData<String>()

    fun searchQuery(stringQuery : String){
        queryLiveData.value = stringQuery
    }

    val searchResultLiveData = queryLiveData.switchMap { searchQuery ->
        executeSearch(searchQuery)
    }

    private fun executeSearch(searchString : String) : LiveData<UiState<String>> {

        val livedata = MutableLiveData<UiState<String>>()

        viewModelScope.runOnBackgroundDispatcher(startLoader = {
            livedata.value = UiState.Loading(null)
        }, backgroundFunc = {
            delay(2000)
            return@runOnBackgroundDispatcher searchString
        }, callback = { response ->
            livedata.value = UiState.Success(response)
        })

        return livedata
    }

    fun experimentInheritance() {
        val object1 : KilometerToMiles = KilometerToMilesConverter() // it first looks for child methods and if not available then check in parent
        val object2 : KilometerToMilesConverter = KilometerToMilesConverter()
        val object3 : KilometerToMiles = KilometerToMiles()


        Log.d("StateViewModel", "Case-1 is object1.convertKmToMiles(1.0)  >> ${object1.convertKmToMiles(1.0)}")
        Log.d("StateViewModel", "Case-2 is object1.convertKmToMiles()  >> ${object1.convertKmToMiles()}")
        Log.d("StateViewModel", "Case-3 is object2.convertKmToMiles(1.0)  >> ${object2.convertKmToMiles(1.0)}")
        Log.d("StateViewModel", "Case-4 is object2.convertKmToMiles()  >> ${object2.convertKmToMiles()}")
        Log.d("StateViewModel", "Case-5 is object3.convertKmToMiles(1.0)  >> ${object3.convertKmToMiles(1.0)}")
        Log.d("StateViewModel", "Case-6 is object3.convertKmToMiles()  >> ${object3.convertKmToMiles()}")
    }

    var count = 0
    private val intFlow = flow<Int>{
        /*while(true){
            delay(1000)
            emit(++count)
        }*/

        repeat(100){ currValue ->
            delay(1000)
            emit(currValue)
        }
    }


    fun emitStateFlowData() : StateFlow<Int> {
        return intFlow.stateIn(
            initialValue = 0,
            scope = viewModelScope,
            started = WhileSubscribed(5000)
        )
    }


}