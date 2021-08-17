package com.example.myuni.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.myuni.R
import com.example.myuni.databinding.FragmentSellBinding
import com.example.myuni.model.Goods
import com.example.myuni.utils.BitmapUtils

class OrderDetailsFragment : Fragment() {
    private lateinit var binding: FragmentSellBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sell, container, false)
        init()
        return binding.root
    }

    private fun init(){
        var bundle = requireArguments()
        val goods = bundle.getSerializable("goods")!! as Goods

        binding.imgBtnTakePic1.visibility = View.GONE
        binding.etTittle.setText(goods.name)
        binding.etDescription.setText(goods.description)
        binding.etPrice.setText(goods.price)
        binding.ivPhoto1.setImageBitmap(BitmapUtils.convertStringToIcon(goods.image1))
        if (goods.image2!!.isNotEmpty())
            binding.ivPhoto2.setImageBitmap(BitmapUtils.convertStringToIcon(goods.image2))
        else
            binding.imgBtnTakePic2.visibility = View.VISIBLE

    }

}