package com.example.myuni.viewmodel

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.R
import com.example.myuni.model.Connect
import com.example.myuni.model.Contacts
import com.example.myuni.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Constructor
import java.util.*
import java.util.logging.Handler
import kotlin.collections.ArrayList

class ChatViewModel : ViewModel(){

    private val _isHide = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val _messageList = MutableLiveData<ArrayList<Message>>().apply {
        var m: ArrayList<Message> = object : ArrayList<Message>(){
            init {
                val msg1 = Message("Hello, how are you?", Message.TYPE_SEND)
                add(msg1)
                val msg2 = Message("Fine, thank you.", Message.TYPE_RECEIVED)
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

    fun sendMessage(content: String){
        val message = Message(content, Message.TYPE_SEND)
        _messageList.value = _messageList.value!!.plus(message) as ArrayList<Message>
//        println("send Message (String)")
//        println("content:${message.content},type${message.type}")
    }

    fun receiveMessage(connect: Connect){
        while (true){
            val newMes: String = connect.receive()
            if (newMes != ""){
                val message = Message(newMes, Message.TYPE_RECEIVED)
                _messageList.postValue( _messageList.value!!.plus(message) as ArrayList<Message>)
            }
        }
    }

}