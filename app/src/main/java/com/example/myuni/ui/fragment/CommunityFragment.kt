package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myuni.R
import com.example.myuni.adapter.CommentAdapter
import com.example.myuni.adapter.CommunityAdapter
import com.example.myuni.databinding.FragmentCommunityBinding
import com.example.myuni.model.Community
import com.example.myuni.model.Contacts
import com.example.myuni.viewmodel.CommunityViewModel
import com.example.myuni.viewmodel.ContactsViewModel
import com.example.myuni.viewmodel.MeViewModel
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast

class CommunityFragment : Fragment(), CommunityAdapter.RecyclerViewItemClickListener {
    private lateinit var binding: FragmentCommunityBinding
    private lateinit var communityViewModel: CommunityViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var contactViewModel: ContactsViewModel
    private var communityList = ArrayList<Community>()
    private lateinit var communityAdapter: CommunityAdapter
    private lateinit var currentUser: Contacts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add("Add").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.navigation_add_community)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun initViewModel() {
        communityViewModel = ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactsViewModel::class.java)
    }

    private fun init() {
        currentUser =  meViewModel.getLoginUser()!!
        val manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.rvCommunity.layoutManager = manager
        communityAdapter = CommunityAdapter(communityList)
        binding.communityAdapter = communityAdapter
        binding.rvCommunity.isNestedScrollingEnabled = false

        communityViewModel.initCommunityList()

        communityViewModel.communityList.observe(viewLifecycleOwner, Observer {
            communityList.clear()
            communityList.addAll(it)
            communityAdapter.notifyDataSetChanged()
        })

        communityAdapter.setOnRecyclerViewItemClickListener(this)
    }

    override fun onItemClickListener(position: Int) {
        val c = communityList[position]
        val boolean = communityViewModel.isGroupMember(c.communityNum)

        alert(c.description, c.name) {
            positiveButton("Join!") {
                //check if the user is already in the community
                if (boolean){
                    alert("You are already in this community!", "Tips") {
                        positiveButton("Yes"){ }
                    }.show()
                }else{
                    //add community
                    contactViewModel.addGroup(c, currentUser)
                    communityViewModel.addCommunity(c, currentUser)
                    toast("Successfully!")
                }


            }
            negativeButton("No") { }
        }.show()
    }
}