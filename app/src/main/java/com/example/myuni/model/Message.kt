package com.example.myuni.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myuni.utils.TimeUtils
import java.time.LocalDateTime

class Message constructor(content: String?, type: Int?, time: String?, profile: String?){
    companion object{
        val TYPE_RECEIVED: Int = 0
        val TYPE_SEND: Int = 1
    }

    val content: String? = content
    val type: Int? = type
    val time: String? = time
    val profile = profile

    @RequiresApi(Build.VERSION_CODES.O)
    constructor():this("",null, "", "")
}