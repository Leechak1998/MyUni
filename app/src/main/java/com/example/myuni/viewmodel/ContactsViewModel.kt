package com.example.myuni.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.R
import com.example.myuni.model.Contacts

class ContactsViewModel : ViewModel() {

    private val _contactsList = MutableLiveData<ArrayList<Contacts>>().apply {
        var c: ArrayList<Contacts> = object : ArrayList<Contacts>(){
            init {
                val c1 = Contacts("jackson", R.drawable.profile_default, "rl1r20@soton.ac.uk")
                val c2 = Contacts("ellen", R.drawable.profile_default, "haoyang@qq.com")
                add(c1)
                add(c2)
            }
        }
        value = c
    }



    val contactsList: MutableLiveData<ArrayList<Contacts>> = _contactsList

    fun addContact(contacts: Contacts){
        _contactsList.value!!.add(contacts)
    }



    fun connectServer(contacts: Contacts){

    }

}