package com.example.myuni.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.myuni.utils.BitmapUtils

class Contacts constructor(name: String?, imageId: String?, email: String?, password: String?){
    val name: String? = name
    val imageId: String? = imageId
    val email: String? = email
    val password: String? = password

    companion object {
        @BindingAdapter("app:setImageBitmap")
        @JvmStatic
        fun setImageViewBitmap(imageView: ImageView, string: String) {
            imageView.setImageBitmap(BitmapUtils.convertStringToIcon(string))
        }
    }

//    //没加JVMStatic，图片无法显示！！！！
//    companion object {
//        @BindingAdapter("android:src")
//        @JvmStatic
//        fun setImageViewResource(imageView: ImageView, resource: Int) {
//            imageView.setImageResource(resource)
//        }
//    }

    constructor():this("",null,"", "")

}