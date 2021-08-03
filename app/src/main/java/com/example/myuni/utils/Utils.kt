package com.example.myuni.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager

class Utils{
    companion object{
        fun closeKeyBoard(content: Context){
            val imm = content.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


}