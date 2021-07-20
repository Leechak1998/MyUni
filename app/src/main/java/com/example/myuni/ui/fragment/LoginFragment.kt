package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myuni.R
import com.example.myuni.utils.HttpConnection
import com.google.firebase.database.FirebaseDatabase

class LoginFragment : Fragment() {
    private lateinit var root: View
    private var params: HashMap<String, String> = HashMap<String, String>()
    private var firebasedatabse =  FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_login, container, false)
        val btn_login: Button = root.findViewById(R.id.btn_login)
        val tv_response: TextView = root.findViewById(R.id.tv_response)
        initData()

        test()

        btn_login.setOnClickListener {
            Thread{
                val httpConnection = HttpConnection()
                val response = httpConnection.doPost("https://us-central1-my-uni-project-fc56b.cloudfunctions.net/addUser", params)
                println("======$response")
            }.start()

        }

        return root
    }

    fun initData(){
        params.put("id", "zz1z20@soton.ac.uk")
        params.put("name", "testUser")
    }

    fun test(){
        var databaseFireReference = firebasedatabse.getReference("usersList")
        println("--------$databaseFireReference-----")
    }
}