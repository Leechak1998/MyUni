package com.example.myuni.ui.fragment

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SimpleAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myuni.R
import com.example.myuni.adapter.CommentAdapter
import com.example.myuni.databinding.FragmentNewsBinding
import com.example.myuni.utils.Uni
import android.webkit.WebViewClient as WebViewClient

class NewsFragment : Fragment(), AdapterView.OnItemClickListener {
    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)
//        initViewModel()
//        init()

        binding.lvNewsList.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, Uni.uniNews)
        binding.lvNewsList.setOnItemClickListener(this)
        return binding.root
    }

    override fun onItemClick(p0: AdapterView<*>?, v: View?, position: Int, p3: Long) {
        val bundle = Bundle()
        bundle.putInt("position", position)
        Navigation.findNavController(v!!).navigate(R.id.navigation_newsDetails,bundle)
    }

//    private fun initViewModel() {
//
//    }
//
//    private fun init() {
//        binding.lvNewsList.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, Uni.uniNews)
//        binding.lvNewsList.setOnClickListener(this)
//
//    }

}