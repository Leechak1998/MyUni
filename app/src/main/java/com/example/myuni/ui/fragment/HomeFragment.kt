package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentHomeBinding
import com.example.myuni.viewmodel.HomeViewModel
import com.example.myuni.viewmodel.MeViewModel

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var currentUser: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun initViewModel(){
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
    }

    private fun init(){
        currentUser = meViewModel.getLoginUser()?.email!!
        binding.ibtnPost.setOnClickListener(this)
        binding.ibtnShop.setOnClickListener(this)
        binding.ibtnNews.setOnClickListener(this)
        binding.ibtnCommunity.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ibtn_post ->
                Navigation.findNavController(v).navigate(R.id.navigation_posting)
            R.id.ibtn_shop ->
                Navigation.findNavController(v).navigate(R.id.navigation_shop)
            R.id.ibtn_news ->
                Navigation.findNavController(v).navigate(R.id.navigation_news)
            R.id.ibtn_community ->
                Navigation.findNavController(v).navigate(R.id.navigation_community)
        }
    }


}