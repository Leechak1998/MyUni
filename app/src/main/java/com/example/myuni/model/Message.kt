package com.example.myuni.model

class Message constructor(content: String?, type: Int?, time: String?){
    companion object{
        val TYPE_RECEIVED: Int = 0
        val TYPE_SEND: Int = 1
    }

    val content: String? = content
    val type: Int? = type
    val time: String? = time

    constructor():this("",null, "")
}