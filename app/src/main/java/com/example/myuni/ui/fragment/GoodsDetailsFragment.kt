package com.example.myuni.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.myuni.R
import com.example.myuni.databinding.FragmentGoodsDetailsBinding
import com.example.myuni.model.Contacts
import com.example.myuni.model.Goods
import com.example.myuni.utils.BitmapUtils
import com.example.myuni.viewmodel.GoodsViewModel
import com.example.myuni.viewmodel.UtilViewModel
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast

class GoodsDetailsFragment : Fragment() {
    private lateinit var binding: FragmentGoodsDetailsBinding
    private lateinit var goodsViewModel: GoodsViewModel
    private lateinit var utilViewModel: UtilViewModel
    private lateinit var goods: Goods
    private var currentUser: String? = null
    private var owner: Contacts? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goods_details, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun initViewModel(){
        goodsViewModel = ViewModelProvider(requireActivity()).get(GoodsViewModel::class.java)
        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
    }

    private fun init(){
        var bundle = requireArguments()
        goods = bundle.getSerializable("goods")!! as Goods
        currentUser = bundle.getString("currentUser")

        utilViewModel.getUserData(goods.owner)
        utilViewModel.userData.observe(viewLifecycleOwner, Observer {
            owner = it
        })

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
                toast("Buyer purchase this product, go to confirm!")
                binding.btnContact.text = "Confirm"
                binding.btnContact.setOnClickListener { it ->
                    Bundle().apply {
                        putSerializable("good",goods)
                        putString("identity", "seller")
                        findNavController().navigate(R.id.navigation_goodForm,this)
                    }
                }
            }else if(goods.status == Goods.SELLING){
                binding.btnContact.text = "Edit"
                binding.btnContact.setOnClickListener { it ->
                    val bundle = Bundle().also {
                        it.putSerializable("goods", goods)
                    }
                    Navigation.findNavController(it).navigate(R.id.navigation_sell, bundle)
                }
            } else if (goods.status == Goods.DELIVERY){
                binding.btnContact.visibility = View.GONE
                binding.tvPrice.text = goods.finalPrice
            }
        }
        // when buyers views this item
        else{
            if (goods.status == Goods.PENDING){
                binding.btnPurchase.visibility = View.GONE
            }else{
                binding.btnPurchase.visibility = View.VISIBLE
                binding.btnPurchase.setOnClickListener {view ->
                    alert("Do you want to purchase this item?","Purchase"){
                        positiveButton("Yes"){
                            Bundle().apply {
                                putSerializable("good",goods)
                                putString("identity", "buyer")
                                findNavController().navigate(R.id.navigation_goodForm,this)
                            }

                        }
                        negativeButton("Consider again..."){ }
                    }.show()
                }
            }

            binding.btnContact.setOnClickListener { it ->
                val bundle = Bundle().also {
                    it.putSerializable("receiver", owner)
                    it.putString("tittle", owner!!.name)
                }
                Navigation.findNavController(it).navigate(R.id.navigation_chat, bundle)
            }
        }
    }

}