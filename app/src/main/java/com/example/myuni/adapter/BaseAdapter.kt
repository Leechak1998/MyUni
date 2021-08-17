package com.example.myuni.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(var data: List<T>) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent?.context), viewType, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun refreshData(newData: List<T>) {
        this.data = newData
        this.notifyDataSetChanged()
    }
}

open class BaseViewHolder(var dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {}