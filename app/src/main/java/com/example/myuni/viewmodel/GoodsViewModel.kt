package com.example.myuni.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Goods
import com.example.myuni.utils.EncodeUtils
import com.example.myuni.utils.Uni
import com.google.firebase.database.*

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
    var optionDis = ""

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
                    _initList.value?.clear()

                    for (key in value!!.keys){
                        val g: Goods? = value[key]
                        _goodsList.value = _goodsList.value?.plus(g) as ArrayList<Goods>
                        _initList.value = _initList.value?.plus(g) as ArrayList<Goods>
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
        println("已经选择的选项：--$optionUni-- , --$optionFri-- , --$optionDis-- , --$optionFris--")
        initGList()
        //filter university option
        var tempList1 = ArrayList<Goods>()
        var switch1 = false
        if (optionUni != ""){
            if (optionUni != "All Universities") {
                val suffix = Uni.uniMap[optionUni]
                val regex = Regex(suffix!!)
                for (i in _goodsList.value!!.indices) {
                    if (regex.containsMatchIn(_goodsList.value!![i].owner)) {
                        tempList1.add(_goodsList.value!![i])
                        switch1 = true
                    }
                }
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
            if (switch2) {
                switchList(tempList2)
            }
        }

        //filter distance option

        return listOf(switch1, switch2)
    }

    fun initGList(){
        _goodsList.value!!.clear()
        for (i in _initList.value!!.indices){
            _goodsList.value = _goodsList.value?.plus(_initList.value!![i]) as ArrayList<Goods>
        }
    }

    private fun switchList(tempList: ArrayList<Goods>){
        _goodsList.value!!.clear()
        for (i in tempList.indices) {
            _goodsList.value = _goodsList.value?.plus(tempList[i]) as ArrayList<Goods>
        }
    }

}