package com.example.myuni.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.adapter.PostingAdapter
import com.example.myuni.databinding.FragmentPostingBinding
import com.example.myuni.model.Posting
import com.example.myuni.viewmodel.PostingViewModel
import com.example.myuni.viewmodel.UtilViewModel

class PostingFragment : Fragment(), AdapterView.OnItemClickListener {
    private lateinit var binding: FragmentPostingBinding
    private lateinit var postingAdapter: PostingAdapter
    private lateinit var postingViewModel: PostingViewModel
    private lateinit var utilViewModel: UtilViewModel
    private var postingList = ArrayList<Posting>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add("Publish").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.navigation_publish)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_posting, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun init() {
        utilViewModel.setNavBarStatus(false)

        postingAdapter = PostingAdapter(requireContext(), R.layout.posting_item, postingList)
        binding.lvPostingList.adapter = postingAdapter
        binding.lvPostingList.onItemClickListener = this

        postingViewModel.initPostingList()
        postingViewModel.postingsList.observe(viewLifecycleOwner, Observer {
            postingList.clear()
            postingList.addAll(it)
            postingAdapter.notifyDataSetChanged()
            binding.lvPostingList.setSelection(it.size)
        })
    }

    private fun initViewModel(){
        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
        postingViewModel = ViewModelProvider(requireActivity()).get(PostingViewModel::class.java)
    }

    override fun onItemClick(p0: AdapterView<*>?, v: View?, position: Int, p3: Long) {
        val bundle = Bundle()
        bundle.putSerializable("posting", postingList[position])
        Navigation.findNavController(v!!).navigate(R.id.navigation_postingDetails, bundle)
    }

}