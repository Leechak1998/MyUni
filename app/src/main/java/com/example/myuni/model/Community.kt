package com.example.myuni.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.myuni.utils.BitmapUtils

class Community constructor(communityNum: String, profile: String, name: String, description: String, nationality: String, creator: String) {
    val communityNum = communityNum
    val profile = profile
    val name = name
    val description = description
    val nationality = nationality
    val creator = creator

    companion object {
        @BindingAdapter("app:setImageBitmap2")
        @JvmStatic
        fun setImageViewBitmap2(imageView: ImageView, string: String) {
            imageView.setImageBitmap(BitmapUtils.convertStringToIcon(string))
        }
    }

    constructor():this("", "", "", "", "", "")
}