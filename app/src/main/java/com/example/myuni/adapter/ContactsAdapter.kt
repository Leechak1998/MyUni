package com.example.myuni.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.myuni.BR
import com.example.myuni.model.Contacts

class ContactsAdapter(_context: Context, _resource: Int, _data: List<Contacts>) : BaseAdapter() {
    private val context: Context = _context
    private val resourceId: Int = _resource
    private val data: List<Contacts> = _data

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val con=getItem(position)
        var binding: ViewDataBinding?
        if (convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),resourceId, parent,false)
        } else {
            binding = DataBindingUtil.getBinding(convertView)
        }
        //binding!!.setVariable(variableId,con)
        binding!!.setVariable(BR.contacts, con)

        return binding.root;
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

}