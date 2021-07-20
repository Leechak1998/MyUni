package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myuni.R
import com.example.myuni.adapter.MessageAdapter
import com.example.myuni.databinding.FragmentChatBinding
import com.example.myuni.model.Connect
import com.example.myuni.model.Message
import com.example.myuni.utils.ObserverManager
import com.example.myuni.viewmodel.ChatViewModel

class ChatFragment : Fragment() {
//    lateinit var root: View
    private lateinit var binding: FragmentChatBinding
    private val msgList = ArrayList<Message>()
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var adapter: MessageAdapter
    private val connect = Connect()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        root = inflater.inflate(R.layout.fragment_chat, container, false)
        binding = DataBindingUtil.inflate<FragmentChatBinding>(inflater, R.layout.fragment_chat, container, false)

        chatViewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)
        chatViewModel.switchNavBarStatus()

        chatViewModel.messageList.observe(viewLifecycleOwner, Observer {
            for (i in 0..it.size-1){
                println("-- " + it[i].content)
            }
            msgList.clear()
            msgList.addAll(it)
            adapter.notifyDataSetChanged()
            binding.lvChat.setSelection(it.size)
            binding.etInput.setText("")
        })

        adapter = MessageAdapter(requireContext(), R.layout.message_item, msgList)
        binding.messageAdapter = adapter
        binding.sendMes = this
//        adapter = MessageAdapter(requireContext(), R.layout.message_item, msgList)
//        binding.messageAdapter = adapter
//        binding.sendMes = this

        //set sender and receiver
        connect.setName("jackson")

        Thread{
            connect.talk()
            chatViewModel.receiveMessage(connect)
        }.start()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        chatViewModel.switchNavBarStatus()
    }

    fun onSubmit(){
        val content = binding.etInput.text.toString()
        if (content.isNotEmpty()){
            chatViewModel.sendMessage(content)
            Thread{
                connect.send(content)
            }.start()
        }
    }


//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ChatFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//                ChatFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
}