package com.example.myuni.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Community
import com.example.myuni.model.Contacts
import com.example.myuni.utils.EncodeUtils
import com.google.firebase.database.*


class ContactsViewModel : ViewModel() {
    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()
    private val _contactsList = MutableLiveData<ArrayList<Contacts>>().apply { value = object : ArrayList<Contacts>(){} }
    val contactsList: MutableLiveData<ArrayList<Contacts>> = _contactsList
    private var _newUser = MutableLiveData<ArrayList<Contacts>>().apply { value = object : ArrayList<Contacts>(){} }
    val newUser = _newUser
//    var receiver = MutableLiveData<Contacts>()


    fun initContactsList(email: String){
        _contactsList.value?.clear()

        dbRef = database.getReference("contactsList").child(EncodeUtils.EncodeString(email))
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    println("Contacts Data change!")
                    val t = object : GenericTypeIndicator<HashMap<String, Contacts>>() {}
                    var value = snapshot.getValue(t)

                    _contactsList.value?.clear()
                    for (key in value!!.keys){
//                        println("---$key------${value[key]}")
                        val c: Contacts? = value[key]
                        if (value[key]!!.email != email)
                            _contactsList.value = _contactsList.value?.plus(c) as ArrayList<Contacts>
                    }
                }
            }


        })

    }

    //改进：增加前先判断是否存在该用户。
    fun searchContacts(email: String){
        dbRef = database.getReference("usersList")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val t = object : GenericTypeIndicator<HashMap<String, Contacts>>() {}
                    var value = snapshot.getValue(t)

                    for (key in value!!.keys){
                        if (email == value[key]!!.email){
                            _newUser.value?.clear()
                            val c = Contacts(value[key]!!.name as String, value[key]!!.imageId as String, value[key]!!.email as String, value[key]!!.password as String, value[key]!!.uni as String)
                            _newUser.value = _newUser.value?.plus(c) as ArrayList<Contacts>
                            break
                        }
                    }
                }
            }


        })

    }

    fun addContacts(userEmail: String){
        dbRef = database.getReference("contactsList").child(EncodeUtils.EncodeString(userEmail))
        var map: HashMap<String, Any> = HashMap<String, Any>()
        val c : Contacts = _newUser.value?.get(0)!!
        _newUser.value!!.clear()

        _contactsList.value = _contactsList.value?.plus(c) as ArrayList<Contacts>
        map[EncodeUtils.EncodeString(c.email!!)] = c
        dbRef.updateChildren(map)
    }

    fun addGroup(community: Community, currentUser: Contacts){
        dbRef = database.getReference("contactsList").child(EncodeUtils.EncodeString(currentUser.email!!))
        var map: HashMap<String, Any> = HashMap<String, Any>()
        //name: String?, imageId: String?, email: String?, password: String?, uni: String
        val c = Contacts()
        c.name = community.name
        c.imageId = community.profile
        c.email = community.communityNum
        map[community.communityNum] = c
        dbRef.updateChildren(map)
    }

//    fun getUser(email: String){
//        var contacts: Contacts
//        database.getReference("usersList").child(EncodeUtils.EncodeString(email)).get().addOnSuccessListener {
////            val t = object : GenericTypeIndicator<HashMap<String, Contacts>>() {}
////            var value = it.getValue(t)
//            receiver.value = it.value as Contacts
//        }.addOnFailureListener {
//
//        }
//    }

}