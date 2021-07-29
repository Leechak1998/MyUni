package com.example.myuni.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.R
import com.example.myuni.model.Contacts
import com.example.myuni.utils.Encode
import com.google.firebase.database.*


class ContactsViewModel : ViewModel() {
    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()
    private val _contactsList = MutableLiveData<ArrayList<Contacts>>().apply { value = object : ArrayList<Contacts>(){} }
    val contactsList: MutableLiveData<ArrayList<Contacts>> = _contactsList
    private var _newUser = MutableLiveData<ArrayList<Contacts>>().apply { value = object : ArrayList<Contacts>(){} }
    val newUser = _newUser


    fun initContactsList(email: String){
        dbRef = database.getReference("contactsList").child(Encode.EncodeString(email))//之后动态获取用户的邮箱

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
                            val c = Contacts(value[key]!!.name as String, value[key]!!.imageId as String, value[key]!!.email as String, value[key]!!.password as String)
                            _newUser.value = _newUser.value?.plus(c) as ArrayList<Contacts>
                            break
                        }
                    }
                }
            }


        })

    }

    fun addContacts(userEmail: String){
        dbRef = database.getReference("contactsList").child(Encode.EncodeString(userEmail))
        var map: HashMap<String, Any> = HashMap<String, Any>()
        val c : Contacts = _newUser.value?.get(0)!!
        _newUser.value!!.clear()
        _contactsList.value = _contactsList.value?.plus(c) as ArrayList<Contacts>
        map[Encode.EncodeString(c.email!!)] = c
        dbRef.updateChildren(map)
    }

}