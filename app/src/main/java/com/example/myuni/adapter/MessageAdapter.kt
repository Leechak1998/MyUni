package com.example.myuni.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.myuni.R
import com.example.myuni.model.Contacts
import com.example.myuni.model.Message
import com.example.myuni.utils.BitmapUtils

class MessageAdapter(context: Context, resource: Int, data: List<Message>) : ArrayAdapter<Message>(context, resource, data) {
    private val resourceId: Int = resource
    private val MesData = data
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)

            val leftLayout:LinearLayout = view.findViewById(R.id.left_layout)
            val rightLayout:LinearLayout = view.findViewById(R.id.right_layout)
            val leftMessage:TextView = view.findViewById(R.id.left_msg)
            val rightMessage:TextView = view.findViewById(R.id.right_msg)
            val leftProfile:ImageView = view.findViewById(R.id.head_left)
            val rightProfile:ImageView = view.findViewById(R.id.head_right)


            viewHolder=ViewHolder(leftLayout, rightLayout, leftMessage, rightMessage, leftProfile, rightProfile)

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val message = getItem(position)//获取当前项得contacts实例

        if ( message!!.type == Message.TYPE_RECEIVED ){
            viewHolder.leftProfile.setImageBitmap(BitmapUtils.convertStringToIcon(MesData[position].profile))
            viewHolder.leftLayout.visibility = View.VISIBLE
            viewHolder.leftProfile.visibility = View.VISIBLE
            viewHolder.rightLayout.visibility = View.GONE
            viewHolder.rightProfile.visibility = View.GONE
            viewHolder.leftMessage.text = message.content
        } else if ( message!!.type == Message.TYPE_SEND ){
            viewHolder.rightProfile.setImageBitmap(BitmapUtils.convertStringToIcon(MesData[position].profile))
            viewHolder.rightLayout.visibility = View.VISIBLE
            viewHolder.rightProfile.visibility = View.VISIBLE
            viewHolder.leftLayout.visibility = View.GONE
            viewHolder.leftProfile.visibility = View.GONE
            viewHolder.rightMessage.text = message.content
        }
        return view
    }

    inner class ViewHolder(val leftLayout:LinearLayout, val rightLayout:LinearLayout, val leftMessage:TextView, val rightMessage:TextView, val leftProfile:ImageView, val rightProfile:ImageView)

}