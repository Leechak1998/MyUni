package com.example.myuni.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.myuni.utils.BitmapUtils
import java.io.Serializable

class Posting constructor(postingNum: String?, category: String?, publisherName: String?, publisherPic: String?, tittle: String?, description: String?, image1: String?, image2: String?, image3: String?, time: String): Serializable {

    val postingNum = postingNum
    val category = category
    val publisherName = publisherName
    val publisherPic = publisherPic
    val tittle = tittle
    val description = description
    val image1 = image1
    val image2 = image2
    val image3 = image3
    val time = time

    companion object {
        @BindingAdapter("app:setImageBitmap1")
        @JvmStatic
        fun setImageViewBitmap1(imageView: ImageView, string: String) {
            imageView.setImageBitmap(BitmapUtils.convertStringToIcon(string))
        }
    }

    constructor():this("","","", "", "", "", null, null, null, "")
}