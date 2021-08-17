package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentLoginBinding
import com.example.myuni.utils.Uni
import com.example.myuni.viewmodel.MeViewModel
import com.example.myuni.viewmodel.UtilViewModel


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var meViewModel: MeViewModel
    private lateinit var utilViewModel: UtilViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun initViewModel(){
        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
    }

    private fun init(){
        utilViewModel.setNavBarStatus(true)
        binding.avi.hide()

        binding.login = this
        meViewModel.isLogin.observe(viewLifecycleOwner, Observer {
            println("登陆状态改变->$it")
            if (it == 1){
                println("登陆成功")
                binding.avi.hide()
                utilViewModel.setNavBarStatus(false)

                NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.navigation_home)
                Toast.makeText(context, "Welcome, ${meViewModel.getLoginUser()!!.name}", Toast.LENGTH_LONG).show()
            } else if (it == -1){
                binding.avi.hide()
                Toast.makeText(context,"Invalid email or password!", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun login(){
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        meViewModel.login(email, password)
        binding.avi.show()
    }

    fun register(){
        NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.navigation_register)
    }

}