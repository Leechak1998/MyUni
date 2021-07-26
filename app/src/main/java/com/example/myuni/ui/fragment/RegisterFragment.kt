package com.example.myuni.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentContactsBinding
import com.example.myuni.databinding.FragmentRegisterBinding
import com.example.myuni.model.Contacts
import com.example.myuni.viewmodel.ContactsViewModel
import com.example.myuni.viewmodel.MeViewModel

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var meViewModel: MeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(inflater, R.layout.fragment_register, container, false)
        meViewModel = ViewModelProvider(this).get(MeViewModel::class.java)
        return binding.root
    }


    fun registerNewUser(){
        val email = binding.etEmail.text.toString()
        val userName = binding.etUsername.text.toString()
        val passWord = binding.etPassword.text.toString()
        //头像目前默认头像
        var newContacts = Contacts(userName, R.drawable.profile_default, email, passWord)
        meViewModel.registerNewUser(newContacts)
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_register_to_navigation_login)
    }

    fun cancel(){
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_register_to_navigation_login)
    }
}