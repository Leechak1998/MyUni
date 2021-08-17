package com.example.myuni.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.adapter.ContactsAdapter
import com.example.myuni.databinding.FragmentAddUserBinding
import com.example.myuni.databinding.FragmentChatBinding
import com.example.myuni.model.Contacts
import com.example.myuni.utils.Utils
import com.example.myuni.viewmodel.ContactsViewModel
import com.example.myuni.viewmodel.MeViewModel
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton

class AddUserFragment : Fragment(), AdapterView.OnItemClickListener {
    private lateinit var binding: FragmentAddUserBinding
    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var meViewModel: MeViewModel
    private var newUserList = ArrayList<Contacts>()
    private var newUser = Contacts()
    private lateinit var adapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_user, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun initViewModel() {
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
        contactsViewModel = ViewModelProvider(requireActivity()).get(ContactsViewModel::class.java)
    }

    private fun init() {
        binding.search = this

        contactsViewModel.newUser.observe(viewLifecycleOwner, Observer {
            newUserList.clear()
            newUserList.addAll(it)
            adapter.notifyDataSetChanged()
            binding.lvNewUser.setSelection(it.size)
            if (newUserList.size > 0)
                newUser = newUserList[0]
        })
        adapter = ContactsAdapter(requireContext(), R.layout.contacts_item, newUserList);
        binding.adapter = adapter

        //set listener
        binding.lvNewUser.setOnItemClickListener(this)
    }

    fun searchUser(){
        val email = binding.etInput.text.toString()
        contactsViewModel.searchContacts(email)
        Utils.closeKeyBoard(requireContext())
    }

    private fun checkUserList(newUser: Contacts): Boolean{
        val list = contactsViewModel.contactsList.value
        for (i in 0 until list!!.size){
            if (newUser.email == list[i].email){
                return true
            }
        }
        return false
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        alert("Do you want to add this contacts?","Add Contacts") {
            positiveButton("Yes") {
                if (checkUserList(newUser)){
                    alert("He/She is your friend already!", "Tips") {
                        positiveButton("Yes"){ }
                    }.show()
                }else{
                    contactsViewModel.addContacts(meViewModel.getLoginUser()?.email!!)
                    NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.action_navigation_addUser_to_navigation_contacts)
                }
            }
            negativeButton("No") { }
        }.show()
    }

}