package com.example.myuni.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter

class Contacts constructor(name: String, imageId: Int, email: String){
    val name: String = name
    val imageId: Int = imageId
    val email: String = email

    //没加JVMStatic，图片无法显示！！！！
    companion object {
        @BindingAdapter("android:src")
        @JvmStatic
        fun setImageViewResource(imageView: ImageView, resource: Int) {
            imageView.setImageResource(resource)
        }
    }

}