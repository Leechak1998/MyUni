package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.adapter.ContactsAdapter
import com.example.myuni.databinding.FragmentContactsBinding
import com.example.myuni.model.Contacts
import com.example.myuni.viewmodel.ContactsViewModel

class ContactsFragment : Fragment() {

    private lateinit var contactsViewModel: ContactsViewModel
    private var contactsList = ArrayList<Contacts>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentContactsBinding>(
            inflater,
            R.layout.fragment_contacts,
            container,
            false
        )

        contactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)

        //添加新用户
//        val c = Contacts("jack", R.drawable.profile_default,"test@qq.com")
//        contactsViewModel.addContact(c)

        contactsViewModel.contactsList.observe(viewLifecycleOwner, Observer { it ->
            contactsList.clear()
            contactsList.addAll(it)
        })

        val adapter = ContactsAdapter(requireContext(), R.layout.contacts_item, contactsList);
        binding.adapter = adapter;

        binding.lvContacts.setOnItemClickListener { _,_ ,position,_ ->
            NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_contacts_to_navigation_chat)
            contactsViewModel.connectServer(contactsList[position])
        }

        return binding.root
    }
}