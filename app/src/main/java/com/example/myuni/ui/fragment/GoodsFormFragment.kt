package com.example.myuni.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.databinding.FragmentGoodsFormBinding
import com.example.myuni.model.Contacts
import com.example.myuni.model.Goods
import com.example.myuni.utils.BitmapUtils
import com.example.myuni.viewmodel.GoodsViewModel
import com.example.myuni.viewmodel.MeViewModel
import com.example.myuni.viewmodel.UtilViewModel
import org.jetbrains.anko.support.v4.toast


class GoodsFormFragment : Fragment(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private lateinit var binding: FragmentGoodsFormBinding
    private lateinit var goodViewModel: GoodsViewModel
    private lateinit var utilViewModel: UtilViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var goodsViewModel: GoodsViewModel
    private lateinit var good: Goods
    private lateinit var currentUser: Contacts
    private var selectedOption = ""
    private var identity = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goods_form, container, false)
        initViewModel()
        init()
        return binding.root
    }

    private fun initViewModel(){
        goodViewModel = ViewModelProvider(requireActivity()).get(GoodsViewModel::class.java)
        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
        goodsViewModel = ViewModelProvider(requireActivity()).get(GoodsViewModel::class.java)
    }

    private fun init(){
        var bundle = requireArguments()
        identity = bundle.getString("identity")!!
        good = bundle.getSerializable("good") as Goods

        utilViewModel.setNavBarStatus(false)
        currentUser = meViewModel.getLoginUser()!!

        //set product information
        binding.tvProductName.text = good.name
        binding.iv1.setImageBitmap(BitmapUtils.convertStringToIcon(good.image1))
        if (good.image2 != "")
            binding.iv2.setImageBitmap(BitmapUtils.convertStringToIcon(good.image2))
        binding.btnSubmit.setOnClickListener(this)

        if (identity == "buyer")
            buyerPage()
        else if (identity == "seller")
            sellerPage()
    }

    private fun buyerPage(){
        binding.tvBuyer.text = currentUser.name
        binding.tvBuyerEmail.text = currentUser.email

        binding.btnSubmit.text = "Purchase"
        binding.radioGroup.setOnCheckedChangeListener(this)
        binding.radioGroup.check(R.id.rb_card)
        binding.btnCancel.visibility = View.GONE
    }

    private fun sellerPage(){
        binding.tvBuyer.text = good.buyerName
        binding.tvBuyerEmail.text = good.buyerEmail

        binding.btnSubmit.text = "Confirm"
        when (good.method) {
            "card" -> binding.radioGroup.check(R.id.rb_card)
            "cash" -> binding.radioGroup.check(R.id.rb_cash)
            "other" -> binding.radioGroup.check(R.id.rb_other)
        }
        binding.rbCard.isEnabled = false
        binding.rbCash.isEnabled = false
        binding.rbOther.isEnabled = false
        //disableRadioGroup(binding.radioGroup)

        binding.etPrice.setText(good.price)
        binding.etAddress.setText(good.address)

        setEditAble(binding.etPrice)
        setEditAble(binding.etAddress)

        binding.btnCancel.setOnClickListener(this)
    }

    private fun setEditAble(et: EditText){
        et.isFocusableInTouchMode = false
        et.keyListener = null
        et.isClickable = false
        et.isFocusable = false
        et.isEnabled = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_submit -> {
                if (identity == "buyer"){
                    if (checkContent()){
                        good.buyerName = binding.tvBuyer.text.toString()
                        good.buyerEmail = binding.tvBuyerEmail.text.toString()
                        good.method = selectedOption
                        good.finalPrice = binding.etPrice.text.toString()
                        good.address = binding.etAddress.text.toString()

                        goodsViewModel.purchaseGoods(good)
                        Toast.makeText(context, "Purchase successfully", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(binding.root).navigate(R.id.navigation_shop)
                    }else
                        toast("Please fill in all the information")
                } else if (identity == "seller"){
                    goodsViewModel.confirmGoods(good)
                    NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.navigation_shop)
                    Toast.makeText(context,"Confirm successfully!", Toast.LENGTH_LONG).show()
                }
            }
            R.id.btn_cancel -> {
                goodsViewModel.cancelOrder(good.orderNum, good)
            }
        }
    }

    private fun checkContent(): Boolean{
        if (binding.etPrice.text.toString() != "" && binding.etAddress.text.toString() != "")
            return true
        return false
    }

//    private fun disableRadioGroup(radioGroup: RadioGroup){
//        for(i in 0..radioGroup.childCount){
//            radioGroup.getChildAt(i).isEnabled = false
//        }
//    }

    override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
        when(p1){
            R.id.rb_card ->{
                selectedOption = "card"
            }
            R.id.rb_cash -> {
                selectedOption = "cash"
            }
            R.id.rb_other -> {
                selectedOption = "other"
            }
        }
    }

}