package com.example.myuni.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Community
import com.example.myuni.model.Contacts
import com.example.myuni.model.Goods
import com.example.myuni.model.Message
import com.example.myuni.utils.EncodeUtils
import com.example.myuni.utils.TimeUtils
import com.google.firebase.database.*
import java.time.LocalDateTime

class CommunityViewModel: ViewModel() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRefReceiver: DatabaseReference
    private var database = FirebaseDatabase.getInstance()
    val communityList = MutableLiveData<ArrayList<Community>>().apply {
        value = object : ArrayList<Community>(){}
    }
    val communityUserList = MutableLiveData<ArrayList<Contacts>>().apply {
        value = object : ArrayList<Contacts>(){}
    }

    fun initCommunityList(){

        dbRef = database.getReference("communityList")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val t = object : GenericTypeIndicator<HashMap<String, Community>>() {}
                    var value = dataSnapshot.getValue(t)
                    communityList.value!!.clear()
                    for (key in value!!.keys){
                        val c: Community? = value[key]
                        communityList.value = communityList.value?.plus(c) as ArrayList<Community>
                    }
                }
            }
        })
    }

    fun createCommunity(community: Community){
        dbRef = database.getReference("communityList")
        var map: HashMap<String, Any> = HashMap<String, Any>()
        map[community.communityNum] = community
        dbRef.updateChildren(map)
    }

    fun addCommunity(community: Community, currentUser: Contacts){
        dbRef = database.getReference("communityList").child(community.communityNum).child("userList")
        var map: HashMap<String, Any> = HashMap<String, Any>()
        map[EncodeUtils.EncodeString(currentUser.email!!)] = currentUser
        dbRef.updateChildren(map)
    }

    fun getCommunityUsersList(Number: String){
        database.getReference("communityList").child(Number).child("userList").get().addOnSuccessListener {
            val t = object : GenericTypeIndicator<HashMap<String, Contacts>>() {}
            var value = it.getValue(t)

            communityUserList.value?.clear()
            for (key in value!!.keys){
                communityUserList.value = communityUserList.value?.plus(value[key]!!) as ArrayList<Contacts>
//                println("===${value[key]}===")
//                println("===${value[key]!!.name}===")
            }

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }

    }
}