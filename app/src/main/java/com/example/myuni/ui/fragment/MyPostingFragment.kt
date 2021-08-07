package com.example.myuni.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myuni.R
import com.example.myuni.adapter.CommentAdapter
import com.example.myuni.adapter.PostingsAdapter
import com.example.myuni.databinding.FragmentMyPostingBinding
import com.example.myuni.model.Posting
import com.example.myuni.viewmodel.MeViewModel
import com.example.myuni.viewmodel.PostingViewModel

class MyPostingFragment : Fragment() {
    private lateinit var binding: FragmentMyPostingBinding
    private lateinit var meViewModel: MeViewModel
    private lateinit var postingViewModel: PostingViewModel
    private lateinit var currentUser: String
    private lateinit var postingsAdapter: PostingsAdapter
    private var myPostingsList = ArrayList<Posting>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_posting, container, false)

        initViewModel()
        init()

        return binding.root
    }

    private fun initViewModel() {
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
        postingViewModel = ViewModelProvider(requireActivity()).get(PostingViewModel::class.java)
    }

    private fun init() {
        currentUser = meViewModel.getLoginUser()?.email!!
        postingViewModel.getMyPostingsList(currentUser)

        postingViewModel.myPostingsList.observe(viewLifecycleOwner, Observer {
            myPostingsList.clear()
            myPostingsList.addAll(it)
            postingsAdapter.notifyDataSetChanged()
        })

        //init recyclerview
        val manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.rvMyPostings.layoutManager = manager
        postingsAdapter = PostingsAdapter(myPostingsList)
        binding.rvMyPostings.adapter = postingsAdapter
        binding.postingsAdapter = postingsAdapter
        binding.rvMyPostings.isNestedScrollingEnabled = false

        //set click listener
        postingsAdapter.setOnRecyclerViewItemClickListener(object : PostingsAdapter.RecyclerViewItemClickListener{
            override fun onItemClickListener(position: Int) {
                val selectedPosting  = myPostingsList[position]
                val bundle = Bundle()
                bundle.putSerializable("posting", selectedPosting)
                Navigation.findNavController(view!!).navigate(R.id.navigation_postingDetails, bundle)
            }

        })

    }


}