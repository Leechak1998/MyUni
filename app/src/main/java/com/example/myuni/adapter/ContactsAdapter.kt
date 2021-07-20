package com.example.myuni.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.myuni.BR
import com.example.myuni.model.Contacts

class ContactsAdapter(_context: Context, _resource: Int, _data: List<Contacts>) : BaseAdapter() {
    private val context: Context = _context
    private val resourceId: Int = _resource
    private val data: List<Contacts> = _data

//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var view: View
//        val viewHolder: ViewHolder
//
//        if (convertView == null) {
//            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
//            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
//
//            val contactsImage:ImageView=view.findViewById(R.id.iv_profile_pic)//绑定布局得图片
//            val contactsName:TextView=view.findViewById(R.id.tv_name)//绑定布局中得名字
//            viewHolder=ViewHolder(contactsImage,contactsName)
//
//            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
//            view.tag = viewHolder
//        } else {
//            view = convertView
//            viewHolder = view.tag as ViewHolder
//        }
//
//        val con=getItem(position)//获取当前项得contacts实例
//        if (con!=null){
//            viewHolder.contactsImage.setImageResource(con.imageId)
//            viewHolder.contactsName.text=con.name
//        }
//
//        return view
//    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val con=getItem(position)
        var binding: ViewDataBinding?
        if (convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),resourceId,parent,false)
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

   // inner class ViewHolder(val contactsImage:ImageView,val contactsName:TextView)

}