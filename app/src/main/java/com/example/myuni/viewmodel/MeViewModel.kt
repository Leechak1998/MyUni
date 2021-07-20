package com.example.myuni.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is me Fragment"
    }
    val text: LiveData<String> = _text

    fun changeData(data: String){
        _text.postValue(data)
        println("_text.value = ${_text.value.toString()}")
    }
}