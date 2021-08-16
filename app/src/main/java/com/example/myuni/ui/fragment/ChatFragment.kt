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
import com.example.myuni.model.Contacts
import com.example.myuni.model.Message
import com.example.myuni.viewmodel.ChatViewModel
import com.example.myuni.viewmodel.CommunityViewModel
import com.example.myuni.viewmodel.MeViewModel
import com.example.myuni.viewmodel.UtilViewModel
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val msgList = ArrayList<Message>()
    private val userList = ArrayList<Contacts>()
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var utilViewModel: UtilViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var communityViewModel: CommunityViewModel
    private lateinit var adapter: MessageAdapter
    private lateinit var receiver: Contacts
    private lateinit var sender: Contacts
    private var chatType: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println("create聊天页面")
        binding = DataBindingUtil.inflate<FragmentChatBinding>(inflater, R.layout.fragment_chat, container, false)

        initViewModel()
        init()



        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSubmit(){
        val content = binding.etInput.text.toString()
        if (chatType == "private"){
            if (content.isNotEmpty()){
                chatViewModel.sendMessage(content)
            }
        }else if (chatType == "group"){
            if (content.isNotEmpty()){
                chatViewModel.sendGroupMessage(content, userList)//内容，群聊用户
            }
        }
        binding.etInput.text.clear()
    }

    private fun initViewModel(){
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
        communityViewModel = ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
        chatViewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){

        var bundle = requireArguments()
        receiver = bundle.getSerializable("receiver") as Contacts

        println("----email:${receiver.email!!}")
        val email = receiver.email!!.toString()
        val regex = Regex("@")
        chatType = if (regex.containsMatchIn(email))
            "private"
        else
            "group"

        println("----type:$chatType")
        utilViewModel.switchNavBarStatus()
        sender = meViewModel.getLoginUser()!!

        chatViewModel.initChatList(sender, receiver)
        adapter = MessageAdapter(requireContext(), R.layout.message_item, msgList)
        binding.messageAdapter = adapter
        binding.sendMes = this

        //set observer
        chatViewModel._messageList.observe(viewLifecycleOwner, Observer {
            msgList.clear()
            msgList.addAll(it)
            adapter.notifyDataSetChanged()
            binding.lvChat.setSelection(it.size)
        })

        //set group chat users list observer and get users list

        if (chatType == "group"){
            communityViewModel.getCommunityUsersList(receiver.email!!)
            communityViewModel.communityUserList.observe(viewLifecycleOwner, Observer {
                userList.clear()
                userList.addAll(it)
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        utilViewModel.switchNavBarStatus()
    }

}