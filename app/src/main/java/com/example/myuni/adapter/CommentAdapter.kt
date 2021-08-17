package com.example.myuni.adapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.example.myuni.BR
import com.example.myuni.R
import com.example.myuni.model.Comment

class CommentAdapter(data: List<Comment>) : com.example.myuni.adapter.BaseAdapter<Comment>(data) {
    private var itemClickListener: ItemClickListener? = null
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        var binding : ViewDataBinding = holder.dataBinding
        binding.setVariable(BR.comment, data[position])
        holder.itemView.setOnClickListener {
            itemClickListener!!.onItemClickListener(binding.root, data[position])
        }

    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.comment_item
    }

    fun setOnItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClickListener(v: View, comment: Comment)
    }


}