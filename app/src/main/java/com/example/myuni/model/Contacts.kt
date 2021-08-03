package com.example.myuni.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.myuni.utils.BitmapUtils

class Contacts constructor(name: String?, imageId: String?, email: String?, password: String?, uni: String){
    val name: String? = name
    val imageId: String? = imageId
    val email: String? = email
    val password: String? = password
    val uni: String? = uni

    companion object {
        @BindingAdapter("app:setImageBitmap")
        @JvmStatic
        fun setImageViewBitmap(imageView: ImageView, string: String) {
            imageView.setImageBitmap(BitmapUtils.convertStringToIcon(string))
        }
    }

    constructor():this("",null,"", "", "")

}