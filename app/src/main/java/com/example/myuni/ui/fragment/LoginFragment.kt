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
import com.example.myuni.viewmodel.MeViewModel
import com.example.myuni.viewmodel.UtilViewModel


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var meViewModel: MeViewModel
    private lateinit var utilViewModel: UtilViewModel
    private var isLogin: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)
        binding.login = this
        binding.avi.hide()
        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
        utilViewModel.setNavBarStatus(true)

        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)

        meViewModel.text.observe(viewLifecycleOwner, Observer {
            isLogin = it
//            println("isLogin => $isLogin      it => $it")
            if (isLogin == 1){
                println("登陆成功")
                binding.avi.hide()
                utilViewModel.setNavBarStatus(false)
                NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_login_to_navigation_home)
                Toast.makeText(context, "User ${meViewModel.getLoginUser()!!.name} login", Toast.LENGTH_LONG).show()
            } else if (isLogin == -1){
                binding.avi.hide()
                Toast.makeText(context,"Invalid email or password!", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    fun login(){
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        meViewModel.login(email, password)
        binding.avi.show()
    }

    fun register(){
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_login_to_navigation_register)
    }

}