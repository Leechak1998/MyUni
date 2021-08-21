package com.example.myuni.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Goods
import com.example.myuni.utils.EncodeUtils
import com.example.myuni.utils.TimeUtils
import com.example.myuni.utils.Uni
import com.google.firebase.database.*
import java.time.LocalDateTime

class GoodsViewModel : ViewModel() {

    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()
    private val _initList = MutableLiveData<ArrayList<Goods>>().apply { value = object : ArrayList<Goods>(){} }
    private val _goodsList = MutableLiveData<ArrayList<Goods>>().apply { value = object : ArrayList<Goods>(){} }
    val goodsList: MutableLiveData<ArrayList<Goods>> = _goodsList
    private val _buyingList = MutableLiveData<ArrayList<Goods>>().apply { value = object : ArrayList<Goods>(){} }
    val buyingList: MutableLiveData<ArrayList<Goods>> = _buyingList
    private val _sellingList = MutableLiveData<ArrayList<Goods>>().apply { value = object : ArrayList<Goods>(){} }
    val sellingList: MutableLiveData<ArrayList<Goods>> = _sellingList
    var optionUni = ""
    var optionFri = ""
    var optionFris = ArrayList<String>()
    var optionNat = ""

    fun initGoodsList(){
        dbRef = database.getReference("goodsList").child("sellingList")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val t = object : GenericTypeIndicator<HashMap<String, Goods>>() {}
                    var value = snapshot.getValue(t)

                    _goodsList.value?.clear()
                    _initList.value?.clear()

                    for (key in value!!.keys){
                        if (value[key]!!.status != Goods.DELIVERY){
                            val g: Goods? = value[key]
                            _goodsList.value = _goodsList.value?.plus(g) as ArrayList<Goods>
                            _initList.value = _initList.value?.plus(g) as ArrayList<Goods>
                        }
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

    fun purchaseGoods(pGoods: Goods){
        //change goods' status
        pGoods.status = Goods.PENDING

        //update item from selling
        dbRef = database.getReference("goodsList").child("buyingList").child(EncodeUtils.EncodeString(pGoods.buyerEmail!!))
       // _buyingList.value = _buyingList.value?.plus(pGoods) as ArrayList<Goods>
        var map: HashMap<String, Any> = HashMap()
        map[pGoods.orderNum] = pGoods
        dbRef.updateChildren(map)

        //update item from selling List
        dbRef = database.getReference("goodsList").child("sellingList")
        var map1: HashMap<String, Any> = HashMap()
        map1[pGoods.orderNum] = pGoods
        dbRef.updateChildren(map1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun confirmGoods(pGoods: Goods){
        //change goods' status and update trading time
        pGoods.status = Goods.DELIVERY
        pGoods.tradingTime = TimeUtils.getCurrentTime(LocalDateTime.now())

        //update item from buying list
        dbRef = database.getReference("goodsList").child("buyingList").child(EncodeUtils.EncodeString(pGoods.buyerEmail!!))
        // _buyingList.value = _buyingList.value?.plus(pGoods) as ArrayList<Goods>
        var map: HashMap<String, Any> = HashMap()
        map[pGoods.orderNum] = pGoods
        dbRef.updateChildren(map)

        //update item from selling List
        dbRef = database.getReference("goodsList").child("sellingList")
        var map1: HashMap<String, Any> = HashMap()
        map1[pGoods.orderNum] = pGoods
        dbRef.updateChildren(map1)
    }

    fun cancelOrder(orderNumber: String, good: Goods) {
        //remove order from selling list
        database.getReference("goodsList").child("buyingList").child(orderNumber).removeValue()

        //remove buyer information of product
        good.buyerName = ""
        good.buyerEmail = ""
        good.finalPrice = ""
        good.method = ""
        good.address = ""
        good.status = Goods.SELLING

        //update item from buying list
        dbRef = database.getReference("goodsList").child("sellingList").child(EncodeUtils.EncodeString(good.buyerEmail!!))
        var map: HashMap<String, Any> = HashMap()
        map[good.orderNum] = good
        dbRef.updateChildren(map)
    }

    fun getSellingList(currentUser: String){
        _sellingList.value = ArrayList()
        dbRef = database.getReference("goodsList").child("sellingList")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
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
        _buyingList.value = ArrayList()
        dbRef = database.getReference("goodsList").child("buyingList").child(EncodeUtils.EncodeString(currentUser))
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}

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

    fun searchGoods(content: String): Boolean{
        val regex = Regex(content, RegexOption.IGNORE_CASE)
        var tempList = ArrayList<Goods>()
        var switch = false
        for (i in _goodsList.value!!.indices) {
            if (regex.containsMatchIn(_goodsList.value!![i].name.toString())) {
                tempList.add(_goodsList.value!![i])
                switch = true
            }
        }
        if (switch)
            switchList(tempList)
        return switch
    }

    fun selectedByOptions(): List<Boolean>{
        println("已经选择的选项：--$optionUni-- , --$optionFri-- , --$optionNat-- , --$optionFris--")
        initGList()
        //filter university option
        var tempList1 = ArrayList<Goods>()
        var switch1 = false
        if (optionUni != ""){
            if (optionUni != "All Universities") {
                val suffix = Uni.uniNameMap[optionUni]
                val regex = Regex(suffix!!)
                for (i in _goodsList.value!!.indices) {
                    if (regex.containsMatchIn(_goodsList.value!![i].owner)) {
                        tempList1.add(_goodsList.value!![i])
                        switch1 = true
                    }
                }
                _goodsList.value = ArrayList()
                if (switch1) {
                    switchList(tempList1)
                }
            }else
                switch1 = true
        }

        //filter friends option
        var tempList2 = ArrayList<Goods>()
        var switch2 = false
        if (optionFri != ""){
            for (i in _goodsList.value!!.indices){
                if (optionFri == _goodsList.value!![i].owner){
                    tempList2.add(_goodsList.value!![i])
                    switch2 = true
                }
            }
            _goodsList.value = ArrayList()
            if (switch2) {
                switchList(tempList2)
            }
        } else if (optionFris.isNotEmpty()){
            for (i in _goodsList.value!!.indices){
                for (j in optionFris.indices){
                    if (_goodsList.value!![i].owner == optionFris[j]){
                        tempList2.add(_goodsList.value!![i])
                        switch2 = true
                        break
                    }
                }
            }
            _goodsList.value = ArrayList()
            if (switch2) {
                switchList(tempList2)
            }
        }

        //filter nationality option
        var tempList3 = ArrayList<Goods>()
        var switch3 = false
        if (optionNat != ""){
            for (i in _goodsList.value!!.indices){
                if (optionNat == _goodsList.value!![i].nation){
                    tempList3.add(_goodsList.value!![i])
                    switch3 = true
                }
            }
            _goodsList.value = ArrayList()
            if (switch3) {
                switchList(tempList3)
            }
        }

        return listOf(switch1, switch2, switch3)
    }

    fun initGList(){
        _goodsList.value!!.clear()
        for (i in _initList.value!!.indices){
            _goodsList.value = _goodsList.value?.plus(_initList.value!![i]) as ArrayList<Goods>
        }
    }

    private fun switchList(tempList: ArrayList<Goods>){
//        _goodsList.value!!.clear()
        for (i in tempList.indices) {
            _goodsList.value = _goodsList.value?.plus(tempList[i]) as ArrayList<Goods>
        }
    }

}