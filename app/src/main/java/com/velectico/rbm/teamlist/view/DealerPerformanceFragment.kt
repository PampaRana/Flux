package com.velectico.rbm.teamlist.view

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
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.DealDropdownDetails
import com.velectico.rbm.beats.model.DealListRequestParams
import com.velectico.rbm.beats.model.DealListResponse
import com.velectico.rbm.databinding.FragmentDealerPerformanceBinding
import com.velectico.rbm.dealer.model.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.teamlist.model.TeamListDealDistPerformanceResponse
import com.velectico.rbm.teamlist.model.TeamListPerformanceRequestParams
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.SharedPreferencesClass
import retrofit2.Callback
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class DealerPerformanceFragment : BaseFragment() {
    private lateinit var binding: FragmentDealerPerformanceBinding
    var dealerId = ""
    var distribId = ""
    var dateFormat: DateFormat? = null
    var date: Date? = null
    var today_date: String? = null
    var adapter2: ArrayAdapter<String>? = null
    var areaValue=""
    var userId=""
    var districtValue=""
    override fun getLayout(): Int {
        return R.layout.fragment_dealer_performance
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentDealerPerformanceBinding
        userId=SharedPreferenceUtils.getLoggedInUserId(context as Context)
        /*if (SharedPreferencesClass.retriveData(
                context as Context,
                "deal") != null) {
            val spinnerPosition: Int = adapter2!!.getPosition(SharedPreferencesClass.retriveData(
                context as Context,
                "deal"))
            binding.spinnerType.setSelection(spinnerPosition)
        }*/
        binding.spinnerType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (binding.spinnerType.selectedItem.toString().equals("Dealer")) {
                        //callApiArea(SharedPreferenceUtils.getLoggedInUserId(context as Context))
                        callApiDistrict(userId)
                        showToastMessage("Select District")
                        SharedPreferencesClass.insertData(
                            context as Context,
                            "deal","Dealer"

                        );
                    }


                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
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
                            binding.rlPerform.visibility = View.GONE
                            SharedPreferencesClass.insertData(
                                context as Context,
                                "deal_name","Select Dealer Name"

                            );
                        } else {
                            val x = dealNameList[position - 1]
                            dealerId = x.UM_ID!!
                            distribId = ""
                            // binding.rlPerform.visibility = View.GONE
                             SharedPreferencesClass.insertData(
                                context as Context,
                                "deal_name",x.UM_Name

                            );
                            binding.rlPerform.visibility = View.GONE
                            callApiPerformance(x.UM_Login_Id.toString())

                        }

                    }

                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {
                    hide()
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
                /*if (SharedPreferencesClass.retriveData(
                        context as Context,
                        "district_name") != null) {
                    val spinnerPosition: Int = adapter2!!.getPosition(SharedPreferencesClass.retriveData(
                        context as Context,
                        "district_name"))
                    binding.spinnerDistrict.setSelection(spinnerPosition)
                }*/


                binding.spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (binding.spinnerDistrict.selectedItem == "Select District") {
                           /* SharedPreferencesClass.insertData(
                                context as Context,
                                "district_name","Select District")*/
                            binding.llArea.visibility=View.GONE
                            binding.llDistrict.visibility=View.GONE

                        } else {
                            val x = districtList[position-1]
                            districtValue = x.DM_ID!!
                           /* SharedPreferencesClass.insertData(
                                context as Context,
                                "district_name",x.DM_District_Name)*/
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
               /* if (SharedPreferencesClass.retriveData(
                        context as Context,
                        "area_name") != null) {
                    val spinnerPosition: Int = adapter2!!.getPosition(SharedPreferencesClass.retriveData(
                        context as Context,
                        "area_name"))
                    binding.spinnerArea.setSelection(spinnerPosition)
                }*/
                binding.spinnerArea.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerArea.selectedItem == "Select Area") {
                                areaValue=""
                               /* SharedPreferencesClass.insertData(
                                    context as Context,
                                    "area_name","Select Area")*/
                                binding.llSpinnerDealDis.visibility=View.GONE
                            } else {
                                val x = areaList[position - 1]
                                areaValue = x.AM_ID!!
                                /*SharedPreferencesClass.insertData(
                                    context as Context,
                                    "area_name",x.AM_Area_Name)*/
                                callDealApi()

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


    private val TeamListDealDistPerformanceResponse =
        object : NetworkCallBack<TeamListDealDistPerformanceResponse>() {
            @SuppressLint("LogNotTimber")
            override fun onSuccessNetwork(
                data: Any?,
                response: NetworkResponse<TeamListDealDistPerformanceResponse>
            ) {
                hide()
                binding.rlPerform.visibility = View.VISIBLE
                dateFormat = SimpleDateFormat("dd-MM-yyyy")
                date = Date()
                today_date = dateFormat!!.format(date)
                binding.tvTodayDate.text = "Till Today(" + today_date + ")"
                // Toast.makeText(context, response.data?.Performance_outOf_5, Toast.LENGTH_LONG).show()
                binding.speedViewDealDist.speedTo(response.data?.Performance_outOf_4!!.toFloat())
                Log.e("Performance", "onSuccessNetwork: " + response.data)
                binding.tvPerformMark.text = response.data.Performance_outOf_4

                binding.btnOrder.setOnClickListener {
                    val navDirection =
                        DealerPerformanceFragmentDirections.actionDealerPerformanceFragmentToOrderPerformFragment(
                            response.data.Sales_Order.Monthly.order_target.toString(),
                            response.data.Sales_Order.Monthly.order_target_achieve.toString(),
                            response.data.Sales_Order.Quaterly.order_target.toString(),
                            response.data.Sales_Order.Quaterly.order_target_achieve.toString(),
                            response.data.Sales_Order.Hly.order_target.toString(),
                            response.data.Sales_Order.Hly.order_target_achieve.toString(),
                            response.data.Sales_Order.Annually.order_target.toString(),
                            response.data.Sales_Order.Annually.order_target_achieve.toString()

                        )
                    Navigation.findNavController(binding.btnOrder).navigate(navDirection)


                }

                binding.btnPayment.setOnClickListener {
                    val navDirection =
                        DealerPerformanceFragmentDirections.actionDealerPerformanceFragmentToPaymentPerformFragment(
                            response.data.CollectionPerformance.Monthly.collection_target.toString(),
                            response.data.CollectionPerformance.Monthly.collection_target_achieve.toString(),
                            response.data.CollectionPerformance.Quaterly.collection_target.toString(),
                            response.data.CollectionPerformance.Quaterly.collection_target_achieve.toString(),
                            response.data.CollectionPerformance.Hly.collection_target.toString(),
                            response.data.CollectionPerformance.Hly.collection_target_achieve.toString(),
                            response.data.CollectionPerformance.Annually.collection_target.toString(),
                            response.data.CollectionPerformance.Annually.collection_target_achieve.toString()

                        )
                    Navigation.findNavController(binding.btnPayment).navigate(navDirection)


                }


            }

            override fun onFailureNetwork(data: Any?, error: NetworkError) {
                hide()
            }

        }

    fun callDealApi() {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),
            areaValue)
        )

        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

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
                //var statList: ArrayList<String> = ArrayList()
                Collections.sort(dealNameList,
                    Comparator { o1, o2 -> o1.UM_Name!!.compareTo(o2.UM_Name!!) })
                for (i in dealNameList){
                    //showToastMessage(i.toString())
                    statList.add(i.UM_Name!!)
                }
                binding.llSpinnerDealDis.visibility = View.VISIBLE
                statList = (statList1 + statList).toMutableList()
                adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerDealDis.adapter = adapter2
               /* if (SharedPreferencesClass.retriveData(
                        context as Context,
                        "deal_name") != null) {
                    val spinnerPosition: Int = adapter2!!.getPosition(SharedPreferencesClass.retriveData(
                        context as Context,
                        "deal_name"))
                    binding.spinnerDealDis.setSelection(spinnerPosition)
                }*/

                /*if (SharedPreferencesClass.retriveData(
                        context as Context,
                        "dealer_name") != null) {
                    binding.spinnerDealDis.setSelection(adapter2!!.getPosition(SharedPreferencesClass.retriveData(
                        context as Context,
                        "dealer_name")))

                } else {
                     adapter2 = context?.let {
                         ArrayAdapter(
                             it,
                             android.R.layout.simple_spinner_dropdown_item, statList
                         )
                     }
                    binding.spinnerDealDis.adapter = adapter2

                }*/



            }



    }

    override fun onFailureNetwork(data: Any?, error: NetworkError) {
        hide()
    }

}

    private fun callApiPerformance(umLoginId: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealerDistPerformance(

            TeamListPerformanceRequestParams(umLoginId)
        )
        responseCall.enqueue(TeamListDealDistPerformanceResponse as Callback<TeamListDealDistPerformanceResponse>)


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

}