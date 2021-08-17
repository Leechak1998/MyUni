package com.example.myuni.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myuni.R
import com.example.myuni.adapter.GoodsAdapter
import com.example.myuni.databinding.FragmentOrderSellBinding
import com.example.myuni.model.Goods
import com.example.myuni.viewmodel.GoodsViewModel
import com.example.myuni.viewmodel.MeViewModel

class OrderBuyFragment : Fragment() {
    private lateinit var binding: FragmentOrderSellBinding
    private lateinit var goodsViewModel: GoodsViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var adapter: GoodsAdapter
    private var buyingList = ArrayList<Goods>()
    private lateinit var currentUser: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_order_sell, container, false)
        initViewModel()
        init()

        return binding.root
    }

    private fun initViewModel(){
        goodsViewModel = ViewModelProvider(requireActivity()).get(GoodsViewModel::class.java)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
    }

    private fun init(){
        currentUser = meViewModel.getLoginUser()!!.email.toString()

        goodsViewModel.buyingList.observe(viewLifecycleOwner, Observer {
            buyingList.clear()
            buyingList.addAll(it)
            adapter.notifyDataSetChanged()
            binding.lvSellList.setSelection(it.size)
            println("更新出售物品列表")
            for (i in buyingList.indices){
                println("------$i----${buyingList[i].name}")
            }
        })
        adapter = GoodsAdapter(requireContext(), R.layout.goods_item, buyingList)
        binding.adapter = adapter

        goodsViewModel.getBuyingList(meViewModel.getLoginUser()!!.email!!)
    }

    companion object {
        @JvmStatic
        fun newInstance() = OrderBuyFragment()
    }
}