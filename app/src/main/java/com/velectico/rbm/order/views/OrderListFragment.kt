package com.velectico.rbm.order.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentOrderListBinding
import com.velectico.rbm.databinding.RowOrderHeadListBinding
import com.velectico.rbm.dealer.model.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.adapters.OrderHeadListAdapter
import com.velectico.rbm.order.model.DistConfirmRequestParams
import com.velectico.rbm.order.model.DistConfirmResponse
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.SharedPreferencesClass
import retrofit2.Callback
import java.util.*


class OrderListFragment : BaseFragment() {
    private lateinit var binding: FragmentOrderListBinding
    private var orderHeadList: List<OrderListDetails> = emptyList()
    private lateinit var adapter: OrderHeadListAdapter
    var orderStatus = ""
    var taskDetails = BeatTaskDetails()
    var userId = ""
    var dealerId = ""
    var distribId = ""
    var role = ""
    var mechId = ""
    var areaValue=""
    var districtValue=""

    override fun getLayout(): Int {
        return R.layout.fragment_order_list
    }

    @SuppressLint("RestrictedApi")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderListBinding


        role = SharedPreferencesClass.retriveData(context as Context, "UM_Role").toString()

        if (role == "L") {
            if (RBMLubricantsApplication.globalRole == "Team") {
                binding.fab.visibility = View.GONE

                userId = GloblalDataRepository.getInstance().teamUserId
            } else {
                userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            }
            if (RBMLubricantsApplication.fromBeat == "Beat") {
                binding.llSpinnerDealDis.visibility = View.GONE
                binding.llSpinnerType.visibility = View.GONE
                dealerId = DataConstant.dealerId
                distribId = DataConstant.distributorId
                callApiOrderList()
            } else {
                binding.llSpinnerDealDis.visibility = View.GONE
                //binding.llSpinnerType.visibility = View.GONE
                callApiOrderList()


            }

        } else if (role == "P") {
            if (RBMLubricantsApplication.globalRole == "Team") {
                binding.fab.visibility = View.GONE

                userId = GloblalDataRepository.getInstance().teamUserId
            } else {
                userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            }
            if (RBMLubricantsApplication.fromBeat == "Beat") {
                binding.llSpinnerDealDis.visibility = View.GONE
                binding.llSpinnerType.visibility = View.GONE

                dealerId = DataConstant.dealerId
                distribId = DataConstant.distributorId
                callApiOrderList()
            } else {
                binding.llSpinnerDealDis.visibility = View.GONE
                //binding.llSpinnerType.visibility = View.GONE
                callApiOrderList()

            }

        } else if (role == "R") {

            binding.llSpinnerDealDis.visibility = View.GONE
            binding.llSpinnerType.visibility = View.GONE
            if (RBMLubricantsApplication.globalRole == "Team") {
                binding.fab.visibility = View.GONE

                userId = GloblalDataRepository.getInstance().teamUserId
            } else {
                userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            }
            dealerId = SharedPreferencesClass.retriveData(context as Context, "UM_ID").toString()
            distribId = ""
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)

            callApiDealDistOrderList(userId, role, orderStatus)

        } else if (role == "D") {

            binding.llSpinnerDealDis.visibility = View.GONE
            binding.llSpinnerType.visibility = View.GONE
            dealerId = ""
            distribId = SharedPreferencesClass.retriveData(context as Context, "UM_ID").toString()
            if (RBMLubricantsApplication.globalRole == "Team") {
                binding.fab.visibility = View.GONE

                userId = GloblalDataRepository.getInstance().teamUserId
            } else {
                userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            }
            callApiDealDistOrderList(userId, role, orderStatus)


        } else {
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            dealerId = ""
            distribId = ""
            mechId = SharedPreferencesClass.retriveData(context as Context, "UM_ID").toString()
            binding.llSpinnerDealDis.visibility = View.GONE
            binding.llSpinnerType.visibility = View.GONE
            callApiOrderList()

        }


        /* showToastMessage(RBMLubricantsApplication.fromBeat)
         showToastMessage("DeID"+taskDetails.dealerId.toString())
         showToastMessage("DstID"+taskDetails.distribId.toString())*/
        binding.spinnerType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (binding.spinnerType.selectedItem.toString().equals("Dealer")) {
                        binding.llDistrict.visibility=View.GONE
                        binding.llSpinnerDealDis.visibility=View.GONE
                        binding.rvOrderList.visibility = View.GONE
                        binding.tvNoData.visibility=View.GONE
                        callApiDistrict(userId)
                        showToastMessage("Select District")

                    }else if (binding.spinnerType.selectedItem.toString().equals("Distributor")) {
                        binding.llArea.visibility=View.GONE
                        binding.llDistrict.visibility=View.GONE
                        binding.llSpinnerDealDis.visibility = View.GONE
                        binding.rvOrderList.visibility = View.GONE
                        binding.tvNoData.visibility=View.GONE

                        callDistApi()



                    } else {
                        binding.llArea.visibility=View.GONE
                        binding.llDistrict.visibility=View.GONE
                        binding.rvOrderList.visibility = View.GONE
                        binding.tvNoData.visibility=View.GONE

                    }


                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }


        binding.fab.setOnClickListener {
            moveToCreateOrder()

        }

        binding.fabFilter.setOnClickListener {
            moveToFilterOrder()
        }
        binding.allButton.setOnClickListener {
            orderStatus = ""
            if (RBMLubricantsApplication.globalRole == "Team") {
                binding.fab.visibility = View.GONE

                userId = GloblalDataRepository.getInstance().teamUserId
            } else {
                userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            }
            if (role == "R" || role == "D") {

                callApiDealDistOrderList(userId, role, orderStatus)

            } else {
                callApiOrderList()
            }
        }
        binding.pendingButton.setOnClickListener {

            orderStatus = "O"
            if (RBMLubricantsApplication.globalRole == "Team") {
                binding.fab.visibility = View.GONE

                userId = GloblalDataRepository.getInstance().teamUserId
            } else {
                userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            }
            if (role == "R" || role == "D") {
                callApiDealDistOrderList(userId, role, orderStatus)

            } else {
                callApiOrderList()
            }
        }
        binding.completedButton.setOnClickListener {
            orderStatus = "C"
            if (RBMLubricantsApplication.globalRole == "Team") {
                binding.fab.visibility = View.GONE

                userId = GloblalDataRepository.getInstance().teamUserId
            } else {
                userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            }
            if (role == "R" || role == "D") {
                callApiDealDistOrderList(userId, role, orderStatus)

            } else {
                callApiOrderList()
            }
        }




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


                        } else {

                            val x = dealNameList[position - 1]
                            dealerId = x.UM_ID!!
                            distribId = ""
                            /*showToastMessage("dealId"+dealerId)
                            showToastMessage("distId"+distribId)*/
                            callApiOrderList()
                        }


                    } else if (binding.spinnerType.selectedItem == "Distributor") {
                        if (binding.spinnerDealDis.selectedItem == "Select Distributor Name") {

                        } else {
                            val x = distNameList[position - 1]
                            distribId = x.UM_ID!!
                            dealerId = ""
                            /*showToastMessage("dealId"+dealerId)
                            showToastMessage("distId"+distribId)*/
                            callApiOrderList()
                        }

                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }

    }

    private fun callApiDistrict(userId: String) {
       // showHud()
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
                            hide()
                           /* SharedPreferencesClass.insertData(
                                context as Context,
                                "district_name","Select District")*/
                            binding.llSpinnerDealDis.visibility=View.GONE
                            binding.llArea.visibility=View.GONE
                            binding.rvOrderList.visibility = View.GONE
                            binding.tvNoData.visibility = View.GONE
                        } else {
                            val x = districtList[position-1]
                            districtValue = x.DM_ID!!
                            /*SharedPreferencesClass.insertData(
                                context as Context,
                                "district_name",x.DM_District_Name)
                           */
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
    private fun callApiArea(userId: String, districtId:String) {
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
                                binding.rvOrderList.visibility = View.GONE
                                binding.tvNoData.visibility = View.GONE
                                binding.llSpinnerDealDis.visibility=View.GONE

                            } else {
                                val x = areaList[position - 1]
                                areaValue = x.AM_ID!!
                                binding.rvOrderList.visibility = View.GONE
                                binding.tvNoData.visibility=View.GONE
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
    private fun callApiDealDistOrderList(userId: String, role: String, orderStatus: String) {
        showHud()
        //showToastMessage(userId+"\n"+role+"\n"+orderStatus)
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getDealDistOrderList(
            DealDistOrderParams(userId, role, orderStatus)
        )
        responseCall.enqueue(OrderHistoryDetailsResponse as Callback<OrderHistoryDetailsResponse>)

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

    fun callApiOrderList() {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBeatAllOrderHistory(
            //BeatAllOrderListRequestParams("7001507620","61","0",orderStatus)
            BeatAllOrderListRequestParams(userId, dealerId, distribId, orderStatus)
        )
        responseCall.enqueue(OrderHistoryDetailsResponse as Callback<OrderHistoryDetailsResponse>)
    }

    private val OrderHistoryDetailsResponse =
        object : NetworkCallBack<OrderHistoryDetailsResponse>() {
            override fun onSuccessNetwork(
                data: Any?,
                response: NetworkResponse<OrderHistoryDetailsResponse>
            ) {
                hide()
                response.data?.status?.let { status ->
                    orderHeadList.toMutableList().clear()
                    if (response.data.count > 0) {
                        binding.tvNoData.visibility = View.GONE
                        orderHeadList = response.data.OrderList!!.toMutableList()
                        Collections.reverse(orderHeadList);

                        binding.rvOrderList.visibility = View.VISIBLE
                        setUpRecyclerView()
                    } else {
                        // showToastMessage("No data found")
                        binding.rvOrderList.visibility = View.GONE
                        binding.tvNoData.visibility = View.VISIBLE

                    }

                }

            }

            override fun onFailureNetwork(data: Any?, error: NetworkError) {
                // hide()
            }

        }


    private fun setUpRecyclerView() {
        //  adapter = OrderHeadListAdapter();
        adapter = OrderHeadListAdapter(object : OrderHeadListAdapter.IBeatDateListActionCallBack {
            override fun moveToOrderDetails(
                position: Int,
                beatTaskId: String?,
                binding: RowOrderHeadListBinding
            ) {
                Log.e("test", "onAddTask" + beatTaskId)

                val navDirection =
                    OrderListFragmentDirections.actionOrderListFragmentToOrderDetailsFragment2(
                        taskDetails,
                        orderHeadList[position]
                    )
                Navigation.findNavController(binding.card).navigate(navDirection)

            }

            override fun moveToOrderConfirm(
                adapterPosition: Int,
                beatTaskId: String,
                binding: RowOrderHeadListBinding
            ) {
                showHud()
                val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
                val responseCall = apiInterface.distConfirmOrder(
                    DistConfirmRequestParams(
                        userId,
                        orderHeadList[adapterPosition].OH_ID.toString()
                    )
                )

                responseCall.enqueue(distConfirmResponse as Callback<DistConfirmResponse>)
            }
        }, context as Context);
        binding.rvOrderList.adapter = adapter
        adapter.orderList = orderHeadList
    }

    private val distConfirmResponse = object : NetworkCallBack<DistConfirmResponse>() {
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DistConfirmResponse>) {

            hide()
            if (response.data!!.status == 1) {
                showToastMessage(response.data.respMessage)
                callApiDealDistOrderList(userId, role, orderStatus)
            } else {
                showToastMessage(response.data.respMessage)

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {

        }

    }

    private fun moveToCreateOrder() {
        val navDirection =
            OrderListFragmentDirections.actionOrderListFragmentToProductFilterFragment()
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }

    private fun moveToFilterOrder() {
        RBMLubricantsApplication.filterFrom = ""
        val navDirection = OrderListFragmentDirections.actionOrderListFragmentToOrderFilter()
        Navigation.findNavController(binding.fabFilter).navigate(navDirection)
    }


    private var distNameList: List<DistDropdownDetails> = emptyList<DistDropdownDetails>()
    private val distNameResponse = object : NetworkCallBack<DistListResponse>() {
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DistListResponse>) {
            hide()

            if (response.data!!.status == 1) {
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
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDistApi() {
        //showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.distDropDownList(
            DistListRequestParams(userId)
        )

        responseCall.enqueue(distNameResponse as Callback<DistListResponse>)

    }

    private var dealNameList: List<DealDropdownDetails> = emptyList<DealDropdownDetails>()
    private val dealNameResponse = object : NetworkCallBack<DealListResponse>() {
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DealListResponse>) {
            hide()

            if (response.data!!.status == 1) {
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
                if (binding.spinnerType.selectedItem == "Dealer") {
                    binding.spinnerDealDis.adapter = adapter2
                }
                binding.llSpinnerDealDis.visibility = View.VISIBLE
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDealApi(userId: String, areaId : String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(userId,areaId)
        )

        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

    }


}