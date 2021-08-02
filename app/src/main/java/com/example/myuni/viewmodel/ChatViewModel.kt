package com.example.myuni.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Message
import com.example.myuni.utils.EncodeUtils
import com.example.myuni.utils.TimeUtils
import com.google.firebase.database.*
import java.time.LocalDateTime
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatViewModel : ViewModel(){
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRefReceiver: DatabaseReference
    private var database = FirebaseDatabase.getInstance()
    private val _messageList = MutableLiveData<ArrayList<Message>>().apply {
        value = object : ArrayList<Message>(){}
    }
    val messageList: LiveData<ArrayList<Message>> = _messageList
    private var count: Int = 0
    private var sender: String = ""
    private var receiver: String = ""


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(content: String){
        dbRef = database.getReference("conversationList").child(EncodeUtils.EncodeString(sender)).child(EncodeUtils.EncodeString(receiver))
        var map: HashMap<String, Any> = HashMap<String, Any>()
        val message = Message(content, Message.TYPE_SEND, TimeUtils.getCurrentTime(LocalDateTime.now()))
        if (count != 0)
            _messageList.value = _messageList.value!!.plus(message) as ArrayList<Message>
        map[EncodeUtils.EncodeString(message.time!!)] = message
        dbRef.updateChildren(map)

        dbRefReceiver = database.getReference("conversationList").child(EncodeUtils.EncodeString(receiver)).child(EncodeUtils.EncodeString(sender))//之后动态获取用户的邮箱
        var mapReceived: HashMap<String, Any> = HashMap<String, Any>()
        val messageReceived = Message(content, Message.TYPE_RECEIVED, TimeUtils.getCurrentTime(LocalDateTime.now()))
        mapReceived[EncodeUtils.EncodeString(message.time!!)] = messageReceived
        dbRefReceiver.updateChildren(mapReceived)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun initChatList(sender: String, receiver: String){
        this.sender = sender
        this.receiver = receiver
        println("sender:$sender  receiver:$receiver")
        dbRef = database.getReference("conversationList").child(EncodeUtils.EncodeString(sender)).child(EncodeUtils.EncodeString(receiver))

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    println("Chat Data change!")
                    val t  = object : GenericTypeIndicator<HashMap<String, Message>>() {}
                    var value = dataSnapshot.getValue(t)

                    if (count == 0){
                        var temp = ArrayList<Message>()
                        for ((c, key) in value!!.keys.withIndex()){
                            val m: Message? = value[key]
                            temp.add(m!!)
                        }
                        _messageList.value = _messageList.value!!.plus(TimeUtils.orderChatList(temp)) as ArrayList<Message>
                        count++
                    }else{
                        //遍历map,只添加最新的消息
                        for ((c, key) in value!!.keys.withIndex()){
                            if (c == value.size){
                                val m: Message? = value[key]
                                _messageList.value = _messageList.value!!.plus(m) as ArrayList<Message>
                            }
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

    fun initCountAndList(){
        _messageList.value!!.clear()
        count = 0
    }


}