package com.example.myuni.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myuni.R
import com.example.myuni.adapter.MessageAdapter
import com.example.myuni.databinding.FragmentChatBinding
import com.example.myuni.model.Message
import com.example.myuni.viewmodel.ChatViewModel
import com.example.myuni.viewmodel.MeViewModel
import com.example.myuni.viewmodel.UtilViewModel
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val msgList = ArrayList<Message>()
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var utilViewModel: UtilViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var adapter: MessageAdapter
    private var receiver: String = ""
    private var sender: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println("create聊天页面")
        binding = DataBindingUtil.inflate<FragmentChatBinding>(inflater, R.layout.fragment_chat, container, false)

        init()

        chatViewModel._messageList.observe(viewLifecycleOwner, Observer {
            println("=============")
            for (i in 0 until it.size){
                println("-- " + it[i].content + " -- " + it[i].type)
            }
            println("=============")

            msgList.clear()
            msgList.addAll(it)
            adapter.notifyDataSetChanged()
            binding.lvChat.setSelection(it.size)
        })

        return binding.root
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun onSubmit(){
        val content = binding.etInput.text.toString()
        if (content.isNotEmpty()){
            chatViewModel.sendMessage(content)
        }
        binding.etInput.text.clear()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){
        var bundle = requireArguments()
        receiver = bundle.getString("email")!!

        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
        utilViewModel.switchNavBarStatus()

        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
        sender = meViewModel.getLoginUser()!!.email!!

        chatViewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)
        chatViewModel.initChatList(sender, receiver)

        adapter = MessageAdapter(requireContext(), R.layout.message_item, msgList)
        binding.messageAdapter = adapter
        binding.sendMes = this
    }

    override fun onDestroy() {
        super.onDestroy()
        utilViewModel.switchNavBarStatus()
    }

}