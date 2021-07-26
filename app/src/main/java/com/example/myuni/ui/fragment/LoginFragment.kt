package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentLoginBinding
import com.example.myuni.model.Message
import com.example.myuni.utils.HttpConnection
import com.example.myuni.viewmodel.MeViewModel
import com.google.firebase.database.*
import java.security.Provider


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var meViewModel: MeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)

        return binding.root
    }

    fun login(){
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (meViewModel.login(email, password))
            NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.action_navigation_login_to_navigation_home)
        else
            Toast.makeText(context,"Invalid email or password!", Toast.LENGTH_SHORT).show()

    }

    fun register(){
        NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.action_navigation_login_to_navigation_register)
    }
}