package com.example.myuni.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Comment
import com.example.myuni.model.Contacts

class UtilViewModel : ViewModel(){
    private val userData = MutableLiveData<Contacts>()

    private val _isHide = MutableLiveData<Boolean>().apply {
        value = false
    }

    val isHide: LiveData<Boolean> = _isHide

    fun switchNavBarStatus(){
        _isHide.value = _isHide.value != true
    }

    fun setNavBarStatus(status: Boolean){
        _isHide.value = status
    }


}