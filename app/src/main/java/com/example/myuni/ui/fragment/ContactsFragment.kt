package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.*
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
import android.app.ActionBar as ActionBar

class ContactsFragment : Fragment() {

    private lateinit var contactsViewModel: ContactsViewModel
    private var contactsList = ArrayList<Contacts>()
    private lateinit var adapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add("Add").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //添加新用户
//        val c = Contacts("jack", R.drawable.profile_default,"test@qq.com")
//        contactsViewModel.addContact()
        contactsViewModel.addContact()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentContactsBinding>(
            inflater,
            R.layout.fragment_contacts,
            container,
            false
        )

        contactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)

        contactsViewModel.contactsList.observe(viewLifecycleOwner, Observer { it ->
            contactsList.clear()
            contactsList.addAll(it)
            adapter.notifyDataSetChanged()
            binding.lvContacts.setSelection(it.size)
//            println("更新用户列表")
//            for (i in contactsList.indices){
//                println("------$i----${contactsList[i].name}")
//            }
        })

        adapter = ContactsAdapter(requireContext(), R.layout.contacts_item, contactsList);
        binding.adapter = adapter;

        contactsViewModel.initContactsList()

        binding.lvContacts.setOnItemClickListener { _,_ ,position,_ ->
            NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_contacts_to_navigation_chat)
        }

        return binding.root
    }
}