package com.velectico.rbm.products.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.DropdownDetails
import com.velectico.rbm.beats.model.OrderVSQualityRequestParams
import com.velectico.rbm.beats.model.OrderVSQualityResponse
import com.velectico.rbm.databinding.FragmentProductFilterBinding
import com.velectico.rbm.navdrawer.views.DashboardActivity
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.SharedPreferencesClass
import kotlinx.android.synthetic.main.fragment_product_filter.*
import retrofit2.Callback
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ProductFilterFragment : BaseFragment()  {
    private lateinit var binding: FragmentProductFilterBinding
    var segId= ""
    var catId= ""
    override fun getLayout(): Int {
        return R.layout.fragment_product_filter
    }
    var hud: KProgressHUD? = null
    fun  showHud(){
        if (hud!=null){

            hud!!.show()
        }
    }

    fun hide(){
        hud?.dismiss()

    }
    var dataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    val orderVSQualityResponseResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                dataList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Segment")
                statList.add("All")
                for (i in dataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                binding.spinnerSegment.adapter = adapter2
                /*if (SharedPreferencesClass.retriveData(
                        context as Context,
                        "seg_name") != null) {
                    val spinnerPosition: Int = adapter2!!.getPosition(
                        SharedPreferencesClass.retriveData(
                            context as Context,
                            "seg_name"))
                    binding.spinnerSegment.setSelection(spinnerPosition)
                }*/


                binding.spinnerSegment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {

                        if (binding.spinnerSegment.selectedItem == "Select Segment") {
                           /* SharedPreferencesClass.insertData(
                                context as Context,
                                "seg_name","Select Segment")*/
                           // segId=""

                        }else if (binding.spinnerSegment.selectedItem == "All") {
                            /*SharedPreferencesClass.insertData(
                                context as Context,
                                "seg_name","All")*/
                           // segId="0"
                        } else {
                            val x = dataList[position-2]
                            segId = x.Exp_Head_Id!!
                           /* SharedPreferencesClass.insertData(
                                context as Context,
                                "seg_name",x.Exp_Head_Name)*/



                        }


                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>) {}
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }


    var catdataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    val catResponseResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                println()
                catdataList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Category")
                statList.add("All")

                for (i in catdataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                binding.spinnerCategory.adapter = adapter2

               /* if (SharedPreferencesClass.retriveData(
                        context as Context,
                        "cat_name") != null) {
                    val spinnerPosition: Int = adapter2!!.getPosition(
                        SharedPreferencesClass.retriveData(
                        context as Context,
                        "cat_name"))
                    binding.spinnerCategory.setSelection(spinnerPosition)
                }*/
                binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (position>=2){

                            //showToastMessage(catId)
                            if (binding.spinnerCategory.selectedItem == "Select Category") {
                                /*SharedPreferencesClass.insertData(
                                    context as Context,
                                    "cat_name","Select Category")
                              //  catId=""*/

                            }else if (binding.spinnerCategory.selectedItem == "All") {
                                /*SharedPreferencesClass.insertData(
                                    context as Context,
                                    "cat_name","All")*/
                               // catId="0"
                            } else {
                                val x = catdataList[position-2]
                                catId = x.Exp_Head_Id!!
                                /*SharedPreferencesClass.insertData(
                                    context as Context,
                                    "cat_name",x.Exp_Head_Name)*/



                            }
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>) {}
                }


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }


    fun initHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentProductFilterBinding
        //showToastMessage(RBMLubricantsApplication.filterFrom)
        initHud()
        binding.btnOrderHistory.setOnClickListener {
            if (spinnerSegment.selectedItem.toString() == "Select Segment" &&
                spinnerCategory.selectedItem.toString() != "Select Category"){
                segId="";
                getList( catId, segId)

            }else if (spinnerSegment.selectedItem.toString() != "Select Segment" &&
                spinnerCategory.selectedItem.toString() == "Select Category"){
                catId=""
                getList( catId, segId)

            }else if (spinnerSegment.selectedItem.toString() == "Select Segment" ||
                spinnerCategory.selectedItem.toString() == "Select Category"){
                Toast.makeText(context,"Select Any One",Toast.LENGTH_LONG).show()

            }else if (spinnerSegment.selectedItem.toString() == "All" &&
                spinnerCategory.selectedItem.toString() == "Select Category"){
                segId="0"
                catId="0"
                getList( catId, segId)

            }else if (spinnerSegment.selectedItem.toString() == "Select Segment" &&
                spinnerCategory.selectedItem.toString() == "All"){
                segId="0"
                catId="0"
                getList( catId, segId)

            } else if (spinnerSegment.selectedItem.toString() == "All" &&
                spinnerCategory.selectedItem.toString() == "All"){
                segId="0"
                catId="0"
                getList( catId, segId)

            }else{
                getList( catId, segId)

            }

        }
        binding.btnClr.setOnClickListener {
            callApi("Product Segment")
            callApi("Product Category")
        }
         callApi("Product Segment")
         callApi("Product Category")


    }



     private fun getList(catId: String, segId: String) {
         val navDirection =  ProductFilterFragmentDirections.actionProductFilterFragmentToProductList(catId, segId)
         Navigation.findNavController(binding.btnOrderHistory).navigate(navDirection)
         /*if (RBMLubricantsApplication.filterFrom == "Product"){
             val navDirection =  ProductFilterFragmentDirections.actionProductFilterFragmentToProductList(catId, segId)
             Navigation.findNavController(binding.btnOrderHistory).navigate(navDirection)
         }
         else {
             RBMLubricantsApplication.fromProductList = ""
             val navDirection =
                 ProductFilterFragmentDirections.actionProductFilterFragmentToCreateOrderFragment(
                     catId,
                     segId
             )
             Navigation.findNavController(binding.btnOrderHistory).navigate(navDirection)
         }*/
     }


    fun callApi(type:String){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type))
        when(type){
            "Product Segment"->{
                responseCall.enqueue(orderVSQualityResponseResponse as Callback<OrderVSQualityResponse>)
            }
            "Product Category"->{
                responseCall.enqueue(catResponseResponse as Callback<OrderVSQualityResponse>)
            }

        }
    }
}
