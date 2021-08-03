package com.example.myuni.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.myuni.R
import com.example.myuni.adapter.OrderAdapter
import com.example.myuni.databinding.FragmentMyOrderBinding
import com.google.android.material.tabs.TabLayoutMediator


class MyOrderFragment : Fragment(){
    private lateinit var binding: FragmentMyOrderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_order, container, false)

        binding.viewpager.adapter = OrderAdapter(requireActivity())
        TabLayoutMediator(binding.tabs,  binding.viewpager) { tab, position ->
            binding.viewpager.setCurrentItem(0, true)
            when (position) {
                0 -> tab.text = "Buying"
                1 -> tab.text = "Selling"
            }
        }.attach()

        return binding.root
    }

}
