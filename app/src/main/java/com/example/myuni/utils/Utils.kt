package com.example.myuni.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.myuni.R

class Utils{
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val PHOTO_REQUEST_GALLERY = 2

        val countriesName = listOf("China",
                                                "Korea",
                                                "Japan",
                                                "Philippines",
                                                "Vietnam",
                                                "India",
                                                "Finland",
                                                "Sweden",
                                                "Russia",
                                                "Ukraine",
                                                "Poland",
                                                "Germany",
                                                "Switzerland",
                                                "Britain",
                                                "Ireland",
                                                "France")

        fun closeKeyBoard(content: Context) {
            val imm = content.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }



}