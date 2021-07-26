package com.example.myuni.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.R
import com.example.myuni.model.Contacts
import com.example.myuni.model.Message
import com.example.myuni.utils.encode
import com.google.firebase.database.*

class MeViewModel : ViewModel() {
    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()

    private val _text = MutableLiveData<String>().apply {
        value = "This is me Fragment"
    }
    val text: LiveData<String> = _text

    fun changeData(data: String){
        _text.postValue(data)
        println("_text.value = ${_text.value.toString()}")
    }

    fun registerNewUser(contact: Contacts){
        dbRef = database.getReference("usersList").child(encode.EncodeString(contact.email!!))
        var map: HashMap<String, Any> = HashMap()
        map[contact.email] = contact
        dbRef.updateChildren(map)
    }

    fun login(email: String, passWord: String): Boolean{
        var isLogin = false
        dbRef = database.getReference("usersList").child(encode.EncodeString(email))
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val t = object : GenericTypeIndicator<List<Contacts>>() {}
                    var value: List<Contacts> = snapshot.getValue(t) as List<Contacts>

                }else{
                    isLogin = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return isLogin
    }
}