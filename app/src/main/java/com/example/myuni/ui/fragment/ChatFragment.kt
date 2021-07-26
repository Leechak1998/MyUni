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
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val msgList = ArrayList<Message>()
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var adapter: MessageAdapter
//    private val connect = Connect()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        root = inflater.inflate(R.layout.fragment_chat, container, false)
        binding = DataBindingUtil.inflate<FragmentChatBinding>(inflater, R.layout.fragment_chat, container, false)

        chatViewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)
        chatViewModel.switchNavBarStatus()
        chatViewModel.initChatList()

        chatViewModel.messageList.observe(viewLifecycleOwner, Observer {
            for (i in 0 until it.size){
                println("-- " + it[i].content + " -- " + it[i].time)
            }
            msgList.clear()
            msgList.addAll(it)
            adapter.notifyDataSetChanged()
            binding.lvChat.setSelection(it.size)
//            binding.etInput.setText("")
            binding.etInput.text.clear()
        })

        adapter = MessageAdapter(requireContext(), R.layout.message_item, msgList)
        binding.messageAdapter = adapter
        binding.sendMes = this

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        chatViewModel.switchNavBarStatus()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSubmit(){
        val content = binding.etInput.text.toString()
        if (content.isNotEmpty()){
            chatViewModel.sendMessage(content)
        }
    }


    fun transToString(time:Long):String{
        return SimpleDateFormat("YYYY-MM-DD-hh-mm-ss").format(time)
    }

}