package com.example.myuni.ui.fragment

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
//import com.example.myuni.R
import com.example.myuni.databinding.FragmentRegisterBinding
import com.example.myuni.model.Contacts
import com.example.myuni.utils.BitmapUtils
import com.example.myuni.utils.Uni
import com.example.myuni.utils.Utils
import com.example.myuni.viewmodel.MeViewModel
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.toast
import java.io.FileNotFoundException
import java.io.InputStream


class RegisterFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var meViewModel: MeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun initViewModel() {
        meViewModel = ViewModelProvider(this).get(MeViewModel::class.java)
    }

    private fun init(){
        binding.register = this

        //set listener
        binding.ivProfile.setOnClickListener(this)

        binding.tvSpinner.text = "Please select your University"
        binding.tvSpinner.setOnClickListener(this)

        binding.tvNationality.text = "Please select your nationality"
        binding.tvNationality.setOnClickListener(this)    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun registerNewUser(){
        //默认头像
        val profile = BitmapUtils.convertIconToString(binding.ivProfile.drawable.toBitmap())
        val email = binding.etEmail.text.toString()
        val userName = binding.etUsername.text.toString()
        val passWord = binding.etPassword.text.toString()
        val uni = binding.tvSpinner.text.toString()
        val nation = binding.tvNationality.text.toString()

        if (checkInfo(email, userName, passWord, uni, nation)){
            var newContacts = Contacts(userName, profile, email, passWord, uni, nation)
            meViewModel.registerNewUser(newContacts)
            NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_register_to_navigation_login)
        }else
            toast("Please complete all information")

        Utils.closeKeyBoard(requireContext())
    }

    fun cancel(){
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_register_to_navigation_login)
    }

    private fun checkInfo(email: String, userName: String, passWord: String, uni: String, nation: String): Boolean{
        if (email != "" && userName != "" && passWord != "" && uni != "Please select your University" && nation != "Please select your nationality")
            return true
        return false
    }

    private fun show() {
        val buttonDialog = Dialog(requireContext())
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_content,null)
        buttonDialog.setContentView(view)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.width = resources.displayMetrics.widthPixels
        view.layoutParams = layoutParams
        buttonDialog.window?.setGravity(Gravity.BOTTOM)
        //buttonDialog.window?.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog)
        buttonDialog.show()

        val photo = buttonDialog.findViewById<TextView>(R.id.tv_photo)
        val album = buttonDialog.findViewById<TextView>(R.id.tv_album)
        val cancel = buttonDialog.findViewById<TextView>(R.id.tv_cancel)

        photo.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                    startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE)
                }
            }
            buttonDialog.dismiss()
        }

        album.setOnClickListener {
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
            Intent(Intent.ACTION_PICK).also { pickPicture ->
                pickPicture.setType("image/*").also {
                    startActivityForResult(pickPicture, Utils.PHOTO_REQUEST_GALLERY)
                }
            }
            buttonDialog.dismiss()
        }

        cancel.setOnClickListener {
            buttonDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        lateinit var imageBitmap: Bitmap
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap

        } else if( requestCode == PHOTO_REQUEST_GALLERY && resultCode == RESULT_OK){
            val resolver: ContentResolver = requireActivity().contentResolver
            try {
                val inputStream: InputStream? = resolver.openInputStream(data?.data!!)
                imageBitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }

        binding.ivProfile.setImageBitmap(imageBitmap)
    }

    companion object{
        const val REQUEST_IMAGE_CAPTURE = 1
        const val PHOTO_REQUEST_GALLERY = 2
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.iv_profile -> {
                show()
            }
            R.id.tvSpinner -> {
                selector("Please select your Uni", Uni.uniName) { _, i ->
                    binding.tvSpinner.text = Uni.uniName[i]
                }
            }
            R.id.tvNationality -> {
                selector("Please select your nationality", Utils.countriesName) { _, i ->
                    binding.tvNationality.text = Utils.countriesName[i]
                }
            }
        }
    }

}