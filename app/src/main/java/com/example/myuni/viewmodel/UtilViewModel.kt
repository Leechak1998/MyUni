package com.example.myuni.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Comment
import com.example.myuni.model.Contacts
import com.example.myuni.utils.EncodeUtils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator

class UtilViewModel : ViewModel(){
    private var database = FirebaseDatabase.getInstance()
    val userData = MutableLiveData<Contacts>()

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

    fun getUserData(email: String){

        database.getReference("usersList").child(EncodeUtils.EncodeString(email)).get().addOnSuccessListener {
            val t = object : GenericTypeIndicator<Contacts>() {}
            var value = it.getValue(t)

            userData.value = value!!
//            println("name:${value!!.name}")

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }
}