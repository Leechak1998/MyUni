package com.example.myuni.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentSellBinding
import com.example.myuni.model.Goods
import com.example.myuni.utils.BitmapUtils
import com.example.myuni.viewmodel.GoodsViewModel
import com.example.myuni.viewmodel.MeViewModel
import com.example.myuni.viewmodel.UtilViewModel

class SellFragment : Fragment() {
    private lateinit var binding: FragmentSellBinding
    private lateinit var utilViewModel: UtilViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var goodsViewModel: GoodsViewModel

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

        init()
        dispatchTakePictureIntent()
        return binding.root
    }

    private fun init(){
        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
        utilViewModel.setNavBarStatus(true)

        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
        goodsViewModel = ViewModelProvider(requireActivity()).get(GoodsViewModel::class.java)

        binding.ivPhoto1.visibility = View.GONE
        binding.ivPhoto2.visibility = View.GONE

        binding.imgBtnTakePic1.setOnClickListener { dispatchTakePictureIntent() }
        binding.imgBtnTakePic2.setOnClickListener { dispatchTakePictureIntent() }


    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun checkContents(): Boolean{
        if (binding.etTittle.text != null && binding.etDescription.text != null && binding.ivPhoto1.drawable != null && binding.etPrice.text != null){

            var newGood : Goods = if (binding.ivPhoto2.drawable != null){
                Goods(binding.etTittle.text.toString(), binding.etPrice.text.toString(), binding.etDescription.text.toString(), BitmapUtils.convertIconToString(binding.ivPhoto1.drawable.toBitmap()), BitmapUtils.convertIconToString(binding.ivPhoto2.drawable.toBitmap()))
            } else{
                Goods(binding.etTittle.text.toString(), binding.etPrice.text.toString(), binding.etDescription.text.toString(), BitmapUtils.convertIconToString(binding.ivPhoto1.drawable.toBitmap()), "")
            }

            goodsViewModel.addGoods(newGood, meViewModel.getLoginUser()?.email.toString())
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            if (binding.ivPhoto1.drawable == null) {

                binding.ivPhoto1.setImageBitmap(imageBitmap)
//                println("看看drawable是什么 ${binding.ivPhoto1.drawable} +++ $imageBitmap")
                binding.ivPhoto1.visibility = View.VISIBLE
                binding.imgBtnTakePic1.visibility = View.GONE
            }else{
                binding.ivPhoto2.setImageBitmap(imageBitmap)
                binding.ivPhoto2.visibility = View.VISIBLE
                binding.imgBtnTakePic2.visibility = View.GONE
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        utilViewModel.setNavBarStatus(false)
    }

    companion object{
        const val REQUEST_IMAGE_CAPTURE = 1
    }

}