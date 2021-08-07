package com.example.myuni.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.RadioGroup
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
import org.jetbrains.anko.support.v4.toast

class PostingFragment : Fragment(), AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener,
    TextWatcher {
    private lateinit var binding: FragmentPostingBinding
    private lateinit var postingAdapter: PostingAdapter
    private lateinit var postingViewModel: PostingViewModel
    private lateinit var utilViewModel: UtilViewModel
    private var postingList = ArrayList<Posting>()
    private lateinit var radioGroup: RadioGroup
    private var selectedOption: String? = null

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

        radioGroup = binding.radioGroup
        radioGroup.check(R.id.rb_all)
        radioGroup.setOnCheckedChangeListener(this)

        //set listener
        binding.btnSearch.setOnClickListener(this)
        binding.etInput.addTextChangedListener(this)
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

    override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
        when(p1){
            R.id.rb_all ->{
                radioGroup.check(p1)
                selectedOption = "All"
                postingViewModel.selectedPostingByCategory("All")
            }
            R.id.rb_study -> {
                radioGroup.check(p1)
                selectedOption = "Study"
                postingViewModel.selectedPostingByCategory("Study")
            }
            R.id.rb_life -> {
                radioGroup.check(p1)
                selectedOption = "Life"
                postingViewModel.selectedPostingByCategory("Life")
            }
            R.id.rb_entertainment -> {
                radioGroup.check(p1)
                selectedOption = "Entertainment"
                postingViewModel.selectedPostingByCategory("Entertainment")
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_search -> {
                if (binding.btnSearch.text == "GO"){
                    val content = binding.etInput.text.toString()
                    if (content != ""){
                        binding.btnSearch.text = "Clear"
                        if (!postingViewModel.searchPosting(content))
                            toast("No related posting")
                    }
                } else if (binding.btnSearch.text == "Clear"){
                    binding.etInput.text.clear()
                    binding.btnSearch.text = "GO"
                    postingViewModel.initPList()
                }
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0?.length == 0){
            binding.btnSearch.text = "GO"
            binding.etInput.clearFocus();//取消焦点
        }
    }

}