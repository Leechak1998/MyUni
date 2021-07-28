package com.example.myuni.model

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.myuni.utils.BitmapUtils


class Goods constructor(name: String?, price: String?, description: String?, image1: String?, image2: String?) {

    val name = name
    val price = price
    val description = description
    val image1 = image1
    val image2 = image2

    companion object {
        @BindingAdapter("app:setImageBitmap")
        @JvmStatic
        fun setImageViewBitmap(imageView: ImageView, string: String) {
            imageView.setImageBitmap(BitmapUtils.convertStringToIcon(string))
        }
    }

    constructor():this("", "", "", null, null)
}