package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myuni.R
import com.example.myuni.utils.ObserverListener
import com.example.myuni.utils.ObserverManager
import com.example.myuni.viewmodel.MeViewModel

class MeFragment : Fragment(), ObserverListener {

    private lateinit var meViewModel: MeViewModel
    lateinit var textView: TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        meViewModel = ViewModelProvider(this).get(MeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_me, container, false)
        textView = root.findViewById(R.id.text_notifications)

        meViewModel.text.observe(viewLifecycleOwner, Observer {
            println("it.value = ${it}")
            textView.text = it
        })

        ObserverManager.instance.add(this)

        return root
    }

    override fun observerUpData(count: String) {
        meViewModel.changeData(count)
        println("----$count----")
    }
}