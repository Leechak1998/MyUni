package com.example.myuni.adapter

import androidx.databinding.ViewDataBinding
import com.example.myuni.BR
import com.example.myuni.R
import com.example.myuni.model.Community
import com.example.myuni.model.Posting

class CommunityAdapter(data: List<Community>) : com.example.myuni.adapter.BaseAdapter<Community>(data) {
    private var recyclerViewItemClickListener: RecyclerViewItemClickListener? = null

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        var binding : ViewDataBinding = holder.dataBinding
        binding.setVariable(BR.community,data.get(position))
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.community_item
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        holder.itemView.setOnClickListener {
            recyclerViewItemClickListener!!.onItemClickListener( position )
        }
    }

    fun setOnRecyclerViewItemClickListener(recyclerViewItemClickListener: RecyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener
    }

    interface RecyclerViewItemClickListener {
        fun onItemClickListener(position: Int)
    }
}