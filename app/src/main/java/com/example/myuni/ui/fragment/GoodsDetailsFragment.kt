package com.example.myuni.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.myuni.R
import com.example.myuni.databinding.FragmentGoodsDetailsBinding
import com.example.myuni.model.Goods
import com.example.myuni.utils.BitmapUtils
import com.example.myuni.viewmodel.GoodsViewModel
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast

class GoodsDetailsFragment : Fragment() {
    private lateinit var binding: FragmentGoodsDetailsBinding
    private lateinit var goodsViewModel: GoodsViewModel
    private lateinit var goods: Goods
    private var currentUser: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goods_details, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun initViewModel(){
        goodsViewModel = ViewModelProvider(requireActivity()).get(GoodsViewModel::class.java)
    }

    private fun init(){
        var bundle = requireArguments()
        goods = bundle.getSerializable("goods")!! as Goods
        currentUser = bundle.getString("currentUser")

        binding.tvTittle.text = goods.name
        binding.tvDescription.text = goods.description
        binding.tvPrice.text = goods.price
        binding.tvStatus.text = goods.status
        binding.ivPhoto1.setImageBitmap(BitmapUtils.convertStringToIcon(goods.image1))
        if (goods.image2!!.isNotEmpty()){
            binding.ivPhoto2.setImageBitmap(BitmapUtils.convertStringToIcon(goods.image2))
        }

        //when seller himself views this item
        if (currentUser == goods.owner){
            binding.btnPurchase.visibility = View.INVISIBLE
            if (goods.status == Goods.PENDING){
                binding.btnContact.text = "Confirm"
                binding.btnContact.setOnClickListener { it ->
                    goodsViewModel.confirmGoods(goods, currentUser!!)
                    this.fragmentManager?.popBackStack()
                    toast("Confirm successfully!")
                }
            }else{
                binding.btnContact.text = "Edit"
                binding.btnContact.setOnClickListener { it ->
                    val bundle = Bundle().also {
                        it.putSerializable("goods", goods)
                    }
                    Navigation.findNavController(it).navigate(R.id.navigation_sell, bundle)
                }
            }
        }
        // when buyers views this item
        else{
            binding.btnPurchase.visibility = View.VISIBLE
            binding.btnPurchase.setOnClickListener {view ->
                alert("Do you want to purchase this item?","Purchase"){
                    positiveButton("Yes"){
                        goodsViewModel.purchaseGoods(goods, currentUser!!)
                        Toast.makeText(context, "Purchase successfully", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(view).navigate(R.id.navigation_shop)
                    }
                    negativeButton("Consider again..."){ }
                }.show()
            }

            binding.btnContact.setOnClickListener { it ->
                val bundle = Bundle().also {
                    it.putString("email", goods.owner)
                }
                Navigation.findNavController(it).navigate(R.id.navigation_chat, bundle)
            }
        }
    }

}