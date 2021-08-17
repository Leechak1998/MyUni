package com.example.myuni.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.myuni.R
import com.example.myuni.databinding.FragmentNewsBinding
import com.example.myuni.databinding.FragmentNewsDetailsBinding
import com.example.myuni.model.Goods
import com.example.myuni.utils.Uni

class NewsDetailsFragment : Fragment() {
    private lateinit var binding: FragmentNewsDetailsBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_details, container, false)

        var bundle = requireArguments()
        val position = bundle.getInt("position")

        binding.wv.settings.javaScriptEnabled = true
        binding.wv.loadUrl(Uni.uniNewsUrls[position])

        return binding.root
    }
}