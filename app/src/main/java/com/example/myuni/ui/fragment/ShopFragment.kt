package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.adapter.ContactsAdapter
import com.example.myuni.adapter.GoodsAdapter
import com.example.myuni.databinding.FragmentShopBinding
import com.example.myuni.model.Contacts
import com.example.myuni.model.Goods
import com.example.myuni.viewmodel.GoodsViewModel
import com.example.myuni.viewmodel.MeViewModel


class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding
    private lateinit var goodsViewModel: GoodsViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var goodsAdapter: GoodsAdapter
    private var goodsList = ArrayList<Goods>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add("Sell").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_shop_to_navigation_sell)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false)

        init()

        return binding.root
    }

    private fun init(){
        goodsViewModel = ViewModelProvider(requireActivity()).get(GoodsViewModel::class.java)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)

        goodsViewModel.goodsList.observe(viewLifecycleOwner, Observer {
            goodsList.clear()
            goodsList.addAll(it)
            goodsAdapter.notifyDataSetChanged()
            binding.lvGoods.setSelection(it.size)
            println("更新二手物品列表")
            for (i in goodsList.indices){
                println("------$i----${goodsList[i].name}")
            }
        })
        goodsAdapter = GoodsAdapter(requireContext(), R.layout.goods_item, goodsList)
        binding.goodsAdapter = goodsAdapter

        goodsViewModel.initGoodsList(meViewModel.getLoginUser()?.email!!)
    }

}