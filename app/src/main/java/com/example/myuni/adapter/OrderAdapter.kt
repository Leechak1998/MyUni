package com.example.myuni.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myuni.ui.fragment.OrderBuyFragment
import com.example.myuni.ui.fragment.OrderSellFragment

class OrderAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val items = 2
    override fun getItemCount(): Int {
        return items
    }

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> OrderBuyFragment.newInstance()
        1 -> OrderSellFragment()
        else -> OrderBuyFragment.newInstance()
    }

}