package com.velectico.rbm.beats.views

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.beats.viewmodel.BeatSharedViewModel
import com.velectico.rbm.databinding.FragmentAssignBeatToLocationBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class AssignBeatToLocation : BaseFragment() {

    private lateinit var binding: FragmentAssignBeatToLocationBinding
    private lateinit var mBeatSharedViewModel: BeatSharedViewModel
    var startDate = ""
    var endDate = ""
   // var userId=""
        companion object{

        var source:String = ""
        var areaList:String = ""
        var taskLevel:String = ""
        var personId:String = ""

    }
    override fun getLayout(): Int {
        return R.layout.fragment_assign_beat_to_location
    }

    override fun init(binding: ViewDataBinding) {
        //showToastMessage(GloblalDataRepository.getInstance().scheduleId)
        this.binding = binding as FragmentAssignBeatToLocationBinding

        /*if (RBMLubricantsApplication.globalRole == "Team" ){
            userId = GloblalDataRepository.getInstance().teamUserId
        }else{
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)

        }*/
        //showToastMessage("UserId"+userId)

        startDate = arguments?.getString(  "startDate").toString()
        endDate = arguments?.getString(  "endDate").toString()
        binding.btnAssignTaskToLocation.setOnClickListener {
            moveToBeatTarget()
        }
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate =  DateUtils.parseDate(startDate,inpFormat,outputformat)
        val endate =  DateUtils.parseDate(endDate,inpFormat,outputformat)

        binding.tvBeatScheduleName.text = "Add Beat Task (" +stdate +" to " + endate +")"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        mBeatSharedViewModel = BeatSharedViewModel.getInstance(baseActivity)
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)

       /* var provinceList: MutableList<String> = ArrayList()
        if (DataConstant.beat_level=="Region"){
            provinceList.add("Region")
            provinceList.add("Zone")
            provinceList.add("District")
            provinceList.add("Area")
        }else  if (DataConstant.beat_level=="Zone"){
            provinceList.add("Zone")
            provinceList.add("District")
            provinceList.add("Area")
        }else  if (DataConstant.beat_level=="District"){
            provinceList.add("District")
            provinceList.add("Area")
        }else{
            provinceList.add("Area")

        }*/

        callApi()


    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callApi()
       // binding.spBeatName.setItem(provinceList)
    }*/

    fun callApi(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        //showToastMessage( "Id"+GloblalDataRepository.getInstance().scheduleId)
        val responseCall = apiInterface.getTaskForList(
            TaskForListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context), GloblalDataRepository.getInstance().scheduleId)
        )
        responseCall.enqueue(orderVSQualityResponseResponse as Callback<TaskForListResponse>)

    }
    private  var dataList : List<TaskForList> = emptyList<TaskForList>()
    private val orderVSQualityResponseResponse = object : NetworkCallBack<TaskForListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<TaskForListResponse>) {
            response.data?.status?.let { status ->

                hide()
                if ((response.data.count) > 0){
                    source = response.data.source!!
                    areaList = response.data.areaList!!
                    dataList = response.data.TaskForList
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Select Task For")
                    for (i in response.data.TaskForList) {
                        statList.add(i.respValue!!)
                    }

                    // binding.spBeatName.setItem(statList)
                    //binding.spBeatName.setSelection(0)

                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.spinner.adapter = null

                    binding.spinner.adapter = adapter2
                    binding.spinner.setSelection(0, false)
                    binding.spinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding.spinner.selectedItem.toString()=="Select Task For"){
                                   // showToastMessage("Select Task For")
                                }else{
                                   // showToastMessage("taskLevel"+ taskLevel)
                                    //binding.spinner.tag = 1
                                    //binding.etHigherLocation.setText(dataList[position].respValue)
                                    taskLevel = dataList[position-1].taskLevel!!
                                    Handler().postDelayed({
                                        callApi2(dataList[position-1].taskLevel!!)
                                    }, 1000)
                                }


                                //   if ( binding.spinner.tag == 0){

                                //    }


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }

                    //  taskLevel = dataList[0].taskLevel!!
                    // callApi2(dataList[0].taskLevel!!)
                }
                else{
                showToastMessage("No data found")
            }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callApi2(level:String){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
       // showToastMessage("Source"+ source)
       // showToastMessage("level"+ level)
        //showToastMessage("areaList"+ areaList)

        val responseCall = apiInterface.getLocationByLevelList(
            LocationByLevelListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),source,level,areaList)
        )
        responseCall.enqueue(getLocationByLevelListResponse as Callback<LocationByLevelListResponse>)

    }
    private  var locationList : List<LocationList> = emptyList<LocationList>()
    private val getLocationByLevelListResponse = object : NetworkCallBack<LocationByLevelListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<LocationByLevelListResponse>) {
            response.data?.status?.let { status ->

                hide()
                locationList.toMutableList().clear()
                locationList  = response.data.LocationList[0]
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Location")
                if (locationList.size>0) {
                    for (i in locationList) {
                        statList.add(i.locValue!!)
                    }

                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.spinner2.adapter = null
                    binding.spinner2.adapter = adapter2
                    binding.spinner2.setSelection(0, false)
                    binding.spinner2.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {

                                if (binding.spinner2.selectedItem.toString()=="Select Location"){
                                   // showToastMessage("Select Location")
                                }else{
                                   // showToastMessage("taskLevel"+ taskLevel)
                                   // showToastMessage("locId"+ areaList)

                                    //binding.spinner.tag = 1
                                    //binding.etHigherLocation.setText(dataList[position].respValue)
                                    areaList = locationList[position-1].locId!!
                                    /*Handler().postDelayed({
                                        callApi3(taskLevel, locationList[position-1].locId!!)
                                    }, 1000)*/
                                }

                                //binding.etHigherLocation.setText( locationList[position].locValue)


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }
                    //  callApi3(locationList)

                }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }


    /*fun callApi3(level:String,locId:String){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getAssignToList(
            AssignToListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),source,level,locId)
            //AssignToListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),"R","R","5")
        )
        responseCall.enqueue(getAssignToListResponse as Callback<AssignToListResponse>)

    }*/
    /*private  var personList : List<AssigntoList> = emptyList<AssigntoList>()
    private val getAssignToListResponse = object : NetworkCallBack<AssignToListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AssignToListResponse>) {
            response.data?.status?.let { status ->

                hide()
                if (response.data.AssignToList != null) {
                    personList = response.data.AssignToList
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Assign To")
                    if (personList.size > 0) {
                        for (i in personList) {
                            statList.add(i.UM_Name!!)
                        }
                        val adapter2 = context?.let {
                            ArrayAdapter(
                                it,
                                android.R.layout.simple_spinner_dropdown_item, statList
                            )
                        }
                        binding.spinner3.adapter = null
                        binding.spinner3.adapter = adapter2
                        binding.spinner3.setSelection(0, false)

                        binding.spinner3.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    adapterView: AdapterView<*>,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {

                                    if (binding.spinner3.selectedItem.toString()=="Assign To"){
                                        //showToastMessage("Select Location")
                                    }else{
                                       // sales_person_cum_lead_id
                                        DataConstant.salesPersonLeadId=personList[position-1].UM_ID!!
                                       // showToastMessage("SalePersonLeadId"+DataConstant.salesPersonLeadId)

                                        //binding.spinner.tag = 1
                                        //binding.etHigherLocation.setText(dataList[position].respValue)
                                        //areaList = personList[position-1].locId!!

                                    }

                                    //binding.etHigherLocation.setText( locationList[position].locValue)


                                }

                                override fun onNothingSelected(adapterView: AdapterView<*>) {}
                            }

                    }
                }
                else{
                    showToastMessage("No Data found")
                }
            }
        }
        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }*/

    private fun moveToBeatTarget(){

        if (binding.spinner.selectedItem.toString()=="Select Task For"){
            showToastMessage("Select Task For")
        }else if (binding.spinner2.selectedItem.toString()=="Select Location"){
            showToastMessage("Select Location")

        }/*else if (binding.spinner3.selectedItem.toString()=="Assign To"){
            showToastMessage("Select Assign To")

        }*/else{
            //showToastMessage("Success")
            val navDirection =  AssignBeatToLocationDirections.actionAssignBeatToLocationToAssignTargetToBeat(startDate,endDate)
            Navigation.findNavController(binding.btnAssignTaskToLocation).navigate(navDirection)
        }


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

}
