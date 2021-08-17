package com.example.myuni.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.myuni.R
import com.example.myuni.databinding.CommonDialogLayoutBinding


class CommonDialog : DialogFragment() {
    private lateinit var binding: CommonDialogLayoutBinding
    var name: String = ""
    var email: String = ""
    var nation: String = ""
    var uni: String = ""
    var profile: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.common_dialog_layout, container, false)
        init()
        return binding.root
    }

    private fun init() {
        binding.tvName.text = name
        binding.tvEmail.text = email
        binding.tvNation.text = nation
        binding.tvUni.text = uni
        binding.ivImage.setImageBitmap(BitmapUtils.convertStringToIcon(profile))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog
    }
}