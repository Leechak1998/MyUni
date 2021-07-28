package com.example.myuni.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Goods
import com.example.myuni.utils.Encode
import com.google.firebase.database.*

class GoodsViewModel : ViewModel() {

    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()

    private val _goodsList = MutableLiveData<ArrayList<Goods>>().apply {
        value = object : ArrayList<Goods>(){}
    }

    val goodsList: MutableLiveData<ArrayList<Goods>> = _goodsList

    fun initGoodsList(email: String){
        dbRef = database.getReference("goodsList").child(Encode.EncodeString(email))//之后动态获取用户的邮箱

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val t = object : GenericTypeIndicator<HashMap<String, Goods>>() {}
                    var value = snapshot.getValue(t)

                    _goodsList.value?.clear()

                    for (key in value!!.keys){
                        val c: Goods? = value[key]
                        _goodsList.value = _goodsList.value?.plus(c) as ArrayList<Goods>
                    }
                }
            }


        })
    }

    fun addGoods(newGoods: Goods, userEmail: String){
        dbRef = database.getReference("goodsList").child(Encode.EncodeString(userEmail))
        var map: HashMap<String, Any> = HashMap<String, Any>()

        val c = Goods(newGoods.name, newGoods.price, newGoods.description, newGoods.image1, newGoods.image2)
        _goodsList.value = _goodsList.value?.plus(c) as ArrayList<Goods>

        map[Encode.EncodeString(newGoods.name!!)] = c
        dbRef.setValue(map)
    }

}