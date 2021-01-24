package com.velectico.rbm.leave.view

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.DropdownDetails
import com.velectico.rbm.beats.model.OrderVSQualityRequestParams
import com.velectico.rbm.beats.model.OrderVSQualityResponse
import com.velectico.rbm.databinding.FragmentApplyLeaveBinding
import com.velectico.rbm.leave.model.ApplyLeaveRequest
import com.velectico.rbm.leave.model.ApplyLeaveResponse
import com.velectico.rbm.leave.model.ApproveRejectLeaveListRequest
import com.velectico.rbm.leave.model.LeaveReason
import com.velectico.rbm.leave.viewmodel.LeaveViewModel
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.loginreg.viewmodel.LoginViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by mymacbookpro on 2020-04-26
 * TODO: Add a class header comment!
 */
class ApplyLeaveFragment : BaseFragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private lateinit var binding:FragmentApplyLeaveBinding
    private lateinit var leaveViewModel: LeaveViewModel
    private var currentDatePicketParentView : TextInputEditText? = null
    private var leaveReasons: List<LeaveReason> = ArrayList<LeaveReason>()
    private var selectedLeaveReason : LeaveReason?=null
    private var flow : String?= null
    private var leaveID : String = ""
    var presentDay=""
    var absentDay=""
    var role=""
    private lateinit var loginViewModel: LoginViewModel


//    companion object{
//        var LeaveListModel:LeaveListModel? = null
//    }

    override fun getLayout(): Int {
        return R.layout.fragment_apply_leave
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentApplyLeaveBinding

        role= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Role").toString()

        //observeViewModelData()
        getRoleWiseView(role,binding)
        setUp()
        initHud()
        //getLeaveListFromServer()
        //showToastMessage(DataConstant.leaveStatus)
        //showToastMessage(RBMLubricantsApplication.globalRole)
        if (RBMLubricantsApplication.globalRole == "Team" ){
            if (DataConstant.leaveStatus=="R"){
                binding.rejectBtn.visibility = View.GONE
                binding.approveBtn.visibility = View.VISIBLE
            }else if (DataConstant.leaveStatus=="A"){
                binding.rejectBtn.visibility = View.VISIBLE
                binding.approveBtn.visibility = View.GONE

            }else{
                binding.rejectBtn.visibility = View.VISIBLE
                binding.approveBtn.visibility = View.VISIBLE
            }
            binding.applyBtn.visibility = View.GONE
            binding.leaveNameEt.isEnabled=false
            binding.leaveCommentsEt.isEnabled=false
            binding.leaveToEt.isEnabled=false
            binding.leaveFromEt.isEnabled=false
        }

        getLoginInfo()

    }

    private fun getLoginInfo() {
        loginViewModel = LoginViewModel.getInstance(activity as BaseActivity)

        loginViewModel.loginAPICall(SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Login_Id").toString(),
            SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Pass").toString())
        loginViewModel.userDataResponse.observe(this, Observer { listResponse ->
            listResponse?.let {
                //showToastMessage(getString(R.string.login_success))
                onLoginSuccess(it)
            }
        })
    }
    private fun onLoginSuccess(response: LoginResponse?) {
       // SharedPreferencesClass.insertData(activity as BaseActivity,"noOfDaysPresent",  response?.userDashboardDetails!!.noOfDaysPresent);
        //SharedPreferencesClass.insertData(activity as BaseActivity,"noOfDaysAbsent",  response?.userDashboardDetails!!.noOfDaysAbsent);
        //presentDay= SharedPreferencesClass.retriveData(activity as BaseActivity,"noOfDaysPresent").toString()
       // absentDay= SharedPreferencesClass.retriveData(activity as BaseActivity,"noOfDaysAbsent").toString()
        binding.tvProdCode.text = response?.userDashboardDetails!!.noOfDaysPresent.toString()
        binding.tvProdCat.text = response?.userDashboardDetails!!.noOfDaysAbsent.toString()
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
    fun initHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    override fun onResume() {
        super.onResume()
        callApi("Leave Reason")
    }

    fun callApi(type:String){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(segmentResponseResponse as Callback<OrderVSQualityResponse>)

    }

    var reasonId:String = ""
    var segdataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    val segmentResponseResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                segdataList  = response.data.BeatReportList
                var statList: MutableList<String> = java.util.ArrayList()
                for (i in segdataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                binding.spLeaveReason.adapter = adapter2
                binding.spLeaveReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (segdataList.size > 0 ){
                            val x = segdataList[position]
                            reasonId = x.Exp_Head_Id!!
                            binding.leaveNameEt.setText(x.Exp_Head_Name)

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
    fun callCreateApi(){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val userId = if (RBMLubricantsApplication.globalRole == "Team" ){
            GloblalDataRepository.getInstance().teamUserId
        }
        else{
            SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.createLeave(
            ApplyLeaveRequest(reasonId, userId,binding.leaveFromEt.text.toString(),binding.leaveToEt.text.toString(),binding.leaveCommentsEt.text.toString(),"")
        )
        responseCall.enqueue(createResponse as Callback<ApplyLeaveResponse>)

    }

    fun callACCEPEREJECT(status:String){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val userId = if (RBMLubricantsApplication.globalRole == "Team" ){
            GloblalDataRepository.getInstance().teamUserId
        }
        else{
            SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)

        val responseCall = apiInterface.accepeRejectLeave(
            ApproveRejectLeaveListRequest(userId,status,leaveID)
        )
        responseCall.enqueue(createResponse as Callback<ApplyLeaveResponse>)

    }



    fun callUpdateApi(){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val userId = if (RBMLubricantsApplication.globalRole == "Team" ){
            GloblalDataRepository.getInstance().teamUserId
        }
        else{
            SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.createLeave(
            ApplyLeaveRequest(reasonId, userId,binding.leaveFromEt.text.toString(),binding.leaveToEt.text.toString(),binding.leaveCommentsEt.text.toString(),"")
        )
        responseCall.enqueue(createResponse as Callback<ApplyLeaveResponse>)

    }


    val createResponse = object : NetworkCallBack<ApplyLeaveResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ApplyLeaveResponse>) {
            response.data?.status?.let { status ->

                hide()
              showToastMessage(response.data.respMessage)
                activity!!.onBackPressed()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }


    private fun setUp() {
        binding.leaveFromEt.setOnClickListener(this)
        binding.leaveToEt.setOnClickListener(this)
        binding.applyBtn.setOnClickListener(this)
        binding.approveBtn.setOnClickListener(this)
        binding.rejectBtn.setOnClickListener(this)

        binding.leaveNameEt.setOnClickListener {
            binding.spLeaveReason.performClick()
        }

        arguments?.let {
            flow = ApplyLeaveFragmentArgs.fromBundle(it).flow
            val position = ApplyLeaveFragmentArgs.fromBundle(it).item
            if(flow == LeaveListFragment.EDIT){
                val model = GloblalDataRepository.getInstance().leaveListModel
                leaveID = model.leaveID.toString()
                reasonId = model.leaveReasonId.toString()
                binding.leaveNameEt.setText(model.leaveReasonName)
                binding.leaveCommentsEt.setText(model.LD_Other_Reason)
                val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
                val outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
                val stdate =  DateUtils.parseDate(model.leaveFrom,inpFormat,outputformat)
                val endate =  DateUtils.parseDate(model.leaveTo,inpFormat,outputformat)
                binding.leaveFromEt.setText(stdate)
                binding.leaveToEt.setText(endate)
//                selectedLeaveReason = LeaveReason(
//                    DD_Dropdown_Val = leaveViewModel.leaveListData[position.toInt()].leaveName as String,
//                    DD_ID = leaveViewModel.leaveListData[position.toInt()].leaveReasonId)
            }
        }
    }

    private fun showCustomDatePicker(minStartDate:String = ""){
        var now = DateUtility.dateStrToCalendar(minStartDate)
        val year = now[Calendar.YEAR]
        val dpd: DatePickerDialog = DatePickerDialog.newInstance(
            this, year,  // Initial year selection
            now[Calendar.MONTH],  // Initial month selection
            now[Calendar.DAY_OF_MONTH] // Inital day selection
        )
        val calenderMaxDate = Calendar.getInstance()
        calenderMaxDate[Calendar.YEAR] = year + 1
        dpd.minDate = now
        dpd.maxDate = calenderMaxDate
        fragmentManager?.let {
            dpd.show(it, DATE_PICKER)
        }
        val holidays = arrayOf("20-05-2020", "21-05-2020", "25-05-2020")
        val disabledDays = DateUtility.getDisabledDatesArr(holidays).toTypedArray()
        dpd.highlightedDays = disabledDays
        dpd.disabledDays = disabledDays
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.leaveFromEt -> {
                currentDatePicketParentView = this.binding.leaveFromEt;
                showCustomDatePicker(binding.leaveFromEt.text.toString());
            }
            R.id.leaveToEt -> {
                currentDatePicketParentView = this.binding.leaveToEt;
                showCustomDatePicker(binding.leaveToEt.text.toString());
            }
            R.id.applyBtn -> {
                if(flow == LeaveListFragment.EDIT){
                    callUpdateApi()
                }else{
                    applyLeaveFromServer()
                }

            }
            R.id.approveBtn ->{

                callACCEPEREJECT("A")

            }
            R.id.rejectBtn ->{
                callACCEPEREJECT("R")


            }
        }
    }


    private fun observeViewModelData() {
        leaveViewModel = LeaveViewModel.getInstance(activity as BaseActivity)
        leaveViewModel.leaveReasonResponse.observe(viewLifecycleOwner, Observer { listResponse ->
            listResponse?.let {
                leaveReasons = listResponse.leaveReasons
                initLeaveTypeSpinner()
            }
        })

        leaveViewModel.applyLeaveResponse.observe(viewLifecycleOwner, Observer { listResponse ->
            listResponse?.let {
                showAlertDialog(listResponse.respMessage)
            }
        })

        leaveViewModel.loading.observe(viewLifecycleOwner, Observer { progress ->

            binding?.progressLayout?.visibility = if(progress) View.VISIBLE else View.GONE
        })

        leaveViewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            (activity as BaseActivity).showAlertDialog(it.errorMessage ?: getString(R.string.no_data_available))
        })
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, monthOfYear, dayOfMonth)
        val subDateString: String = DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.YYYY_DASH_MM_DASH_DD)
        currentDatePicketParentView?.setText(subDateString)
    }

    companion object{
        const val TAG = "ApplyLeaveFragment"
        const val DATE_PICKER = "DatePickerDialog"
    }

    private fun initLeaveTypeSpinner() {
        //https://github.com/Chivorns/SmartMaterialSpinner
        var leaveTypeListStringArray: MutableList<String> = ArrayList()
        for (leaveType in this.leaveReasons){
            leaveTypeListStringArray.add(leaveType.DD_Dropdown_Val)
        }
        binding.spLeaveReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                binding.leaveNameEt.setText( leaveTypeListStringArray[position])
                selectedLeaveReason = leaveReasons[position]
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        binding.spLeaveReason.setItem(leaveTypeListStringArray)
    }

    private fun applyLeaveFromServer(){
        val fromDate = binding.leaveFromEt.text.toString()?.trim();
        val toDate = binding.leaveToEt.text.toString()?.trim();
        if(reasonId.isEmpty()){
            showToastMessage("Please select leave reason")
        }
        else if(binding.leaveFromEt==null || fromDate == ""){
            showToastMessage("Please select from date")
        }
        else if(binding.leaveToEt==null || toDate == ""){
            showToastMessage("Please select to date")
        }
        else{

            if (fromDate.compareTo(toDate) > 0) {
                showToastMessage("From Date can not after To Date ")
                //println("Date 1 occurs after Date 2")


            } else if (fromDate.compareTo(toDate) < 0) {
               // showToastMessage("From Date occurs before To Date ")
                //showToastMessage("Date 1 occurs before Date 2")

                // println("Date 1 occurs before Date 2")
                binding?.progressLayout?.visibility = View.VISIBLE
                val userId : String = SharedPreferenceUtils.getLoggedInUserId(context as Context)
                val isEdit = flow == LeaveListFragment.EDIT
                callCreateApi()
            } else if (fromDate.compareTo(toDate) === 0) {
                //showToastMessage("Can not same date for leave")

               // println("Both dates are equal")

                binding?.progressLayout?.visibility = View.VISIBLE
                val userId : String = SharedPreferenceUtils.getLoggedInUserId(context as Context)
                val isEdit = flow == LeaveListFragment.EDIT
                callCreateApi()
            }



        }
    }

    private fun getLeaveListFromServer() {
        binding?.progressLayout?.visibility = View.VISIBLE
        leaveViewModel.getLeaveReasonAPICall()
    }

    private fun showAlertDialog(msg:String?){
        val dialog = AlertDialog.Builder(context as Context)
        dialog.setTitle(getString(R.string.alert_title))
            .setMessage(msg)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.ok_button)) {d, _ ->
                d.dismiss()
                activity?.onBackPressed()
            }
            .show()
    }

    private fun getRoleWiseView(uRole:String,binding: FragmentApplyLeaveBinding) {

        when (uRole) {

            SALES_LEAD_ROLE -> {

                binding.applyBtn.visibility = View.VISIBLE
            }
            SALES_PERSON_ROLE -> {

                binding.applyBtn.visibility = View.VISIBLE
            }



        }


    }
}