package com.example.myuni.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Message
import com.example.myuni.utils.Encode
import com.example.myuni.utils.TimeUtils
import com.google.firebase.database.*
import java.time.LocalDateTime
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatViewModel : ViewModel(){
    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()


//    private val _messageList = MutableLiveData<ArrayList<Message>>().apply {
//        var m: ArrayList<Message> = object : ArrayList<Message>(){
//            init {
//                val msg1 = Message("Hello, how are you?", Message.TYPE_SEND, LocalDateTime.now().toString())
//                add(msg1)
//                val msg2 = Message("Fine, thank you.", Message.TYPE_RECEIVED, LocalDateTime.now().toString())
//                add(msg2)
//            }
//        }
//        value = m
//    }

    private val _messageList = MutableLiveData<ArrayList<Message>>().apply {
        value = object : ArrayList<Message>(){}
    }

    val messageList: LiveData<ArrayList<Message>> = _messageList


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(content: String){
        var map: HashMap<String, Any> = HashMap<String, Any>()
        val message = Message(content, Message.TYPE_SEND, TimeUtils.getCurrentTime(LocalDateTime.now()))
        _messageList.value = _messageList.value!!.plus(message) as ArrayList<Message>
        map[message.time.toString()] = message
        dbRef.updateChildren(map)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun initChatList(){
        dbRef = database.getReference("conversationList").child(Encode.EncodeString("rl1r20@soton.ac.uk")).child(Encode.EncodeString("yh12n20@soton.ac.uk"))//之后动态获取用户的邮箱

        var map: HashMap<String, Message> = HashMap()
        val m = Message("hi ellen", Message.TYPE_SEND, TimeUtils.getCurrentTime(LocalDateTime.now()))
        map[m.time.toString()] = m
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