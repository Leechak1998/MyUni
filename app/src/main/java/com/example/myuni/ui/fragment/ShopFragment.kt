package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.adapter.ContactsAdapter
import com.example.myuni.adapter.GoodsAdapter
import com.example.myuni.databinding.FragmentShopBinding
import com.example.myuni.model.Contacts
import com.example.myuni.model.Goods


class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding
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

        val g = Goods("MacBook", "20.0", "This is a macBook", (R.drawable.profile_default).toLong())
        goodsList.add(g)

        goodsAdapter = GoodsAdapter(requireContext(), R.layout.goods_item, goodsList)
        binding.goodsAdapter = goodsAdapter


        return binding.root
    }

}