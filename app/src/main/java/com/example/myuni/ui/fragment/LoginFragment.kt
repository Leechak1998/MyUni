package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myuni.R
import com.example.myuni.model.Message
import com.example.myuni.utils.HttpConnection
import com.google.firebase.database.*


class LoginFragment : Fragment() {
    private lateinit var root: View
    private var params: HashMap<String, String> = HashMap<String, String>()
    private lateinit var dbRef: DatabaseReference

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
            }

//            var map1: HashMap<String, Any> = HashMap<String, Any>()
//            val data1 = "{ \"name\" = \"Liven\" & \"age\" = \"23\"}"
//            map1.put("1", data1)
//            dbRef.updateChildren(map1)

            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    println("DATA CHANGE!!!!!!")
                    val value = dataSnapshot.value as ArrayList<String>?

                    for (i in 0..value!!.size-1){
                        println("---($i)---" + value[i])
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("NOTHING CHANGE!!!!!!")
                }
            })


            println("click button")
        }

        return root
    }

    fun initData(){
        params.put("id", "zz1z20@soton.ac.uk")
        params.put("name", "testUser")
    }

    fun test(){

        var database = FirebaseDatabase.getInstance()
        dbRef = database.getReference("conversationList").child("messagesId1")
//        ArrayList<SessionBean> list= new ArrayList<>();
//        SessionBean sessionBean = new SessionBean("hellow "+messageid+"!", "", "", "ake", System.currentTimeMillis() + "");
//        SessionBean sessionBean1 = new SessionBean("hellow ake!", "", "", messageid, System.currentTimeMillis() + "");
//        list.add(sessionBean);
//        list.add(sessionBean1);
//        reference.setValue(sessionBean);

        val message = Message("test", Message.TYPE_RECEIVED)
//        var map: HashMap<String, Any> = HashMap<String, Any>()
//        map.put("1", message)
//        dbRef.updateChildren(map)

        var map: HashMap<String, Any> = HashMap<String, Any>()
        val data = "{ \"name\" = \"jackson1\" & \"age\" = \"23\"}"
        val data1 = "{ \"name\" = \"Liven1\" & \"age\" = \"23\"}"
        map.put("0", data)
        map.put("1", data1)
        dbRef.setValue(map)

        //dbRef.updateChildren(map)

//        dbRef.setValue("this is text")


    }


}