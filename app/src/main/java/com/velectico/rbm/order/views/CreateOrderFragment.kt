package com.velectico.rbm.order.views

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.CreateOrderDetailsResponse
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.beats.model.CreateOrderListRequestParams
import com.velectico.rbm.database.DB_Manager
import com.velectico.rbm.database.Helper_Cart_DB
import com.velectico.rbm.databinding.FragmentCreateOrderBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.adapters.OrderCartListAdapter
import com.velectico.rbm.products.view.ProductListFragment.Companion.seletedItemsChecked
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.productItemClickListener
import retrofit2.Callback


class CreateOrderFragment : BaseFragment(),productItemClickListener {
    private lateinit var binding : FragmentCreateOrderBinding
    private var orderCartList : List<CreateOrderListDetails> = emptyList()
    private lateinit var adapter: OrderCartListAdapter
    private var locationManager : LocationManager? = null
    var db: Helper_Cart_DB? = null
    var segId= ""
    var catId= ""
    private lateinit var listener: productItemClickListener
    override fun getLayout(): Int {
        return R.layout.fragment_create_order
    }
    companion object{
        var orderItems:HashMap<String,String> = HashMap()
        var schemeItems:HashMap<String,String?> = HashMap()
        var seletedItems = HashSet<CreateOrderListDetails>()
    }


    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentCreateOrderBinding
        listener = this
        //orderCartList = OrderCart().getDummyOrderCart()
        catId = arguments?.getString(  "catId").toString()
        segId = arguments?.getString(  "segId").toString()

        db =  Helper_Cart_DB(context);
        DB_Manager.initializeInstance(db);
        db!!.clearCart()
        //showToastMessage(catId)
        binding.btnCheckOut.setOnClickListener {
            moveToOrderPreview()
        }

        binding.fabFilter.setOnClickListener {
            moveToProdFilter()
        }
        if (RBMLubricantsApplication.fromProductList == "Product"){
            //showToastMessage("from Product")
            orderCartList.toMutableList().clear()
            orderCartList = seletedItemsChecked.toMutableList()

            setUpRecyclerView()

//            adapter = OrderCartListAdapter(context!!,listener);
//            binding.rvCartList.adapter = adapter
//            val x = mutableListOf<CreateOrderListDetails>()
//            for (i in seletedItemsChecked ){
//                x.add(i)
//            }
//            adapter.orderCart = seletedItemsChecked.toList()
//            adapter.notifyDataSetChanged()
        }
        else {
            callCreateOrderList()
        }

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        adapter = OrderCartListAdapter(context!!,listener, binding.tvProdId, binding.btnCheckOut);
        binding.rvCartList.adapter = adapter
        adapter.orderCart = orderCartList
        //setUp()
    }


    private fun setUp(){
        checkPermission()

    }

    private fun checkPermission(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(baseActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(baseActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun moveToOrderPreview(){
        val navDirection =  CreateOrderFragmentDirections.actionCreateOrderFragmentToOrderPreviewFragment()
        Navigation.findNavController(binding.btnCheckOut).navigate(navDirection)
    }

    private fun moveToProdFilter(){
        val navDirection =  CreateOrderFragmentDirections.actionCreateOrderFragmentToProductFilterFragment()
        Navigation.findNavController(binding.fabFilter).navigate(navDirection)

    }

    var hud: KProgressHUD? = null
    fun  showHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();
    }

    fun hide(){
        hud?.dismiss()
    }
    fun callCreateOrderList(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getCreateOrderList(
            CreateOrderListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),catId, segId)
        )
        responseCall.enqueue(CreateOrderDetailsResponse as Callback<CreateOrderDetailsResponse>)
    }

    private val CreateOrderDetailsResponse = object : NetworkCallBack<CreateOrderDetailsResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<CreateOrderDetailsResponse>
        ) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test333","OrderHistoryDetailsResponse status="+response.data)
                orderCartList.toMutableList().clear()
                if (response.data.count > 0) {

                        orderCartList = response.data.CreateOrderList!!.toMutableList()
                        setUpRecyclerView()

                } else {
                    showToastMessage("No data found")

                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }


    }
    override fun onItemClick() {
        //caculateGross()

    }


    fun caculateGross(){
        var grossAmt = 0.0
        if (seletedItems.size !=0){
            for (i in seletedItems ){
                val total  = (i.PM_Disc_Price!!.toDouble())*(orderItems[i.PM_Quantity_Val!!]!!.toDouble()*(i.PM_Quantity_Val!!.toDouble()))
                grossAmt += total
            }
        }else{
            binding.tvProdId.setText("$grossAmt")

        }

        binding.tvProdId.setText("$grossAmt")
    }

}