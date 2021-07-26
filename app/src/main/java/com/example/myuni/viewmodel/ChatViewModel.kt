package com.example.myuni.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Message
import com.example.myuni.utils.encode
import com.example.myuni.utils.timeUtils
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

class ChatViewModel : ViewModel(){
    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()

    private val _isHide = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val _messageList = MutableLiveData<ArrayList<Message>>().apply {
        var m: ArrayList<Message> = object : ArrayList<Message>(){
            init {
                val msg1 = Message("Hello, how are you?", Message.TYPE_SEND, LocalDateTime.now().toString())
                add(msg1)
                val msg2 = Message("Fine, thank you.", Message.TYPE_RECEIVED, LocalDateTime.now().toString())
                add(msg2)
            }
        }
        value = m
    }

    val isHide: LiveData<Boolean> = _isHide
    val messageList: LiveData<ArrayList<Message>> = _messageList

    fun switchNavBarStatus(){
        _isHide.value = _isHide.value != true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(content: String){
        var map: HashMap<String, Any> = HashMap<String, Any>()
        val message = Message(content, Message.TYPE_SEND, timeUtils.getCurrentTime(LocalDateTime.now()))
        _messageList.value = _messageList.value!!.plus(message) as ArrayList<Message>
        map[message.time.toString()] = message
        dbRef.updateChildren(map)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun initChatList(){
        dbRef = database.getReference("conversationList").child(encode.EncodeString("rl1r20@soton.ac.uk")).child(encode.EncodeString("yh12n20@soton.ac.uk"))//之后动态获取用户的邮箱

        var map: HashMap<String, Message> = HashMap()
//        var list = ArrayList<Message>()
        val m = Message("hi ellen", Message.TYPE_SEND, timeUtils.getCurrentTime(LocalDateTime.now()))
//        list.add(m)
        map[m.time.toString()] = m
//        dbRef.setValue(list)
        dbRef.setValue(map)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    val t  = object : GenericTypeIndicator<HashMap<String, Message>>() {}
                    var value = dataSnapshot.getValue(t)

                    //遍历map,只添加最新的消息
                    for ((count, key) in value!!.keys.withIndex()){
                        if (count == value.size){
                            val m: Message? = value[key]
                            _messageList.value = _messageList.value!!.plus(m) as ArrayList<Message>
                        }
                    }
                }else{
                    println("none data...")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("NOTHING CHANGE!!!!!!")
            }
        })
    }


}