package com.example.myuni.ui.fragment

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentSellBinding
import com.example.myuni.model.Goods
import com.example.myuni.utils.BitmapUtils
import com.example.myuni.utils.OrderUtils
import com.example.myuni.utils.Utils
import com.example.myuni.viewmodel.GoodsViewModel
import com.example.myuni.viewmodel.MeViewModel
import com.example.myuni.viewmodel.UtilViewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class SellFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentSellBinding
    private lateinit var utilViewModel: UtilViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var goodsViewModel: GoodsViewModel
    private var orderNum = ""
    private var position = -1
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add("Submit").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (checkContents()){
            NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_sell_to_navigation_shop)
            Toast.makeText(context, "Added item successfully", Toast.LENGTH_SHORT).show()
        }else
            Toast.makeText(context, "Please complete all information", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sell, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun initViewModel() {
        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
        goodsViewModel = ViewModelProvider(requireActivity()).get(GoodsViewModel::class.java)
    }

    private fun init(){
        utilViewModel.setNavBarStatus(true)

        if (arguments != null){
            val bundle = requireArguments()
            val goods = bundle.getSerializable("goods")!! as Goods
            orderNum = goods.orderNum

            binding.imgBtnTakePic1.visibility = View.GONE
            binding.etTittle.setText(goods.name)
            binding.etDescription.setText(goods.description)
            binding.etPrice.setText(goods.price)
            binding.ivPhoto1.setImageBitmap(BitmapUtils.convertStringToIcon(goods.image1))
            if (goods.image2!!.isNotEmpty()){
                binding.ivPhoto2.setImageBitmap(BitmapUtils.convertStringToIcon(goods.image2))
                binding.imgBtnTakePic2.visibility = View.GONE
            }

        }else{
            binding.ivPhoto1.visibility = View.INVISIBLE
            binding.ivPhoto2.visibility = View.INVISIBLE
        }

        //set listener
        binding.ivPhoto1.setOnClickListener(this)
        binding.ivPhoto2.setOnClickListener(this)
        binding.imgBtnTakePic1.setOnClickListener(this)
        binding.imgBtnTakePic2.setOnClickListener(this)

//        binding.ivPhoto1.setOnClickListener {
//            position = 0
//            show()
//        }
//        binding.ivPhoto2.setOnClickListener {
//            position = 1
//            show()
//        }
//        binding.imgBtnTakePic1.setOnClickListener {
//            position = 0
//            show()
//        }
//        binding.imgBtnTakePic2.setOnClickListener {
//            position = 1
//            show()
//        }

    }

    private fun show() {
        val buttonDialog = Dialog(requireContext(),R.style.Theme_AppCompat_DayNight_Dialog)
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_content,null)
        buttonDialog.setContentView(view)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.width = resources.displayMetrics.widthPixels
        view.layoutParams = layoutParams
        buttonDialog.window?.setGravity(Gravity.BOTTOM)
        buttonDialog.window?.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog)
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


    private fun checkContents(): Boolean{
        if (binding.etTittle.text != null && binding.etDescription.text != null && binding.ivPhoto1.drawable != null && binding.etPrice.text != null){
            val tittle = binding.etTittle.text.toString()
            val price = binding.etPrice.text.toString()
            val des = binding.etDescription.text.toString()
            val img1 = BitmapUtils.convertIconToString(binding.ivPhoto1.drawable.toBitmap())

            val img2: String = if (binding.ivPhoto2.drawable != null)
                BitmapUtils.convertIconToString(binding.ivPhoto2.drawable.toBitmap())
            else
                ""

            val currentUser = meViewModel.getLoginUser()!!
            val orderNumber: String = if (orderNum.isNotEmpty())
                orderNum
            else
                OrderUtils.getOrderNumber()

            val newGood : Goods = if (binding.ivPhoto2.drawable != null){
                Goods(tittle, price, des, img1, img2, currentUser.email!!, currentUser.nation!!, orderNumber, Goods.SELLING, "", "", "", "", "", "")
            } else{
                Goods(tittle, price, des, img1, img2, currentUser.email!!, currentUser.nation!!, orderNumber, Goods.SELLING, "", "", "", "", "", "")
            }

            goodsViewModel.addGoods(newGood, meViewModel.getLoginUser()?.email.toString())
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        lateinit var imageBitmap: Bitmap
        if (requestCode == Utils.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
        } else if(requestCode == RegisterFragment.PHOTO_REQUEST_GALLERY && resultCode == RESULT_OK){
            val resolver: ContentResolver = requireActivity().contentResolver
            try {
                val inputStream: InputStream? = resolver.openInputStream(data?.data!!)
                imageBitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }

        if (position == 0){
            binding.ivPhoto1.setImageBitmap(imageBitmap)
            binding.ivPhoto1.visibility = View.VISIBLE
            binding.imgBtnTakePic1.visibility = View.INVISIBLE
        }else if (position == 1){
            binding.ivPhoto2.setImageBitmap(imageBitmap)
            binding.ivPhoto2.visibility = View.VISIBLE
            binding.imgBtnTakePic2.visibility = View.INVISIBLE
        }

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.iv_photo1, R.id.img_btn_takePic1 -> {
                position = 0
                show()
            }
            R.id.iv_photo2, R.id.img_btn_takePic2 -> {
                position = 1
                show()
            }
        }
    }
}