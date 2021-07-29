package com.example.myuni.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myuni.model.Message
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class TimeUtils{
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentTime(time: LocalDateTime): String{
            return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        }

        fun transToString(time:Long):String{
            return SimpleDateFormat("YYYY-MM-DD-hh-mm-ss").format(time)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun orderChatList(chatList: ArrayList<Message>): ArrayList<Message>{
            var temp: Message
            for (i in 0 until chatList.size-1){
                for (j in 0 until chatList.size-1-i){
                    var date1 = SimpleDateFormat("YYYY-MM-DD hh:mm:ss").parse(chatList[j].time)
                    var date2 = SimpleDateFormat("YYYY-MM-DD hh:mm:ss").parse(chatList[j+1].time)
                    when {
                        date1 >= date2 -> {
                            temp = chatList[j]
                            chatList[j] = chatList[j+1]
                            chatList[j+1] = temp
                        }

                    }
                }
            }

            return chatList
        }
    }
}