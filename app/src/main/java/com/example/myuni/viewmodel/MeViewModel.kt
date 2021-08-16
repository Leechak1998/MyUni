package com.example.myuni.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Contacts
import com.example.myuni.model.Message
import com.example.myuni.utils.EncodeUtils
import com.example.myuni.utils.TimeUtils
import com.google.firebase.database.*
import java.time.LocalDateTime
import kotlin.collections.HashMap

class MeViewModel : ViewModel() {
    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()
    private var loginUser: Contacts? = null

    private val _isLogin = MutableLiveData<Int>().apply {
        value = 0
    }
    val isLogin: LiveData<Int> = _isLogin

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerNewUser(contact: Contacts){
        //初始化用户列表
        dbRef = database.getReference("usersList")
        var map: HashMap<String, Any> = HashMap()
        map[EncodeUtils.EncodeString(contact.email!!)] = contact
        dbRef.updateChildren(map)

        //初始化注册联系列表
        dbRef = database.getReference("contactsList").child(EncodeUtils.EncodeString(contact.email!!))
        map.clear()
        map[EncodeUtils.EncodeString(contact.email!!)] = contact
        dbRef.updateChildren(map)

        //初始化注册对话列表
        var map1: HashMap<String, Any> = HashMap<String, Any>()
        val message = Message(null, Message.TYPE_SEND, TimeUtils.getCurrentTime(LocalDateTime.now()), loginUser!!.imageId)
        //val message = Message()
        dbRef = database.getReference("conversationList").child(EncodeUtils.EncodeString(contact.email!!)).child(EncodeUtils.EncodeString(contact.email!!))
        map1[EncodeUtils.EncodeString(message.time!!)] = message
        dbRef.setValue(map1)
    }

    fun login(email: String, passWord: String){
        dbRef = database.getReference("usersList").child(EncodeUtils.EncodeString(email))

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
                        loginUser = Contacts(value["name"] as String, value["imageId"] as String, value["email"] as String, value["password"] as String, value["uni"] as String)
                        _isLogin.value = 1
                    } else{
                        _isLogin.value = -1
                        println("my password:${passWord}, ps:${value!!["password"]}")
                    }
                }else
                    _isLogin.value = -1
            }

        })
    }

    fun getLoginUser(): Contacts?{
        if (_isLogin.value == 1)
            return loginUser
        return null
    }

    fun logOut(){
        _isLogin.value = 0

    }
}