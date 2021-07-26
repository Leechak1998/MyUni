package com.example.myuni.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class timeUtils{
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentTime(time: LocalDateTime): String{
            return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        }

        fun transToString(time:Long):String{
            return SimpleDateFormat("YYYY-MM-DD-hh-mm-ss").format(time)
        }
    }
}