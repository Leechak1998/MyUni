package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentMeBinding
import com.example.myuni.model.Contacts
import com.example.myuni.ui.activity.MainActivity
import com.example.myuni.utils.BitmapUtils
import com.example.myuni.viewmodel.MeViewModel

class MeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentMeBinding
    private lateinit var meViewModel: MeViewModel
    private lateinit var currentUser: Contacts

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false)
        initViewModel()
        init()



        return binding.root
    }

    private fun initViewModel(){
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
    }

    private fun init(){
        currentUser = meViewModel.getLoginUser()!!
        binding.im.setImageBitmap(BitmapUtils.convertStringToIcon(currentUser.imageId))
        binding.tv.text = currentUser.name

        binding.btnProfile.setOnClickListener(this)
        binding.btnPosting.setOnClickListener(this)
        binding.btnMyOrder.setOnClickListener(this)
        binding.btnOut.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnProfile ->
                Toast.makeText(requireContext(), "Go to profile page", Toast.LENGTH_SHORT).show()
            R.id.btnPosting ->
                Navigation.findNavController(view).navigate(R.id.navigation_myPostings)
            R.id.btnMyOrder ->
                Navigation.findNavController(view).navigate(R.id.navigation_myOrder)
            R.id.btnOut -> {
                meViewModel.logOut()
                Navigation.findNavController(view).navigate(R.id.navigation_login)
            }

        }
    }

}