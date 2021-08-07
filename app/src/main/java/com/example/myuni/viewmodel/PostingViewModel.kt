package com.example.myuni.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Comment
import com.example.myuni.model.Goods
import com.example.myuni.model.Posting
import com.google.firebase.database.*

class PostingViewModel: ViewModel(){
    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()
    private val _initList = MutableLiveData<ArrayList<Posting>>().apply { value = object : ArrayList<Posting>(){} }
    private val _postingsList = MutableLiveData<ArrayList<Posting>>().apply { value = object : ArrayList<Posting>(){} }
    val postingsList: MutableLiveData<ArrayList<Posting>> = _postingsList
    private val _commentsList = MutableLiveData<ArrayList<Comment>>().apply { value = object : ArrayList<Comment>(){} }
    val commentsList: MutableLiveData<ArrayList<Comment>> = _commentsList
    private val _myPostingsList = MutableLiveData<ArrayList<Posting>>().apply { value = object : ArrayList<Posting>(){} }
    val myPostingsList: MutableLiveData<ArrayList<Posting>> = _myPostingsList

    fun initPostingList(){
        _postingsList.value?.clear()
        dbRef = database.getReference("postingsList")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val t = object : GenericTypeIndicator<HashMap<String, Posting>>() {}
                    var value = snapshot.getValue(t)

                    _postingsList.value?.clear()
                    _initList.value?.clear()

                    for (key in value!!.keys){
                        val p: Posting? = value[key]
                        _postingsList.value = _postingsList.value?.plus(p) as ArrayList<Posting>
                        _initList.value = _initList.value?.plus(p) as ArrayList<Posting>
                    }
                }
            }

        })
    }

    fun publishPosting(newPosting: Posting){
        dbRef = database.getReference("postingsList")
        var map: HashMap<String, Any> = HashMap()
        _postingsList.value = _postingsList.value?.plus(newPosting) as ArrayList<Posting>
        map[newPosting.postingNum!!] = newPosting
        dbRef.updateChildren(map)
    }

    fun selectedPostingByCategory(category: String){
        initPList()
        when (category) {
            "All" -> {
            // return all postings
            }
            "Study" -> {
                filterList("Study")
            }
            "Life" -> {
                filterList("Life")
            }
            "Entertainment" -> {
                filterList("Entertainment")
            }
        }
    }

    private fun filterList(category: String){
        val tempList = ArrayList<Posting>()

        for (i in _postingsList.value!!.indices){
            if (category == _postingsList.value!![i].category)
                tempList.add(_postingsList.value!![i])
        }
        _postingsList.value = ArrayList()
        for (i in tempList.indices){
            _postingsList.value = _postingsList.value?.plus(tempList[i]) as ArrayList<Posting>
        }
    }

    fun searchPosting(content: String): Boolean{
        val regex = Regex(content, RegexOption.IGNORE_CASE)
        var tempList = ArrayList<Posting>()
        var switch = false
        for (i in _postingsList.value!!.indices) {
            if (regex.containsMatchIn(_postingsList.value!![i].tittle.toString())) {
                tempList.add(_postingsList.value!![i])
                switch = true
            }
        }
        _postingsList.value = ArrayList()
        if (switch) {
            for (i in tempList.indices) {
                _postingsList.value = _postingsList.value?.plus(tempList[i]) as ArrayList<Posting>
            }
        }
        return switch
    }

    fun initPList(){
        _postingsList.value!!.clear()
        for (i in _initList.value!!.indices){
            _postingsList.value = _postingsList.value?.plus(_initList.value!![i]) as ArrayList<Posting>
        }
    }

    fun initCommentsList(postingNum: String){
        _commentsList.value?.clear()
        dbRef = database.getReference("postingsList").child(postingNum).child("commentsList")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val t = object : GenericTypeIndicator<HashMap<String, Comment>>() {}
                    var value = snapshot.getValue(t)

                    _commentsList.value?.clear()
                    for (key in value!!.keys){
                        val c: Comment? = value[key]
                        _commentsList.value = _commentsList.value?.plus(c) as ArrayList<Comment>
                    }
                }
            }

        })
    }

    fun publishComment(posting: Posting, newComment: Comment){
        dbRef = database.getReference("postingsList").child(posting.postingNum!!).child("commentsList")
        var map: HashMap<String, Any> = HashMap()
        _commentsList.value = _commentsList.value?.plus(newComment) as ArrayList<Comment>
        map[newComment.time!!] = newComment
        dbRef.updateChildren(map)
    }

    fun getMyPostingsList(currentUser: String){
        _myPostingsList.value = ArrayList()
        dbRef = database.getReference("postingsList")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val t = object : GenericTypeIndicator<HashMap<String, Posting>>() {}
                    var value = snapshot.getValue(t)

                    for (key in value!!.keys){
                        val p: Posting? = value[key]
                        if (currentUser == p!!.publisherName)
                            _myPostingsList.value = _myPostingsList.value?.plus(p) as ArrayList<Posting>
                        else
                            println("========没有=======")
                    }
                }
            }

        })
    }
}