package com.example.myuni.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Contacts
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
    val _messageList = MutableLiveData<ArrayList<Message>>().apply {
        value = object : ArrayList<Message>(){}
    }
//    val messageList: LiveData<ArrayList<Message>> = _messageList
    private lateinit var sender: Contacts
    private lateinit var receiver: Contacts
    private var isListening = false

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(content: String){
        dbRef = database.getReference("conversationList").child(EncodeUtils.EncodeString(sender.email!!)).child(EncodeUtils.EncodeString(receiver.email!!))
        var map: HashMap<String, Any> = HashMap<String, Any>()
        val message = Message(content, Message.TYPE_SEND, TimeUtils.getCurrentTime(LocalDateTime.now()), sender.imageId)
        map[EncodeUtils.EncodeString(message.time!!)] = message
        dbRef.updateChildren(map)

        dbRefReceiver = database.getReference("conversationList").child(EncodeUtils.EncodeString(receiver.email!!)).child(EncodeUtils.EncodeString(sender.email!!))
        var mapReceived: HashMap<String, Any> = HashMap<String, Any>()
        val messageReceived = Message(content, Message.TYPE_RECEIVED, TimeUtils.getCurrentTime(LocalDateTime.now()), sender.imageId)
        mapReceived[EncodeUtils.EncodeString(message.time!!)] = messageReceived
        dbRefReceiver.updateChildren(mapReceived)

        isListening = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendGroupMessage(content: String, userList: List<Contacts>){

        dbRef = database.getReference("conversationList").child(EncodeUtils.EncodeString(sender.email!!)).child(receiver.email!!)
        var map: HashMap<String, Any> = HashMap<String, Any>()
        val message = Message(content, Message.TYPE_SEND, TimeUtils.getCurrentTime(LocalDateTime.now()), sender.imageId)
        map[EncodeUtils.EncodeString(message.time!!)] = message
        dbRef.updateChildren(map)

        for (i in userList.indices){
            if (sender.email != userList[i].email){
                dbRefReceiver = database.getReference("conversationList").child(EncodeUtils.EncodeString(userList[i].email!!)).child(receiver.email!!)
                var mapReceived: HashMap<String, Any> = HashMap<String, Any>()
                val messageReceived = Message(content, Message.TYPE_RECEIVED, TimeUtils.getCurrentTime(LocalDateTime.now()), sender.imageId)
                mapReceived[EncodeUtils.EncodeString(message.time!!)] = messageReceived
                dbRefReceiver.updateChildren(mapReceived)
            }
        }

        isListening = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initChatList(sender: Contacts, receiver: Contacts){
        _messageList.value!!.clear()
        this.sender = sender
        this.receiver = receiver
        isListening = true
        println("===sender:${this.sender.email}  receiver:${this.receiver.email}===")

        dbRef = database.getReference("conversationList").child(EncodeUtils.EncodeString(this.sender.email!!)).child(EncodeUtils.EncodeString(this.receiver.email!!))
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //if (isListening){
                    if (dataSnapshot.exists()){
                        println("Chat Data change!")
                        val t  = object : GenericTypeIndicator<HashMap<String, Message>>() {}
                        var value = dataSnapshot.getValue(t)

                        _messageList.value?.clear()
                        var temp = ArrayList<Message>()

                        for ((c, key) in value!!.keys.withIndex()){
                            val m: Message? = value[key]
                            temp.add(m!!)
                        }

                        //Sort the chats in chronological order
                        _messageList.value = _messageList.value!!.plus(TimeUtils.orderChatList(temp)) as ArrayList<Message>
                        isListening = false
                    }else{
                        println("none data...")
                    }
                //}
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("NOTHING CHANGE!!!!!!")
            }
        })
    }


}