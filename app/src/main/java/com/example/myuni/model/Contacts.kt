package com.example.myuni.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.myuni.utils.BitmapUtils
import java.io.Serializable

class Contacts constructor(name: String?, imageId: String?, email: String?, password: String?, uni: String, nation: String): Serializable{
    var name: String? = name
    var imageId: String? = imageId
    var email: String? = email
    var password: String? = password
    var uni: String? = uni
    var nation: String? = nation

    companion object {
        @BindingAdapter("app:setImageBitmap")
        @JvmStatic
        fun setImageViewBitmap(imageView: ImageView, string: String) {
            imageView.setImageBitmap(BitmapUtils.convertStringToIcon(string))
        }
    }

    constructor():this("",null,"", "", "", "")

}