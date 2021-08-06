package com.example.myuni.adapter

import androidx.databinding.ViewDataBinding
import com.example.myuni.BR
import com.example.myuni.R
import com.example.myuni.model.Comment

class CommentAdapter(data: List<Comment>) : com.example.myuni.adapter.BaseAdapter<Comment>(data) {
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        var binding : ViewDataBinding = holder.dataBinding
        binding.setVariable(BR.comment,data.get(position))
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.comment_item
    }

}