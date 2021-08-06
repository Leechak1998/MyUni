package com.example.myuni.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.myuni.utils.BitmapUtils
import java.io.Serializable


class Goods constructor(name: String?, price: String?, description: String?, image1: String?, image2: String?, owner: String, orderNum: String): Serializable{

    val name = name
    val price = price
    val description = description
    val image1 = image1
    val image2 = image2
    val owner = owner
    val orderNum = orderNum

    companion object {
        @BindingAdapter("app:setImageBitmap")
        @JvmStatic
        fun setImageViewBitmap(imageView: ImageView, string: String) {
            imageView.setImageBitmap(BitmapUtils.convertStringToIcon(string))
        }
    }

    constructor():this("", "", "", null, null, "", "")
}