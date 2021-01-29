package com.velectico.rbm.order.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.database.CartProduct
import com.velectico.rbm.database.DB_Manager
import com.velectico.rbm.database.Helper_Cart_DB
import com.velectico.rbm.databinding.FragmentOrderCheckOutBinding
import com.velectico.rbm.dealer.model.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.model.CreditDaysOutStandingRequestParams
import com.velectico.rbm.order.model.CreditDaysOutStandingResponse
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.SharedPreferencesClass
import retrofit2.Callback
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderCheckOutFragment : BaseFragment() {
    private lateinit var binding: FragmentOrderCheckOutBinding
    var userId = ""
    var dealerId = ""
    var distribId = ""
    var paymentType = "C"
    var db: Helper_Cart_DB? = null
    var checkoutItemsList: ArrayList<CartProduct>? = ArrayList<CartProduct>()
    var orderProductList: ArrayList<OrderDetailsParams>? = null
    var taskId = ""
    var dateFormat: DateFormat? = null
    var date: Date? = null
    var today_date: String? = null
    var taskDetails = BeatTaskDetails()
    var role = ""
    var areaValue=""
    var districtValue=""


    override fun getLayout(): Int {
        return R.layout.fragment_order_check_out

    }

    @SuppressLint("SimpleDateFormat")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderCheckOutBinding
        userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        //showToastMessage("User Id"+userId)
        db = Helper_Cart_DB(context)
        DB_Manager.initializeInstance(db)
        checkoutItemsList!!.clear()
        checkoutItemsList!!.addAll(db!!.getCartItems())
        // val languages = resources.getStringArray(R.array.array_dealDist)
        dateFormat = SimpleDateFormat("yyyy-MM-dd")
        date = Date()
        today_date = dateFormat!!.format(date)
        /* taskDetails = arguments!!.get("taskDetails") as BeatTaskDetails
         //showToastMessage(taskDetails.toString())
         if (taskDetails.dealerId != null){
             dealerId = taskDetails.dealerId.toString()
             distribId = taskDetails.distribId.toString()

         }*/

        role = SharedPreferencesClass.retriveData(context as Context, "UM_Role").toString()
        if (role == "L") {
            if (RBMLubricantsApplication.fromBeat == "Beat") {
                binding.llSpinnerDealDis.visibility = View.GONE
                binding.llSpinnerType.visibility = View.GONE
                dealerId = DataConstant.dealerId
                distribId = DataConstant.distributorId
                taskId = DataConstant.taskId

                DataConstant.dealerId = dealerId
                DataConstant.distributorId = distribId
                DataConstant.taskId = taskId
                callApiCreditOutStand()
                // showToastMessage(RBMLubricantsApplication.fromBeat)
                //showToastMessage("DeID"+ DataConstant.dealerId)
                //showToastMessage("DstID"+DataConstant.distributorId)
                // showToastMessage("TID"+DataConstant.taskId)
            } else {
                binding.llSpinnerDealDis.visibility = View.GONE
                //binding.llSpinnerType.visibility = View.GONE


            }
        }else if (role == "P") {
            if (RBMLubricantsApplication.fromBeat == "Beat") {
                binding.llSpinnerDealDis.visibility = View.GONE
                binding.llSpinnerType.visibility = View.GONE
                dealerId = DataConstant.dealerId
                distribId = DataConstant.distributorId
                taskId = DataConstant.taskId

                DataConstant.dealerId = dealerId
                DataConstant.distributorId = distribId
                DataConstant.taskId = taskId
                callApiCreditOutStand()
                // showToastMessage(RBMLubricantsApplication.fromBeat)
                //showToastMessage("DeID"+ DataConstant.dealerId)
                //showToastMessage("DstID"+DataConstant.distributorId)
                // showToastMessage("TID"+DataConstant.taskId)
            } else {
                binding.llSpinnerDealDis.visibility = View.GONE
                //binding.llSpinnerType.visibility = View.GONE


            }
        }else if (role == "R") {
            binding.llSpinnerDealDis.visibility = View.GONE
            binding.llSpinnerType.visibility = View.GONE
            dealerId = SharedPreferencesClass.retriveData(context as Context, "UM_ID").toString()
            distribId = ""
            callApiCreditOutStand()


        }else if (role == "D") {
            binding.llSpinnerDealDis.visibility = View.GONE
            binding.llSpinnerType.visibility = View.GONE
            dealerId = ""
            distribId = SharedPreferencesClass.retriveData(context as Context, "UM_ID").toString()
            callApiCreditOutStand()


        } else {
            binding.llSpinnerDealDis.visibility = View.GONE
            binding.llSpinnerType.visibility = View.GONE
        }



        binding.spinnerType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (binding.spinnerType.selectedItem.toString().equals("Dealer")) {
                        binding.llDistrict.visibility=View.VISIBLE
                        binding.llSpinnerDealDis.visibility=View.GONE
                        binding.rlCredit.visibility = View.GONE
                        binding.btnPlaceOrder.visibility = View.GONE
                        callApiDistrict(userId)

                        showToastMessage("Select District")

                    }else if (binding.spinnerType.selectedItem.toString().equals("Distributor")) {
                        binding.llArea.visibility=View.GONE
                        binding.llDistrict.visibility=View.GONE
                        binding.llSpinnerDealDis.visibility = View.GONE
                        binding.rlCredit.visibility = View.GONE
                        binding.btnPlaceOrder.visibility = View.GONE


                        callDistApi()



                    } else {
                        binding.llArea.visibility=View.GONE
                        binding.llDistrict.visibility=View.GONE
                        binding.rlCredit.visibility = View.GONE
                        binding.btnPlaceOrder.visibility = View.GONE


                    }

                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }
        /*binding.spinnerPaymentType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (binding.spinnerType.selectedItem == "Cash") {
                        paymentType = "C"

                    } else {
                        paymentType = "Q"

                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }*/

        binding.spinnerDealDis.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (binding.spinnerType.selectedItem == "Dealer") {
                        if (binding.spinnerDealDis.selectedItem == "Select Dealer Name") {
                            binding.btnPlaceOrder.visibility = View.GONE
                            binding.rlCredit.visibility = View.GONE

                        } else {
                            val x = dealNameList[position - 1]
                            dealerId = x.UM_ID!!
                            distribId = ""
                            binding.btnPlaceOrder.visibility = View.GONE
                            binding.rlCredit.visibility = View.GONE

                            // showToastMessage("dealId" + dealerId)
                           // showToastMessage("distId" + distribId)
                            callApiCreditOutStand()
                        }
                        /*if (dealNameList.size > 0) {
                            val x = dealNameList[position]
                            dealerId = x.UM_ID!!
                            //showToastMessage("Dealer Id"+dealerId)
                            distribId = ""
                            //showToastMessage("Distributor Id"+distribId)
                            callApiCreditOutStand()

                        }*/

                    } else if (binding.spinnerType.selectedItem == "Distributor") {
                        if (binding.spinnerDealDis.selectedItem == "Select Distributor Name") {
                            binding.btnPlaceOrder.visibility = View.GONE
                            binding.rlCredit.visibility = View.GONE


                        } else {
                            val x = distNameList[position - 1]
                            distribId = x.UM_ID!!
                            dealerId = ""
                            binding.btnPlaceOrder.visibility = View.GONE
                            binding.rlCredit.visibility = View.GONE
                            //showToastMessage("dealId" + dealerId)
                            //showToastMessage("distId" + distribId)
                            callApiCreditOutStand()
                        }
                        /*if (distNameList.size > 0) {
                            val x = distNameList[position]
                            distribId = x.UM_ID!!
                            dealerId = ""
                            //showToastMessage("Distributor Id"+distribId)
                            //showToastMessage("Dealer Id"+dealerId)

                            callApiCreditOutStand()
                        }*/
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }

        binding.btnPlaceOrder.setOnClickListener {
            if (binding.llSpinnerDealDis.visibility==View.VISIBLE) {
                if (binding.spinnerDealDis.selectedItem.toString() == "Select Dealer Name") {
                    showToastMessage("Select Dealer Name")
                } else if (binding.spinnerDealDis.selectedItem.toString() == "Select Distributor Name") {
                    showToastMessage("Select Distributor Name")
                }else{
                    placeOrder()

                }
            }else {

                placeOrder()

            }
        }
    }
    private fun callApiDistrict(userId: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getDistrict(
            DealerDistrictParams(userId)
        )
        responseCall.enqueue(districtResponse as Callback<DistrictResponse>)

    }

    var districtList : List<DistrictDetails> = emptyList<DistrictDetails>()

    val districtResponse = object : NetworkCallBack<DistrictResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DistrictResponse>) {
            response.data?.status?.let { status ->

                hide()
                districtList  = response.data.DistrictList
                var statList1: MutableList<String> = ArrayList()
                statList1.add("Select District")

                var statList: MutableList<String> = ArrayList()
                Collections.sort(districtList,
                    Comparator { o1, o2 -> o1.DM_District_Name!!.compareTo(o2.DM_District_Name!!) })
                for (i in districtList){
                    //showToastMessage(i.toString())
                    statList.add(i.DM_District_Name!!)
                }
                // Collections.sort(statList, String.CASE_INSENSITIVE_ORDER);
                statList= (statList1+statList).toMutableList()
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }

                binding.spinnerDistrict.adapter = adapter2
                binding.llDistrict.visibility=View.VISIBLE

                binding.spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (binding.spinnerDistrict.selectedItem == "Select District") {
                          /*  SharedPreferencesClass.insertData(
                                context as Context,
                                "district_name","Select District")*/
                            binding.llSpinnerDealDis.visibility=View.GONE
                            binding.llArea.visibility=View.GONE

                        } else {
                            val x = districtList[position-1]
                            districtValue = x.DM_ID!!
                           /* SharedPreferencesClass.insertData(
                                context as Context,
                                "district_name",x.DM_District_Name)*/
                            binding.llSpinnerDealDis.visibility=View.GONE
                            callApiArea(userId, districtValue)

                            //showToastMessage(x.AM_ID)

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

    private fun callApiArea(userId: String, districtId: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getArea(
            DealerAreaParams(userId, districtId)
        )
        responseCall.enqueue(areaResponse as Callback<AreaResponse>)

    }

    var areaList: List<AreaDetails> = emptyList<AreaDetails>()

    val areaResponse = object : NetworkCallBack<AreaResponse>() {
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AreaResponse>) {
            response.data?.status?.let { status ->

                hide()
                areaList = response.data.AreaList
                var statList1: MutableList<String> = java.util.ArrayList()
                statList1.add("Select Area")
                var statList: MutableList<String> = java.util.ArrayList()
                Collections.sort(areaList,
                    Comparator { o1, o2 -> o1.AM_Area_Name!!.compareTo(o2.AM_Area_Name!!) })
                for (i in areaList){
                    //showToastMessage(i.toString())
                    statList.add(i.AM_Area_Name!!)
                }
                statList = (statList1 + statList).toMutableList()
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerArea.adapter = adapter2
                binding.llArea.visibility=View.VISIBLE

                binding.spinnerArea.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerArea.selectedItem == "Select Area") {

                            } else {
                                val x = areaList[position - 1]
                                areaValue = x.AM_ID!!
                                binding.rlCredit.visibility = View.GONE
                                binding.btnPlaceOrder.visibility = View.GONE
                                callDealApi(userId, areaValue)
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
    private fun placeOrder() {
        showHud()
        orderProductList = ArrayList<OrderDetailsParams>()
        for (i in checkoutItemsList!!.indices) {
            orderProductList!!.add(
                OrderDetailsParams(
                    checkoutItemsList!![i].cart_product_id!!,
                    checkoutItemsList!![i].cart_product_scheme_id!!,
                    checkoutItemsList!![i].cart_product_quantity!!,
                    checkoutItemsList!![i].cart_product_quantity_type!!,
                    checkoutItemsList!![i].cart_product_mrp_price!!,
                    checkoutItemsList!![i].cart_product_disc_price!!,
                    checkoutItemsList!![i].cart_product_net_price!!,
                    checkoutItemsList!![i].cart_product_gst!!,
                    checkoutItemsList!![i].cart_product_total_price!!,
                    checkoutItemsList!![i].cart_product_ltr!!
                )
            )

        }
        /*showToastMessage("dealerId"+dealerId)
        showToastMessage("taskId"+taskId)
        showToastMessage("distId"+distribId)
        showToastMessage("paymentType"+paymentType)
        showToastMessage("List"+ orderProductList!!.toList())*/
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val model = CreateOrderPRParams(
            userId,
            taskId,
            dealerId,
            distribId,
            today_date.toString(),
            paymentType,
            orderProductList!!.toList()

        )
        val responseCall = apiInterface.createOrder(
            model
        )
        responseCall.enqueue(CreateOrderDetailsResponse as Callback<CreateOrderResponse>)
    }

    private fun callApiCreditOutStand() {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getCreditOutStandingList(

            CreditDaysOutStandingRequestParams(userId, dealerId, distribId)
        )
        responseCall.enqueue(creditDaysOutStandingResponse as Callback<CreditDaysOutStandingResponse>)
    }


    private val creditDaysOutStandingResponse =
        object : NetworkCallBack<CreditDaysOutStandingResponse>() {
            @SuppressLint("LogNotTimber", "SetTextI18n")
            override fun onSuccessNetwork(
                data: Any?,
                response: NetworkResponse<CreditDaysOutStandingResponse>
            ) {

                hide()
                Log.e("Credit", "onSuccessNetwork: " + response.data)
                //showToastMessage("response="+response.data)
                if (response.data!!.status == 1) {
                    binding.rlCredit.visibility = View.VISIBLE
                    binding.tvCreditDay.text = "Credit Limit Days : " + response.data!!.Creadit_Days
                    binding.tvOutstandAmt.text =
                        "Outstanding Amount : " + response.data.totalOutStanding
                } else {
                    binding.rlCredit.visibility = View.VISIBLE
                    binding.tvCreditDay.text = "Credit Limit Days : " + response.data!!.Creadit_Days
                    binding.tvOutstandAmt.text =
                        "Outstanding Amount : " + response.data.totalOutStanding
                }
                binding.btnPlaceOrder.visibility=View.VISIBLE


            }

            override fun onFailureNetwork(data: Any?, error: NetworkError) {
                // hide()
            }

        }

    var hud: KProgressHUD? = null
    fun showHud() {
        hud = KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();
    }

    fun hide() {
        hud?.dismiss()
    }

    private var distNameList: List<DistDropdownDetails> = emptyList<DistDropdownDetails>()
    private val distNameResponse = object : NetworkCallBack<DistListResponse>() {
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DistListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DistList.toString())
                hide()
                distNameList = response.data.DistList
                var statList1: MutableList<String> = ArrayList()
                statList1.add("Select Distributor Name")
                var statList: MutableList<String> = ArrayList()
                Collections.sort(distNameList,
                    Comparator { o1, o2 -> o1.UM_Name!!.compareTo(o2.UM_Name!!) })
                for (i in distNameList){
                    //showToastMessage(i.toString())
                    statList.add(i.UM_Name!!)
                }
                statList = (statList1 + statList).toMutableList()
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }

                if (binding.spinnerType.selectedItem == "Distributor") {
                    binding.spinnerDealDis.adapter = adapter2
                }
                binding.llSpinnerDealDis.visibility = View.VISIBLE
                binding.rlCredit.visibility = View.GONE

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDistApi() {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.distDropDownList(
            DistListRequestParams(userId)
        )

        responseCall.enqueue(distNameResponse as Callback<DistListResponse>)

    }


    private var dealNameList: List<DealDropdownDetails> = emptyList<DealDropdownDetails>()
    private val dealNameResponse = object : NetworkCallBack<DealListResponse>() {
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DealListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DealList.toString())
                hide()
                dealNameList = response.data.DealList
                var statList1: MutableList<String> = ArrayList()
                statList1.add("Select Dealer Name")
                var statList: MutableList<String> = ArrayList()
                Collections.sort(dealNameList,
                    Comparator { o1, o2 -> o1.UM_Name!!.compareTo(o2.UM_Name!!) })
                for (i in dealNameList){
                    //showToastMessage(i.toString())
                    statList.add(i.UM_Name!!)
                }
                statList = (statList1 + statList).toMutableList()
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                /*if (binding.spinnerType.selectedItem == "Dealer") {
                    binding.spinnerDealDis.adapter = adapter2
                }*/
                binding.llSpinnerDealDis.visibility = View.VISIBLE
                binding.spinnerDealDis.adapter = adapter2
                binding.rlCredit.visibility = View.GONE
                binding.btnPlaceOrder.visibility = View.GONE

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDealApi(userId: String , areaId: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(userId, areaId)
        )

        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

    }

    private val CreateOrderDetailsResponse = object : NetworkCallBack<CreateOrderResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<CreateOrderResponse>
        ) {
            hide()
            if (response.data!!.status == 1) {
                Toast.makeText(context, response.data.respMessage, Toast.LENGTH_LONG).show()
                moveToOrderList()
            } else {
                Toast.makeText(context, response.data.respMessage, Toast.LENGTH_LONG).show()

            }
            /*response.data?.status?.let { status ->

            }*/

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }


    }

    private fun moveToOrderList() {

        if (RBMLubricantsApplication.fromBeat == "Beat") {
            DataConstant.dealerId = dealerId
            DataConstant.distributorId = distribId
            DataConstant.taskId = taskId
            val navDirection =
                OrderCheckOutFragmentDirections.actionOrderCheckOutFragmentToBeatTaskDealerDetailsFragment(
                    taskDetails
                )
            Navigation.findNavController(binding.btnPlaceOrder).navigate(navDirection)

        } else {
            val navDirection =
                OrderCheckOutFragmentDirections.actionOrderCheckOutFragmentToOrderListFragment(
                    taskDetails
                )
            Navigation.findNavController(binding.btnPlaceOrder).navigate(navDirection)

            db!!.clearCart()
        }
    }
}