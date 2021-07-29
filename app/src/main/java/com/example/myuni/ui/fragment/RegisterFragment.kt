package com.example.myuni.ui.fragment

import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentRegisterBinding
import com.example.myuni.model.Contacts
import com.example.myuni.utils.BitmapUtils
import com.example.myuni.viewmodel.MeViewModel
import java.io.FileNotFoundException
import java.io.InputStream


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var meViewModel: MeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentRegisterBinding>(inflater, R.layout.fragment_register, container, false)

        init()
        return binding.root
    }

    private fun init(){
        binding.register = this
        meViewModel = ViewModelProvider(this).get(MeViewModel::class.java)

        binding.ivPrifile.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }


    fun registerNewUser(){
        val email = binding.etEmail.text.toString()
        val userName = binding.etUsername.text.toString()
        val passWord = binding.etPassword.text.toString()
        //默认头像
        val profile = BitmapUtils.convertIconToString(binding.ivPrifile.drawable.toBitmap())

        var newContacts = Contacts(userName, profile, email, passWord)
        meViewModel.registerNewUser(newContacts)
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_register_to_navigation_login)
    }

    fun cancel(){
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_register_to_navigation_login)
    }

    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
//                startActivityForResult(takePictureIntent, SellFragment.REQUEST_IMAGE_CAPTURE)
//            }
//        }


        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        Intent(Intent.ACTION_PICK).also { pickPicture ->
            pickPicture.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*").also {
                startActivityForResult(pickPicture, PHOTO_REQUEST_GALLERY)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

        } else if( requestCode == PHOTO_REQUEST_GALLERY && resultCode == RESULT_OK){
            //val imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data?.extras?.get("data") as Uri?)
//            val imageBitmap = data?.extras?.getParcelable<Bitmap>("data")
//            println("allocationByteCount: ${imageBitmap?.allocationByteCount} --- generationId: ${imageBitmap?.generationId} --- byteCount: ${imageBitmap?.byteCount}")


            val resolver: ContentResolver = requireActivity().contentResolver
            try {
                val inputStream: InputStream? = resolver.openInputStream(data?.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.ivPrifile.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Destoty RegisterFragment")
    }

    companion object{
        const val REQUEST_IMAGE_CAPTURE = 1
        const val PHOTO_REQUEST_GALLERY = 2
    }

}