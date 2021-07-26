package com.example.myuni.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myuni.R
import com.example.myuni.model.Contacts
import com.example.myuni.utils.encode
import com.google.firebase.database.*


class ContactsViewModel : ViewModel() {
    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()

    private val _contactsList = MutableLiveData<ArrayList<Contacts>>().apply {
        var c: ArrayList<Contacts> = object : ArrayList<Contacts>(){
            init {
                val c = Contacts("test", R.drawable.profile_default, "test@soton.ac.uk","")
                add(c)
            }
        }
        value = c
    }


    val contactsList: MutableLiveData<ArrayList<Contacts>> = _contactsList

    fun initContactsList(email: String){
        dbRef = database.getReference("contactsList").child(email)//之后动态获取用户的邮箱

//        //改进：从数据库中获取
//        var map: HashMap<String, Any> = HashMap<String, Any>()
//        val c1 = Contacts("jackson", R.drawable.profile_default, "rl1r20@soton.ac.uk","")
//        val c2 = Contacts("ellen", R.drawable.profile_default, "yh12n20@soton.ac.uk","")
//
//        map[encode.EncodeString(c1.email!!)] = c1
//        map[encode.EncodeString(c2.email!!)] = c2
//        dbRef.setValue(map)

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    println("DATA CHANGE!!!!!!")
                    val t = object : GenericTypeIndicator<HashMap<String, Contacts>>() {}
                    var value = snapshot.getValue(t)

                    _contactsList.value?.clear()

                    for (key in value!!.keys){
                        println("---$key------${value[key]}")
                        val c: Contacts? = value[key]
                        _contactsList.value = _contactsList.value!!.plus(c) as ArrayList<Contacts>
                    }
                }
            }


        })

    }

    //改进：增加前先判断是否存在该用户。
    fun addContact(/*contacts: Contacts*/){
        var map: HashMap<String, Any> = HashMap<String, Any>()
        val c = Contacts("test2", R.drawable.profile_default, "test2@soton.ac.uk","")
        _contactsList.value = _contactsList.value?.plus(c) as ArrayList<Contacts>
        map["test2"] = c
        dbRef.updateChildren(map)

    }

}