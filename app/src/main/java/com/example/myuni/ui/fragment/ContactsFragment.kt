package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.core.view.get
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
import com.example.myuni.viewmodel.MeViewModel
import android.app.ActionBar as ActionBar

class ContactsFragment : Fragment(), AdapterView.OnItemClickListener {
    private lateinit var binding: FragmentContactsBinding
    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var meViewModel: MeViewModel
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
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_contacts_to_navigation_addUser)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun initViewModel() {
        contactsViewModel = ViewModelProvider(requireActivity()).get(ContactsViewModel::class.java)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
    }

    private fun init() {
        contactsViewModel.initContactsList(meViewModel.getLoginUser()!!.email!!)
        contactsViewModel.contactsList.observe(viewLifecycleOwner, Observer { it ->
            contactsList.clear()
            contactsList.addAll(it)
            adapter.notifyDataSetChanged()
            binding.lvContacts.setSelection(it.size)
        })

        adapter = ContactsAdapter(requireContext(), R.layout.contacts_item, contactsList);
        binding.adapter = adapter

        //set listener
        binding.lvContacts.onItemClickListener = this
    }

    override fun onItemClick(p0: AdapterView<*>?, v: View?, position: Int, p3: Long) {
        val contacts = contactsList[position]
        val bundle = Bundle().also {
            it.putSerializable("receiver", contacts)
        }
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_contacts_to_navigation_chat, bundle)
    }

}