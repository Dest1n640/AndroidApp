package com.example.a08_counter_on_mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _counter = MutableLiveData(0)
    val counter: LiveData<Int> = _counter

    fun onIncrementClicked() {
        _counter.value = (_counter.value ?: 0) + 1
    }
}
