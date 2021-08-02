package com.example.myuni.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Goods
import com.example.myuni.utils.EncodeUtils
import com.example.myuni.utils.OrderUtils
import com.google.firebase.database.*

class GoodsViewModel : ViewModel() {

    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()
    private val _goodsList = MutableLiveData<ArrayList<Goods>>().apply { value = object : ArrayList<Goods>(){} }
    val goodsList: MutableLiveData<ArrayList<Goods>> = _goodsList
    private val _buyingList = MutableLiveData<ArrayList<Goods>>().apply { value = object : ArrayList<Goods>(){} }
    val buyingList: MutableLiveData<ArrayList<Goods>> = _buyingList
    private val _sellingList = MutableLiveData<ArrayList<Goods>>().apply { value = object : ArrayList<Goods>(){} }
    val sellingList: MutableLiveData<ArrayList<Goods>> = _sellingList

    fun initGoodsList(){
        dbRef = database.getReference("goodsList").child("sellingList")

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

    //Add item to selling list
    fun addGoods(newGoods: Goods, userEmail: String){
        dbRef = database.getReference("goodsList").child("sellingList")
        _goodsList.value = _goodsList.value?.plus(newGoods) as ArrayList<Goods>
        var map: HashMap<String, Any> = HashMap()
        map[newGoods.orderNum] = newGoods
        dbRef.updateChildren(map)
    }

    fun purchaseGoods(pGoods: Goods, userEmail: String){
        //Add item to buying list
        dbRef = database.getReference("goodsList").child("buyingList").child(EncodeUtils.EncodeString(userEmail))
        _buyingList.value = _buyingList.value?.plus(pGoods) as ArrayList<Goods>
        var map: HashMap<String, Any> = HashMap()
        map[pGoods.orderNum] = pGoods
        dbRef.updateChildren(map)

        //Delete item from sellingList
        dbRef = database.getReference("goodsList").child("sellingList").child(pGoods.orderNum)
        dbRef.removeValue()
    }

    fun getSellingList(currentUser: String){
        dbRef = database.getReference("goodsList").child("sellingList")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val t = object : GenericTypeIndicator<HashMap<String, Goods>>() {}
                    var value = snapshot.getValue(t)

                    _sellingList.value?.clear()

                    for (key in value!!.keys){
                        val g: Goods? = value[key]

                        if (currentUser == g!!.owner)
                            _sellingList.value = _sellingList.value?.plus(g) as ArrayList<Goods>
                        else
                            println("current: ${currentUser}  == owner: ${g!!.owner}")
                    }
                }
            }
        })
    }

    fun getBuyingList(currentUser: String){
        dbRef = database.getReference("goodsList").child("buyingList").child(EncodeUtils.EncodeString(currentUser))

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val t = object : GenericTypeIndicator<HashMap<String, Goods>>() {}
                    var value = snapshot.getValue(t)

                    _buyingList.value?.clear()

                    for (key in value!!.keys){
                        val g: Goods? = value[key]
                        _buyingList.value = _buyingList.value?.plus(g) as ArrayList<Goods>
                    }
                }
            }

        })
    }

}