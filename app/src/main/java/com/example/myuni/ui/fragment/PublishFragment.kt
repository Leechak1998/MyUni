package com.example.myuni.ui.fragment

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentPublishBinding
import com.example.myuni.model.Contacts
import com.example.myuni.model.Posting
import com.example.myuni.utils.BitmapUtils
import com.example.myuni.utils.OrderUtils
import com.example.myuni.utils.TimeUtils
import com.example.myuni.utils.Utils
import com.example.myuni.viewmodel.MeViewModel
import com.example.myuni.viewmodel.PostingViewModel
import com.example.myuni.viewmodel.UtilViewModel
import java.io.FileNotFoundException
import java.io.InputStream
import java.time.LocalDateTime

class PublishFragment : Fragment(), RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private lateinit var binding: FragmentPublishBinding
    private lateinit var currentUser: Contacts
    private lateinit var currentUserPic: String
    private var tittle: String? = null
    private var description : String? = null
    private var radioGroup : RadioGroup? = null
    private var category : String? = null
    private var image1 : String? = null
    private var image2 : String? = null
    private var image3 : String? = null
    private var selectedImage = 0
    private lateinit var utilViewModel: UtilViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var postingViewModel: PostingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add("Submit").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (checkContents()){
            publishPosting()
            NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.navigation_posting)
            Toast.makeText(context, "Publish successfully", Toast.LENGTH_SHORT).show()
        }else
            Toast.makeText(context, "Please complete all information", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_publish, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun init(){
        utilViewModel.setNavBarStatus(true)
        currentUser = meViewModel.getLoginUser()!!
        currentUserPic = meViewModel.getLoginUser()!!.imageId.toString()

        radioGroup = binding.radioGroup
        radioGroup!!.setOnCheckedChangeListener(this)
        binding.ivImage1.setOnClickListener(this)
        binding.ivImage2.setOnClickListener(this)
        binding.ivImage3.setOnClickListener(this)
    }

    private fun initViewModel(){
        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
        postingViewModel = ViewModelProvider(requireActivity()).get(PostingViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun publishPosting() {
        val orderNumber = OrderUtils.getOrderNumber()
        val currentTime = TimeUtils.getCurrentTime(LocalDateTime.now())
        println("--tittle:$tittle-- --des:$description--")
        val newPosting = Posting(orderNumber, category, currentUser.email, currentUser.name, currentUserPic, currentUser.nation, currentUser.uni, tittle, description, image1, image2, image3, currentTime)
        postingViewModel.publishPosting(newPosting)
    }

    private fun checkContents(): Boolean {
        tittle = binding.etTittle.text.toString()
        description = binding.etDescription.text.toString()
        radioGroup = binding.radioGroup
        image1 = BitmapUtils.convertIconToString(binding.ivImage1.drawable.toBitmap())
        image2 = if (binding.ivImage2.drawable != null)
            BitmapUtils.convertIconToString(binding.ivImage2.drawable.toBitmap())
        else
            ""
        image3 = if (binding.ivImage3.drawable != null)
            BitmapUtils.convertIconToString(binding.ivImage3.drawable.toBitmap())
        else
            ""
        if (category != null && tittle != null && description != null){
            return true
        }
        return false
    }

    private fun show() {
        val buttonDialog = Dialog(requireContext()/*,R.style.Theme_AppCompat_DayNight_Dialog*/)
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_content,null)
        buttonDialog.setContentView(view)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.width = resources.displayMetrics.widthPixels
        view.layoutParams = layoutParams
        buttonDialog.window?.setGravity(Gravity.BOTTOM)
//        buttonDialog.window?.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog)
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
        if (requestCode == Utils.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
        } else if(requestCode == Utils.PHOTO_REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            val resolver: ContentResolver = requireActivity().contentResolver
            try {
                val inputStream: InputStream? = resolver.openInputStream(data?.data!!)
                imageBitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }

        when(selectedImage){
            1 -> binding.ivImage1.setImageBitmap(imageBitmap)
            2 -> binding.ivImage2.setImageBitmap(imageBitmap)
            3 -> binding.ivImage3.setImageBitmap(imageBitmap)
        }

    }

    override fun onCheckedChanged(radioGroup: RadioGroup?, p1: Int) {
        when(p1){
            R.id.rb_study -> category = binding.rbStudy.text.toString()
            R.id.rb_life -> category = binding.rbLife.text.toString()
            R.id.rb_entertainment -> category = binding.rbEntertainment.text.toString()
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.iv_image1 -> {
                show()
                binding.ivImage1.background = null
                selectedImage = 1
            }
            R.id.iv_image2 -> {
                show()
                binding.ivImage2.background = null
                selectedImage = 2
            }
            R.id.iv_image3 -> {
                show()
                binding.ivImage3.background = null
                selectedImage = 3
            }
        }
    }

}