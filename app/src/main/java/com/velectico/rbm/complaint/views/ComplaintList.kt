package com.velectico.rbm.complaint.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.complaint.adapter.ComplaintListAdapter
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.complaint.model.ComplaintListRequestParams
import com.velectico.rbm.complaint.model.ComplaintListResponse
import com.velectico.rbm.databinding.FragmentComplaintListBinding
import com.velectico.rbm.databinding.RowComplaintListBinding
import com.velectico.rbm.dealer.model.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ComplaintList : BaseFragment() {

    private lateinit var binding: FragmentComplaintListBinding;
    private var complaintList : List<ComplainListDetails> = emptyList()
    private var complaintList11 = ComplainListDetails()
    private lateinit var adapter: ComplaintListAdapter
    var dealerId = ""
    var distribId = ""
    var mechId = ""
    var orderStatus = "O"
    var role=""
    var umId=""
    var areaValue=""
    var userId=""
    var districtValue=""
    var typestrings: ArrayList<String> = ArrayList()
    var adapterType: ArrayAdapter<String>? = null
    override fun getLayout(): Int {
        return R.layout.fragment_complaint_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentComplaintListBinding
        binding.fab.setOnClickListener {
            moveToCreateComplaint()
        }
        binding.expenseButton.setOnClickListener{
            orderStatus = "C"
            binding.rvBeatList.visibility = View.GONE
            binding.tvNoData.visibility = View.GONE
            callApiList()
            //setUpRecyclerView()
        }
        binding.beatButton.setOnClickListener{
            orderStatus = "O"
            binding.rvBeatList.visibility = View.GONE
            binding.tvNoData.visibility = View.GONE
            callApiList()
            //setUpRecyclerView()
        }
        role= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Role").toString()
        umId= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_ID").toString()
        userId=SharedPreferenceUtils.getLoggedInUserId(context as Context)
        if (role== SALES_PERSON_ROLE || role== SALES_LEAD_ROLE){
            dealerId = ""
            distribId=""
            mechId=""
            callApiList()

        }
        if(role == DEALER_ROLE){
            binding.llSpinnerDealDis.visibility = View.GONE
            binding.llSpinnerType.visibility = View.GONE
            dealerId = umId
            distribId=""
            mechId=""
            binding.rvBeatList.visibility = View.GONE
            binding.tvNoData.visibility = View.GONE
            callApiList()
            return
        }
        if(role == DISTRIBUTER_ROLE){
            binding.llSpinnerDealDis.visibility = View.GONE
            binding.llSpinnerType.visibility = View.GONE
            distribId = umId
            dealerId=""
            mechId=""
            binding.rvBeatList.visibility = View.GONE
            binding.tvNoData.visibility = View.GONE
            callApiList()
            return
        }
        if(role == MECHANIC_ROLE){
            binding.llSpinnerDealDis.visibility = View.GONE
            binding.llSpinnerType.visibility = View.GONE
            mechId = umId
            distribId=""
            dealerId=""
            binding.rvBeatList.visibility = View.GONE
            binding.tvNoData.visibility = View.GONE
            callApiList()
            return
        }
        /*typestrings.add("Select Type");
        typestrings.add("Dealer");
        typestrings.add("Distributor");
        val adapter2 = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item, typestrings)
        }
        binding.spinnerType.adapter = adapter2*/
        //showToastMessage(SharedPreferenceUtils.getLoggedInUserId(context as Context)+"\n"+dealerId+"\n"+distribId+"\n"+mechId+"\n"+orderStatus)

        // access the spinner
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
                        binding.rvBeatList.visibility = View.GONE
                        binding.tvNoData.visibility=View.GONE
                        callApiDistrict(userId)
                        showToastMessage("Select District")

                    }else if (binding.spinnerType.selectedItem.toString().equals("Distributor")) {
                        binding.llArea.visibility=View.GONE
                        binding.llDistrict.visibility=View.GONE
                        binding.llSpinnerDealDis.visibility = View.GONE
                        binding.rvBeatList.visibility = View.GONE
                        binding.tvNoData.visibility=View.GONE

                        callDistApi()



                    } else {
                        binding.llArea.visibility=View.GONE
                        binding.llDistrict.visibility=View.GONE
                        binding.rvBeatList.visibility = View.GONE
                        binding.tvNoData.visibility=View.GONE

                    }



                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }

        binding.spinnerDealDis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {

                if (binding.spinnerType.selectedItem == "Dealer"){
                    if (binding.spinnerDealDis.selectedItem=="Select Dealer Name"){
                        hide()
                        binding.rvBeatList.visibility = View.GONE
                        binding.tvNoData.visibility = View.GONE

                    }else{
                        val x = dealNameList[position-1]
                        dealerId = x.UM_ID!!
                        distribId=""
                        binding.rvBeatList.visibility = View.GONE
                        binding.tvNoData.visibility = View.GONE
                        //showToastMessage("dealId"+dealerId)
                        //showToastMessage("distId"+distribId)
                        /*SharedPreferencesClass.insertData(
                            context as Context,
                            "dealer_name",x.UM_Name)*/
                        callApiList()
                    }


                }
                else if (binding.spinnerType.selectedItem == "Distributor"){
                    if (binding.spinnerDealDis.selectedItem=="Select Distributor Name"){
                        hide()
                        binding.rvBeatList.visibility = View.GONE
                        binding.tvNoData.visibility = View.GONE

                    }else{
                        val x = distNameList[position-1]
                        distribId = x.UM_ID!!
                        dealerId=""
                        binding.rvBeatList.visibility = View.GONE
                        binding.tvNoData.visibility = View.GONE
                        /*SharedPreferencesClass.insertData(
                            context as Context,
                            "dist_name",x.UM_Name)*/
                        callApiList()
                    }

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }



        binding.fab.setOnClickListener {
            moveToCreateComplaint()
        }

        binding.fabFilter.setOnClickListener {
            moveToFilterComplaint()
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
                           /* SharedPreferencesClass.insertData(
                                context as Context,
                                "district_name","Select District")*/
                            hide()
                            binding.llSpinnerDealDis.visibility=View.GONE
                            binding.llArea.visibility=View.GONE
                            binding.rvBeatList.visibility = View.GONE
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
    private fun callApiArea(userId: String, districtId : String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getArea(
            DealerAreaParams(userId,districtId)
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

                /*if (SharedPreferencesClass.retriveData(
                        context as Context,
                        "area_name") != null) {
                    val spinnerPosition: Int = adapter2!!.getPosition(SharedPreferencesClass.retriveData(
                        context as Context,
                        "area_name"))
                    binding.spinnerArea.setSelection(spinnerPosition)
                }*/
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
                                /*SharedPreferencesClass.insertData(
                                    context as Context,
                                    "area_name","Select Area")*/
                                binding.rvBeatList.visibility = View.GONE
                                binding.tvNoData.visibility=View.GONE
                                binding.llSpinnerDealDis.visibility=View.GONE

                            } else {
                                val x = areaList[position - 1]
                                areaValue = x.AM_ID!!
                                /*SharedPreferencesClass.insertData(
                                    context as Context,
                                    "area_name",x.AM_Area_Name)*/
                                binding.rvBeatList.visibility = View.GONE
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
    private fun setUpRecyclerView() {


        adapter = ComplaintListAdapter(object : ComplaintListAdapter.IComplaintListActionCallBack{
            override fun moveToComplainDetails(position: Int, beatTaskId: String?,binding: RowComplaintListBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  ComplaintListDirections.actionComplaintListToCreateComplaintsUserWise(complaintList[position])
                Navigation.findNavController(binding.card).navigate(navDirection)
            }
        }, context as Context)
        binding.rvBeatList.adapter = adapter
        adapter.complaintList = complaintList;
    }

     private fun moveToCreateComplaint(){
      val navDirection =  ComplaintListDirections.actionComplaintListToCreateComplaintsUserWise(complaintList11)
         Navigation.findNavController(binding.fab).navigate(navDirection)
     }

    private fun moveToFilterComplaint(){
        val navDirection =  ComplaintListDirections.actionComplaintListToComplaintFilterFragment()
        Navigation.findNavController(binding.fabFilter).navigate(navDirection)
    }

    private  var distNameList : List<DistDropdownDetails> = emptyList<DistDropdownDetails>()
    private val distNameResponse = object : NetworkCallBack<DistListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DistListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DistList.toString())
                hide()
                distNameList  = response.data.DistList
                var statList1: MutableList<String> = ArrayList()
                statList1.add("Select Distributor Name")
                var statList: MutableList<String> = ArrayList()
                Collections.sort(distNameList,
                    Comparator { o1, o2 -> o1.UM_Name!!.compareTo(o2.UM_Name!!) })
                for (i in distNameList){
                    //showToastMessage(i.toString())
                    statList.add(i.UM_Name!!)
                }
               // Collections.sort(statList, String.CASE_INSENSITIVE_ORDER);
                statList = (statList1 + statList).toMutableList()
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                binding.spinnerDealDis.adapter = adapter2

                binding.llSpinnerDealDis.visibility=View.VISIBLE

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDistApi(){
       // showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.distDropDownList(
            DistListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context))
        )

        responseCall.enqueue(distNameResponse as Callback<DistListResponse>)

    }

    private  var dealNameList : List<DealDropdownDetails> = emptyList<DealDropdownDetails>()
    private val dealNameResponse = object : NetworkCallBack<DealListResponse>(){
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
                binding.spinnerDealDis.adapter = adapter2
                binding.llSpinnerDealDis.visibility=View.VISIBLE

            }



        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDealApi(userId: String, areaId: String){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(userId,areaId)
        )

        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

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

    fun callApiList(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getComplaintList(

            ComplaintListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),"0",dealerId,distribId,mechId,orderStatus)
        )
        responseCall.enqueue(ComplaintListResponse as Callback<ComplaintListResponse>)
    }

    private val ComplaintListResponse = object : NetworkCallBack<ComplaintListResponse>(){
        @SuppressLint("RestrictedApi")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ComplaintListResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test0000","OrderHistoryDetailsResponse status="+response.data)
                complaintList.toMutableList().clear()
                if (response.data.count > 0){
                    binding.rvBeatList.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
                    complaintList = response.data.ComplaintList!!.toMutableList()
                    Collections.reverse(complaintList);
                    setUpRecyclerView()
                    binding.fab.visibility = View.VISIBLE
                }
                else{
                    binding.rvBeatList.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                    //binding.resolvButton.visibility = View.GONE
                    //binding.pendButton.visibility = View.GONE
                    //binding.rvComplaintList.visibility = View.GONE
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }
}
