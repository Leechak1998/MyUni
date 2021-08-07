package com.example.myuni.ui.fragment

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.myuni.R
import com.example.myuni.adapter.GoodsAdapter
import com.example.myuni.databinding.FragmentShopBinding
import com.example.myuni.model.Contacts
import com.example.myuni.model.Goods
import com.example.myuni.utils.Uni
import com.example.myuni.viewmodel.ContactsViewModel
import com.example.myuni.viewmodel.GoodsViewModel
import com.example.myuni.viewmodel.MeViewModel
import com.example.myuni.viewmodel.UtilViewModel
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.toast


class ShopFragment : Fragment(), View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    private lateinit var binding: FragmentShopBinding
    private lateinit var goodsViewModel: GoodsViewModel
    private lateinit var meViewModel: MeViewModel
    private lateinit var utilViewModel: UtilViewModel
    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var goodsAdapter: GoodsAdapter
    private var goodsList = ArrayList<Goods>()
    private lateinit var currentUser: String
    private var uniName = ""
    private var friend = ""
    private var friendsArrayList = ArrayList<Contacts>()
    private var distance = ""
    private var distanceList = listOf<String>("1km", "5km", "10km", "10km+")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add("Sell").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        NavHostFragment.findNavController(this.requireParentFragment()).navigate(R.id.action_navigation_shop_to_navigation_sell)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false)

        init()

        binding.etInput.addTextChangedListener(this)
        binding.lvGoods.onItemClickListener = this
        binding.btnDistance.setOnClickListener(this)
        binding.btnUni.setOnClickListener(this)
        binding.btnFriends.setOnClickListener(this)
        binding.btnSearch.setOnClickListener(this)
        binding.btnClear.setOnClickListener(this)

        return binding.root
    }

    private fun init(){
        goodsViewModel = ViewModelProvider(requireActivity()).get(GoodsViewModel::class.java)
        goodsViewModel.initGoodsList()

        goodsViewModel.goodsList.observe(viewLifecycleOwner, Observer {
            goodsList.clear()
            goodsList.addAll(it)
            goodsAdapter.notifyDataSetChanged()
            binding.lvGoods.setSelection(it.size)
        })
        goodsAdapter = GoodsAdapter(requireContext(), R.layout.goods_item, goodsList)
        binding.goodsAdapter = goodsAdapter


        meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)
        currentUser = meViewModel.getLoginUser()!!.email.toString()

        utilViewModel = ViewModelProvider(requireActivity()).get(UtilViewModel::class.java)
        utilViewModel.setNavBarStatus(false)

        contactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        contactsViewModel.initContactsList(currentUser)
        contactsViewModel.contactsList.observe(viewLifecycleOwner, Observer {
            friendsArrayList.clear()
            friendsArrayList.addAll(it)
//            println("更新friends列表")
//            for (i in friendsArrayList.indices){
//                println("------$i----${friendsArrayList[i].name}")
//            }
        })

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_search ->{
                if (binding.btnSearch.text == "GO"){
                    val content = binding.etInput.text.toString()
                    if (content != ""){
                        binding.btnSearch.text = "Clear"
                        if (!goodsViewModel.searchGoods(content))
                            toast("No related products")
                    }
                } else if (binding.btnSearch.text == "Clear"){
                    binding.etInput.text.clear()
                    binding.btnSearch.text = "GO"
                    goodsViewModel.initGList()
                }
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
            R.id.btn_uni -> {
                selector("Please select University", Uni.uniName) { _, i ->
                    uniName = Uni.uniName[i]
                    goodsViewModel.optionUni = uniName
                    binding.btnUni.background.setColorFilter(Color.parseColor("gray"), PorterDuff.Mode.DARKEN)
                    if (!goodsViewModel.selectedByOptions()[0]){
                        toast("No related products")
                        //initBtn()
                    }
                }
            }
            R.id.btn_friends -> {

                var friendsNameList = ArrayList<String>()
                friendsNameList.add("All friends")

                var friendsEmailList = ArrayList<String>()

                for (i in friendsArrayList.indices){
                    friendsNameList.add(friendsArrayList[i].name!!)
                    friendsEmailList.add(friendsArrayList[i].email!!)
                }

                selector("Please select your friend", friendsNameList) { _, i ->
                    if (i != 0){
                        friend = friendsArrayList[i-1].email!!
                        goodsViewModel.optionFri = friend
                        goodsViewModel.optionFris = ArrayList()
                        binding.btnFriends.background.setColorFilter(Color.parseColor("gray"), PorterDuff.Mode.DARKEN)
                        if (!goodsViewModel.selectedByOptions()[1]){
                            toast("No related products")
                            //initBtn()
                        }
                    }
                    else{
                        goodsViewModel.optionFris = friendsEmailList
                        goodsViewModel.optionFri = ""
                        binding.btnFriends.background.setColorFilter(Color.parseColor("gray"), PorterDuff.Mode.DARKEN)
                        if (!goodsViewModel.selectedByOptions()[1]){
                            toast("No related products")
                            //initBtn()
                        }

                    }

                }
            }
            R.id.btn_distance -> {
                selector("Please select range distance", distanceList) { _, i ->
                    distance = distanceList[i]
                }
            }
            R.id.btn_clear -> {
                initBtn()
            }
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, v: View?, position: Int, p3: Long) {
        val goods = goodsList[position]
        val bundle = Bundle().also {
            it.putSerializable("goods", goods)
            it.putString("currentUser", currentUser)
        }
        Navigation.findNavController(v!!).navigate(R.id.navigation_goodsDetails, bundle)
    }

    private fun initBtn(){
        //clear selected options
        goodsViewModel.optionDis = ""
        goodsViewModel.optionFri = ""
        goodsViewModel.optionUni = ""
        //clear background color
        binding.btnFriends.background.clearColorFilter()
        binding.btnDistance.background.clearColorFilter()
        binding.btnUni.background.clearColorFilter()

        goodsViewModel.initGList()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0?.length == 0){
            binding.btnSearch.text = "GO"
            binding.etInput.clearFocus();//取消焦点
        }

    }

}