package com.example.myuni.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentSellBinding

class SellFragment : Fragment() {
    private lateinit var binding: FragmentSellBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sell, container, false)

        init()
        dispatchTakePictureIntent()
        return binding.root
    }

    private fun init(){
        binding.ivPhoto2.visibility = View.GONE

        binding.imgBtnTakePic.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            if (binding.ivPhoto.drawable == null)
                binding.ivPhoto.setImageBitmap(imageBitmap)
            else{
                binding.ivPhoto2.setImageBitmap(imageBitmap)
                binding.ivPhoto2.visibility = View.VISIBLE
                binding.imgBtnTakePic.visibility = View.GONE
            }

        }
    }

    companion object{
        const val REQUEST_IMAGE_CAPTURE = 1
    }

}