package com.example.myuni.ui.fragment

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentAddCommunityBinding
import com.example.myuni.model.Community
import com.example.myuni.utils.BitmapUtils
import com.example.myuni.utils.OrderUtils
import com.example.myuni.utils.Utils
import com.example.myuni.viewmodel.CommunityViewModel
import com.example.myuni.viewmodel.MeViewModel
import org.jetbrains.anko.support.v4.selector
import java.io.FileNotFoundException
import java.io.InputStream

class AddCommunityFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentAddCommunityBinding
    private lateinit var communityViewModel: CommunityViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var currentUser: String
    private var groupProfile: String? = null
    private var groupName: String? = null
    private var groupDes: String? = null
    private var nationality: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add("Create").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (checkContents()){
            createCommunity()
            NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.navigation_community)
            Toast.makeText(context, "Create successfully", Toast.LENGTH_SHORT).show()
        }else
            Toast.makeText(context, "Please complete all information", Toast.LENGTH_SHORT).show()

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_community, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun initViewModel() {
        communityViewModel = ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
    }

    private fun init() {
        currentUser = meViewModel.getLoginUser()!!.email!!

        //set listener
        binding.tvCountry.setOnClickListener(this)
        binding.ivGroupProfile.setOnClickListener(this)
    }

    private fun checkContents(): Boolean {
        groupProfile = BitmapUtils.convertIconToString(binding.ivGroupProfile.drawable.toBitmap())
        groupName = binding.etName.text.toString()
        groupDes = binding.etDescription.text.toString()
        nationality = binding.tvCountry.text.toString()

        if (groupName != null && groupDes != null &&  nationality != "Please select your nationality"){
            return true
        }
        return false
    }

    private fun createCommunity(){
        val communityNum = OrderUtils.getOrderNumber()
        val community = Community(communityNum, groupProfile!!, groupName!!, groupDes!!, nationality!!, currentUser)
        communityViewModel.createCommunity(community)
    }

    private fun showCamera(){
        val buttonDialog = Dialog(requireContext())
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_content,null)
        buttonDialog.setContentView(view)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.width = resources.displayMetrics.widthPixels
        view.layoutParams = layoutParams
        buttonDialog.window?.setGravity(Gravity.BOTTOM)
        //buttonDialog.window?.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog)
        buttonDialog.show()

        val photo = buttonDialog.findViewById<TextView>(R.id.tv_photo)
        val album = buttonDialog.findViewById<TextView>(R.id.tv_album)
        val cancel = buttonDialog.findViewById<TextView>(R.id.tv_cancel)

        photo.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                    startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE)
                }
            }
            buttonDialog.dismiss()
        }

        album.setOnClickListener {
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
            Intent(Intent.ACTION_PICK).also { pickPicture ->
                pickPicture.setType("image/*").also {
                    startActivityForResult(pickPicture, Utils.PHOTO_REQUEST_GALLERY)
                }
            }
            buttonDialog.dismiss()
        }

        cancel.setOnClickListener {
            buttonDialog.dismiss()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_country -> {
                selector("Please select your nationality", Utils.countriesName) { _, i ->
                    binding.tvCountry.text = Utils.countriesName[i]
                }
            }
            R.id.iv_groupProfile -> {
                showCamera()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        lateinit var imageBitmap: Bitmap
        if (requestCode == Utils.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap

        } else if( requestCode == Utils.PHOTO_REQUEST_GALLERY && resultCode == Activity.RESULT_OK){
            val resolver: ContentResolver = requireActivity().contentResolver
            try {
                val inputStream: InputStream? = resolver.openInputStream(data?.data!!)
                imageBitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }

        binding.ivGroupProfile.setImageBitmap(imageBitmap)
    }
}