package com.velectico.rbm.payment.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TableRow
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentPaymentListFragmentBinding
import com.velectico.rbm.dealer.model.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.payment.adapter.PaymentOutStandAdapter
import com.velectico.rbm.payment.models.OutStandingResponse
import com.velectico.rbm.payment.models.PaymentHistoryRequestParams
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.SharedPreferencesClass
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentPaymentList : BaseFragment() {
    private lateinit var binding: FragmentPaymentListFragmentBinding;
    var paymentOutStandAdapter: PaymentOutStandAdapter? = null

    //private var paymentOutStandList: List<OutStandingInfoDetails> = ArrayList()
    var userId = ""
    var dealerId = ""
    var distribId = ""
    var mechId=""
    var role = ""
    var adapter2: ArrayAdapter<String>? = null
    var personName=""
    var mobile=""
    var areaValue=""
    var districtValue=""

    override fun getLayout(): Int {
        return R.layout.fragment_payment_list_fragment
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentPaymentListFragmentBinding

        binding.spinnerDeal.visibility = View.GONE

        role = SharedPreferencesClass.retriveData(context as Context, "UM_Role").toString()
        userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)


        if (role == "L") {

            //Sales Lead
           // binding.llSpinnerDealDis.visibility = View.VISIBLE
            binding.llSpinnerType.visibility = View.VISIBLE

        } else if (role == "P") {

            //Sales Person
            //binding.llSpinnerDealDis.visibility = View.VISIBLE
            binding.llSpinnerType.visibility = View.VISIBLE

        } else if (role == "R") {

            //Dealer
            binding.llSpinnerDealDis.visibility = View.GONE
            binding.llSpinnerType.visibility = View.GONE

            dealerId = SharedPreferencesClass.retriveData(context as Context, "UM_ID").toString()
            distribId = ""
            binding.card.visibility = View.GONE
            binding.rlName.visibility = View.GONE
            binding.tvNoData.visibility=View.GONE

            callOutStandApiList()

        } else if (role == "D") {
            //Distributor
            binding.llSpinnerDealDis.visibility = View.GONE
            binding.llSpinnerType.visibility = View.GONE

            dealerId = ""
            distribId = SharedPreferencesClass.retriveData(context as Context, "UM_ID").toString()
            binding.card.visibility = View.GONE
            binding.rlName.visibility = View.GONE
            binding.tvNoData.visibility=View.GONE

            callOutStandApiList()
            //showToastMessage ("dealId" + dealerId)
           // showToastMessage("distId" + distribId)

        } else {
            dealerId=""
            distribId=""
            mechId = SharedPreferencesClass.retriveData(context as Context, "UM_ID").toString()

            //Mechanic
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
                        binding.llDistrict.visibility=View.GONE
                        binding.llSpinnerDealDis.visibility=View.GONE
                        binding.card.visibility = View.GONE
                        binding.rlName.visibility = View.GONE
                        binding.tvNoData.visibility=View.GONE
                        callApiDistrict(userId)
                        showToastMessage("Select District")

                    }else if (binding.spinnerType.selectedItem.toString().equals("Distributor")) {
                        binding.llDistrict.visibility=View.GONE
                        binding.llArea.visibility=View.GONE
                        binding.llSpinnerDealDis.visibility = View.GONE
                        binding.card.visibility = View.GONE
                        binding.rlName.visibility = View.GONE
                        binding.tvNoData.visibility=View.GONE

                        callDistApi()



                    } else {
                        binding.llDistrict.visibility=View.GONE
                        binding.llArea.visibility=View.GONE
                        binding.card.visibility = View.GONE
                        binding.rlName.visibility = View.GONE
                        binding.tvNoData.visibility=View.GONE

                    }

                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }

        binding.tvDealerCallNo.setOnClickListener {
            callUser()
        }

       /* if (SharedPreferencesClass.retriveData(context as Context, "Spin_Type_Name")
                .toString() == "Dealer"
        ) {
            if (SharedPreferencesClass.retriveData(context as Context, "Spin_Name")
                    .toString() != null
            ) {
                binding.spinnerDealDis.setSelection(
                    adapter2!!.getPosition(
                        SharedPreferencesClass.retriveData(
                            context as Context,
                            "Spin_Name"
                        ).toString()
                    )
                );

            }
        } else {
            if (SharedPreferencesClass.retriveData(context as Context, "Spin_Name")
                    .toString() != null
            ) {
                binding.spinnerDealDis.setSelection(
                    adapter2!!.getPosition(
                        SharedPreferencesClass.retriveData(
                            context as Context,
                            "Spin_Name"
                        ).toString()
                    )
                );

            }
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

                        } else {
                            val x = dealNameList[position - 1]
                            dealerId = x.UM_ID!!
                            distribId = ""
                            personName=x.UM_Name!!
                            mobile=x.UM_Login_Id
                            SharedPreferencesClass.insertData(
                                context as Context,
                                "Spin_Type_Name",
                                "Dealer"
                            );
                            SharedPreferencesClass.insertData(
                                context as Context,
                                "Spin_Name",
                                x.UM_Name
                            );

                           // showToastMessage("dealId" + dealerId)
                           // showToastMessage("distId" + distribId)
                            binding.rlName.visibility = View.GONE
                            binding.card.visibility = View.GONE
                            binding.tvNoData.visibility=View.GONE
                            callOutStandApiList()
                        }


                    } else if (binding.spinnerType.selectedItem == "Distributor") {
                        if (binding.spinnerDealDis.selectedItem == "Select Distributor Name") {

                        } else {
                            val x = distNameList[position - 1]
                            distribId = x.UM_ID!!
                            dealerId = ""
                            personName=x.UM_Name!!
                            mobile=x.UM_Login_Id
                            SharedPreferencesClass.insertData(
                                context as Context,
                                "Spin_Type_Name",
                                "Distributor"
                            );
                            SharedPreferencesClass.insertData(
                                context as Context,
                                "Spin_Name",
                                x.UM_Name
                            );
                            binding.rlName.visibility = View.GONE
                            binding.card.visibility = View.GONE
                            binding.tvNoData.visibility=View.GONE

                            //showToastMessage("dealId" + dealerId)
                            //showToastMessage("distId" + distribId)
                            callOutStandApiList()
                        }

                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }

        binding.paymenthistory1.setOnClickListener {
            if(binding.llSpinnerDealDis.visibility==View.VISIBLE){
                if (binding.spinnerDealDis.selectedItem=="Select Dealer Name" ||
                        binding.spinnerDealDis.selectedItem=="Select Distributor Name"){

                    showToastMessage("Please select anyone")
                }else{
                    val navDirection = FragmentPaymentListDirections.actionFragmentPaymentListToPaymentHistoryFragment()
                    Navigation.findNavController(binding.paymenthistory1).navigate(navDirection)
                }

            }else {
                val navDirection = FragmentPaymentListDirections.actionFragmentPaymentListToPaymentHistoryFragment()
                Navigation.findNavController(binding.paymenthistory1).navigate(navDirection)
            }
        }
        binding.paybtn1.setOnClickListener {
            val navDirection = FragmentPaymentListDirections.actionFragmentPaymentListToPaymentPayFragment(
                    userId, dealerId, distribId
                )
            Navigation.findNavController(binding.paybtn1).navigate(navDirection)
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
                            binding.llSpinnerDealDis.visibility=View.GONE
                            binding.llArea.visibility=View.GONE
                            binding.card.visibility = View.GONE
                            binding.rlName.visibility = View.GONE
                            binding.tvNoData.visibility=View.GONE
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
                                binding.card.visibility = View.GONE
                                binding.rlName.visibility = View.GONE
                                binding.tvNoData.visibility=View.GONE
                                binding.llSpinnerDealDis.visibility=View.GONE

                            } else {
                                val x = areaList[position - 1]
                                areaValue = x.AM_ID!!
                                binding.rlName.visibility = View.GONE
                                binding.card.visibility = View.GONE
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
    private fun callUser(){
        val u = Uri.parse("tel:" + binding.tvDealerCallNo.text.toString())

        val i = Intent(Intent.ACTION_DIAL, u)

        try {
            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            startActivity(i)
        } catch (s: SecurityException) {
            // show() method display the toast with
            // exception message.
            Log.e("Error::","error while opening call");
        }

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
                adapter2 = context?.let {
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
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DealList.toString())
                hide()
                dealNameList = response.data.DealList
                var statList1: MutableList<String> = java.util.ArrayList()
                statList1.add("Select Dealer Name")
                var statList: MutableList<String> = ArrayList()
                Collections.sort(dealNameList,
                    Comparator { o1, o2 -> o1.UM_Name!!.compareTo(o2.UM_Name!!) })
                for (i in dealNameList){
                    //showToastMessage(i.toString())
                    statList.add(i.UM_Name!!)
                }
                statList = (statList1 + statList).toMutableList()
                adapter2 = context?.let {
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


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDealApi(userId: String, areaId: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(userId,areaId)
        )
        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

    }


    private fun callOutStandApiList() {
        showHud()
        DataConstant.dealerId = dealerId
        DataConstant.distributorId = distribId
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getPaymentOutStandingList(
            PaymentHistoryRequestParams(userId, dealerId, distribId)
        )
        responseCall.enqueue(paymentOutStandingResponse as Callback<OutStandingResponse>)

    }


    private val paymentOutStandingResponse = object : NetworkCallBack<OutStandingResponse>() {
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OutStandingResponse>) {
            hide()
            response.data?.status?.let { status ->
                //paymentOutStandList.toMutableList().clear()
                binding.paymenthistory1.isEnabled=true
                if (response.data.count > 0) {
                    binding.card.visibility = View.VISIBLE
                    binding.rlName.visibility = View.VISIBLE

                    binding.tvNoData.visibility = View.GONE
                    binding.tvDealerName.text=personName
                    binding.tvDealerCallNo.text=mobile
                    binding.totalamt.text = "Total Outstanding : ₹" + response.data.totalOutStanding
                    //paymentOutStandList.toMutableList().clear()
                    //paymentOutStandList = response.data.paymentOutStandingList.toMutableList()
                    response.data.paymentOutStandingList.toMutableList().clear()

                    binding.mTableLayout.removeAllViews()
                    binding.mTableLayout.addView(View(context))
                    val tableRowHeader = layoutInflater.inflate(
                        R.layout.payment_table_header,
                        null
                    ) as TableRow
                    binding.mTableLayout.addView(tableRowHeader)
                    binding.mTableLayout.setVisibility(View.VISIBLE)
                    for (i in response.data.paymentOutStandingList) {

                        val tableRow = layoutInflater.inflate(
                            R.layout.payment_table_item,
                            null
                        ) as TableRow
                        val tv_date_value = tableRow.findViewById(R.id.tv_date_value) as TextView
                        val tv_invoice_no_value =
                            tableRow.findViewById(R.id.tv_invoice_no_value) as TextView
                        val tv_amount_value =
                            tableRow.findViewById(R.id.tv_amount_value) as TextView

                        tv_amount_value.text = "₹ " + i.SIH_Invoice_Amt
                        val inpFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        val outputformat = SimpleDateFormat("dd-MMM-yy", Locale.US);
                        val stdate = DateUtils.parseDate(i.invoiceDate, inpFormat, outputformat)
                        tv_date_value.text = stdate
                        tv_invoice_no_value.text = i.SIH_Invoice_No
                        binding.mTableLayout.addView(tableRow);

                    }


                    /*paymentOutStandList = response.data.paymentOutStandingList.toMutableList()
                    paymentOutStandAdapter = PaymentOutStandAdapter(
                        context,
                        paymentOutStandList
                    )
                    binding.rvPaymentList.adapter=paymentOutStandAdapter;*/


                } else {
                    binding.card.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.rlName.visibility = View.VISIBLE
                    binding.tvDealerName.text=personName
                    binding.tvDealerCallNo.text=mobile
                }

            }

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


}
