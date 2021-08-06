package com.example.myuni.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myuni.model.Comment
import com.example.myuni.model.Contacts
import com.example.myuni.model.Posting
import com.example.myuni.utils.EncodeUtils
import com.example.myuni.utils.OrderUtils
import com.example.myuni.utils.TimeUtils
import com.google.firebase.database.*
import java.sql.Time

class PostingViewModel: ViewModel(){
    private lateinit var dbRef: DatabaseReference
    private var database = FirebaseDatabase.getInstance()
    private val _postingsList = MutableLiveData<ArrayList<Posting>>().apply { value = object : ArrayList<Posting>(){} }
    val postingsList: MutableLiveData<ArrayList<Posting>> = _postingsList
    private val _commentsList = MutableLiveData<ArrayList<Comment>>().apply { value = object : ArrayList<Comment>(){} }
    val commentsList: MutableLiveData<ArrayList<Comment>> = _commentsList

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
                    for (key in value!!.keys){
                        val p: Posting? = value[key]
                        _postingsList.value = _postingsList.value?.plus(p) as ArrayList<Posting>
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
}