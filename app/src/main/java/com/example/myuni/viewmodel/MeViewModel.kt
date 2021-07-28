package com.example.myuni.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.R
import com.example.myuni.model.Contacts
import com.example.myuni.utils.Encode
import com.google.firebase.database.*
import kotlin.collections.HashMap

class MeViewModel : ViewModel() {
    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()
    private var loginUser: Contacts? = null

    private val _text = MutableLiveData<Int>().apply {
        value = 0
    }
    val text: LiveData<Int> = _text

    fun registerNewUser(contact: Contacts){
        //初始化用户列表
        dbRef = database.getReference("usersList")
        var map: HashMap<String, Any> = HashMap()
        map[Encode.EncodeString(contact.email!!)] = contact
        dbRef.updateChildren(map)

        //初始化注册联系列表
        dbRef = database.getReference("contactsList").child(Encode.EncodeString(contact.email!!))
        map.clear()
        map[Encode.EncodeString(contact.email!!)] = contact
        dbRef.updateChildren(map)

        //初始化注册对话列表
        dbRef = database.getReference("conversationList").child(Encode.EncodeString(contact.email!!))
        map.clear()
        map[Encode.EncodeString(contact.email!!)] = contact
        dbRef.updateChildren(map)
    }

    fun login(email: String, passWord: String){
        dbRef = database.getReference("usersList").child(Encode.EncodeString(email))

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot){
                if (snapshot.exists()) {
                    val t = object : GenericTypeIndicator<HashMap<String, Any>>() {}
                    var value = snapshot.getValue(t)
                    if (passWord == value!!["password"]){
                        println("密码正确")
                        //头像之后再做处理，先给默认头像
                        loginUser = Contacts(value["name"] as String, R.drawable.profile_default, value["email"] as String, value["password"] as String)
                        _text.value = 1
                    } else{
                        _text.value = -1
                        println("my password:${passWord}, ps:${value!!["password"]}")
                    }
                }else
                    _text.value = -1
            }

        })
    }

    fun getLoginUser(): Contacts?{
        if (_text.value == 1)
            return loginUser
        return null
    }
}