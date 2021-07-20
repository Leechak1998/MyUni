package com.example.myuni.ui.activity

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myuni.R
import com.example.myuni.viewmodel.ChatViewModel
import com.example.myuni.viewmodel.ContactsViewModel

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_contacts,
            R.id.navigation_me
           // R.id.navigation_chat
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        chatViewModel.isHide.observe(this, Observer { it ->
            if (it){
                navView.visibility = View.INVISIBLE
//                println("Invisible")
            } else {
                navView.visibility = View.VISIBLE
//                println("Visible")
            }
        })

    }
}