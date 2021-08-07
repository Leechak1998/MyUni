package com.example.myuni.ui.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myuni.R
import com.example.myuni.adapter.CommentAdapter
import com.example.myuni.databinding.FragmentPostingDetailsBinding
import com.example.myuni.model.Comment
import com.example.myuni.model.Contacts
import com.example.myuni.model.Posting
import com.example.myuni.utils.BitmapUtils
import com.example.myuni.utils.TimeUtils
import com.example.myuni.viewmodel.MeViewModel
import com.example.myuni.viewmodel.PostingViewModel
import com.example.myuni.viewmodel.UtilViewModel
import org.jetbrains.anko.support.v4.toast
import java.time.LocalDateTime

class PostingDetailsFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentPostingDetailsBinding
    private lateinit var postingViewModel: PostingViewModel
    private lateinit var utilViewModel: UtilViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var commentAdapter: CommentAdapter
    private var commentsList = ArrayList<Comment>()
    private lateinit var posting: Posting
    private lateinit var currentUser: Contacts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_posting_details, container, false)
        initViewModel()
        init()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun init() {
        utilViewModel.setNavBarStatus(true)
        currentUser = meViewModel.getLoginUser()!!

        val bundle = requireArguments()
        posting = bundle.getSerializable("posting")!! as Posting
        binding.tvTittle.text = posting.tittle
        binding.tvDescription.text = posting.description
        binding.tvAuthor.text = posting.publisherName
        binding.tvCategory.text = posting.category
        binding.tvTime.text = posting.time
        binding.ivProfile.setImageBitmap(BitmapUtils.convertStringToIcon(posting.publisherPic))
        if (posting.image1 != null)
            binding.ivImage1.setImageBitmap(BitmapUtils.convertStringToIcon(posting.image1))
        if (posting.image2 != null)
            binding.ivImage2.setImageBitmap(BitmapUtils.convertStringToIcon(posting.image2))
        if (posting.image3 != null)
            binding.ivImage3.setImageBitmap(BitmapUtils.convertStringToIcon(posting.image3))

        //init comments recyclerView
        val manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.rvCommentsList.layoutManager = manager
        commentAdapter = CommentAdapter(commentsList)
        binding.rvCommentsList.adapter = commentAdapter
        binding.commentAdapter = commentAdapter
        binding.rvCommentsList.isNestedScrollingEnabled = false
        postingViewModel.initCommentsList(posting.postingNum.toString())

        postingViewModel.commentsList.observe(viewLifecycleOwner, Observer {
            commentsList.clear()
            commentsList.addAll(it)
            commentAdapter.notifyDataSetChanged()
        })

        binding.btnAddComment.setOnClickListener(this)
    }

    private fun initViewModel() {
        postingViewModel = ViewModelProvider(requireActivity()).get(PostingViewModel::class.java)
        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_addComment -> {
                val buttonDialog = Dialog(requireContext()/*,R.style.Theme_AppCompat_DayNight_Dialog*/)
                val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_addcomment,null)
                buttonDialog.setContentView(view)
                val layoutParams: ViewGroup.LayoutParams = view.layoutParams
                layoutParams.width = resources.displayMetrics.widthPixels
                view.layoutParams = layoutParams
                buttonDialog.window?.setGravity(Gravity.BOTTOM)
//                buttonDialog.window?.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog)
                buttonDialog.show()

                val review = buttonDialog.findViewById<EditText>(R.id.et_comment)
                val publishBtn = buttonDialog.findViewById<Button>(R.id.btn_publish)

                publishBtn.setOnClickListener{
                    if (review.text.toString() != ""){
                        val time = TimeUtils.getCurrentTime(LocalDateTime.now())

                        val newComment = Comment(currentUser.name, currentUser.imageId, review.text.toString(), time)
                        postingViewModel.publishComment(posting, newComment)
                        toast("Publish successfully!")
                        buttonDialog.dismiss()
                    } else {
                        toast("Can not publish empty comment!")
                    }

                }
            }
        }
    }



}