package com.velectico.rbm.products.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.CreateOrderDetailsResponse
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.beats.model.CreateOrderListRequestParams
import com.velectico.rbm.databinding.FragmentProductListBinding
import com.velectico.rbm.databinding.RowProductListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.products.adapters.ProductListAdapter
import com.velectico.rbm.utils.*
import retrofit2.Callback

/**
 * Fragement to display list of all the products
 */

//https://developer.android.com/topic/libraries/view-binding#kotlin
class ProductListFragment : BaseFragment() {

    private lateinit var binding: FragmentProductListBinding
    private lateinit var adapter : ProductListAdapter
    private var orderCartList : List<CreateOrderListDetails> = emptyList()
    var segId= ""
    var catId= ""
    var role=""
    companion object {
        var seletedItemsChecked = HashSet<CreateOrderListDetails>()
    }
    override fun getLayout(): Int {
        return R.layout.fragment_product_list
    }

    override fun init(binding: ViewDataBinding) {
        //setHasOptionsMenu(true)

        this.binding = binding as FragmentProductListBinding
        catId = arguments?.getString(  "catId").toString()
        segId = arguments?.getString(  "segId").toString()
        role= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Role").toString()


       // showToastMessage(role)
        /*if (role == MECHANIC_ROLE){
            binding.fab.hide()

        }else  if (role == DEALER_ROLE){
            binding.fab.hide()

        }else  if (role == DISTRIBUTER_ROLE){
            binding.fab.hide()

        }else{
            binding.fab.show()

        }*/




        seletedItemsChecked.clear()
        binding.fab.setOnClickListener {
            if (seletedItemsChecked.count() > 0){
                RBMLubricantsApplication.filterFrom = ""
                RBMLubricantsApplication.fromProductList = "Product"
                moveToOrder()
            }
            else{
                showToastMessage("Please select atleast one list to continue")
            }

        }

        binding.fabFilter.setOnClickListener {
            moveToFilter()
        }
        setUpRecyclerView()
        callCreateOrderList()
        //Toast.makeText(context,"Cat Id"+catId, Toast.LENGTH_LONG).show()
       // Toast.makeText(context,"Seg Id"+segId,Toast.LENGTH_LONG).show()

    }




    private fun setUpRecyclerView(){
        /* adapter = ProductListAdapter(
             requireActivity()
         );*/

        adapter = ProductListAdapter(object : ProductListAdapter.IProdListActionCallBack{
            override fun moveToProdDetails(position: Int, beatTaskId: String?,binding: RowProductListBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  ProductListFragmentDirections.actionProductListToProductDetailsListFragment(orderCartList[position])
                Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)
            }
        }, context as Context);


        binding?.rvProductList?.adapter = adapter
        adapter.data = orderCartList
    }


    private fun moveToOrder(){
        val navDirection =  ProductListFragmentDirections.actionProductListToCreateOrderFragment("","")
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }

    private fun moveToFilter(){

        val navDirection =  ProductListFragmentDirections.actionProductListToProductFilterFragment()
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
            CreateOrderListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),catId,segId)
        )
        responseCall.enqueue(CreateOrderDetailsResponse as Callback<CreateOrderDetailsResponse>)
    }

    private val CreateOrderDetailsResponse = object : NetworkCallBack<CreateOrderDetailsResponse>() {
        @SuppressLint("RestrictedApi")
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<CreateOrderDetailsResponse>
        ) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test333","OrderHistoryDetailsResponse status="+response.data)
                orderCartList.toMutableList().clear()
                //showToastMessage("Count"+response.data.count)
                if (response.data.count > 0) {
                    orderCartList = response.data.CreateOrderList!!.toMutableList()
                    binding.tvNoData.visibility = View.GONE
                    binding.rvProductList.visibility = View.VISIBLE
                    /*else  if (role == DEALER_ROLE){
                        binding.fab.hide()

                    }else  if (role == DISTRIBUTER_ROLE){
                        binding.fab.hide()

                    }*/
                    if (role == MECHANIC_ROLE){
                        binding.fab.hide()

                    }else{
                        binding.fab.show()

                    }

                    //binding.fab.visibility = View.VISIBLE

                    setUpRecyclerView()
                } else {
                    //showToastMessage("No data found")
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.rvProductList.visibility = View.VISIBLE
                    binding.fab.visibility = View.GONE

                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }


    }
}
