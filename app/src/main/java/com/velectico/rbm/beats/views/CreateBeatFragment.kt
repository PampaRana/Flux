package com.velectico.rbm.beats.views

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.beats.viewmodel.BeatSharedViewModel
import com.velectico.rbm.databinding.FragmentCreateBeatBinding
import com.velectico.rbm.leave.view.ApplyLeaveFragment
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

/**
 * UI to create Beats
 */
class CreateBeatFragment : BaseFragment(), com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private lateinit var binding: FragmentCreateBeatBinding;
    private var currentDatePicketParentView : TextInputEditText? = null

    private lateinit var mBeatSharedViewModel: BeatSharedViewModel
    private lateinit var mAssignments: MutableList<BeatAssignments>;
    var beatLevel = ""
    var masterId = ""
    var userId=""



    override fun getLayout(): Int {
        return R.layout.fragment_create_beat
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentCreateBeatBinding
        if (RBMLubricantsApplication.globalRole == "Team") {

            userId = GloblalDataRepository.getInstance().teamUserId
        } else {
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }

       /* showToastMessage("userUmId"+SharedPreferencesClass.retriveData(context as Context,"UM_ID"))
        showToastMessage("userLoginId"+SharedPreferencesClass.retriveData(context as Context,"UM_Login_Id"))
        showToastMessage("userRole"+SharedPreferencesClass.retriveData(context as Context,"UM_Role"))*/
        /* showToastMessage("UserId"+userId)
         showToastMessage("UserId"+GloblalDataRepository.getInstance().teamUserId)*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBeatSharedViewModel = BeatSharedViewModel.getInstance(baseActivity)
        mBeatSharedViewModel.setBeat(Beats())
        mBeatSharedViewModel.beats.value?.beatAssignments = BeatAssignments().getBlankList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListeners();
        initSpinner();

    }

    fun addListeners() {
        //et_beat_name
        this.binding.etBeatName.setOnClickListener {
            this.binding.spBeatName.performClick()
        }
        this.binding.etAssignTo.setOnClickListener {
            if (binding.etAssignTo.text.toString()=="Beat Name") {

            }else{
                this.binding.spSalesperson.performClick()
            }
        }
        this.binding.etStartDate.setOnClickListener {
            currentDatePicketParentView = this.binding.etStartDate;
            showCustomDatePicker(binding.etStartDate.text.toString());
           // showDatePickerDialog();
        }
        this.binding.etEndDate.setOnClickListener {
            currentDatePicketParentView = this.binding.etEndDate;
            showCustomDatePicker(binding.etEndDate.text.toString());

            //showDatePickerDialog();
        }
        this.binding.btnAssignTask.setOnClickListener {
            var errMsg = mBeatSharedViewModel.beats.value?.isValidaData()
            val startDate = binding.etStartDate.text.toString()?.trim();
            val endDate = binding.etEndDate.text.toString()?.trim();
            if (binding.etBeatName.text.toString()=="Beat Level"){
                showToastMessage("Select Beat Level")
            }else if (binding.etAssignTo.text.toString()=="Beat Name"){
                showToastMessage("Select Beat Name")
            }else if(binding.etStartDate==null || startDate == ""){
                showToastMessage("Please select Start date")
            }
            else if(binding.etEndDate==null || endDate == ""){
                showToastMessage("Please select End date")
            }/*else if (errMsg != null) {
                showToastMessage(errMsg)
            }*/ else {
                if (startDate.compareTo(endDate) > 0) {
                    showToastMessage("Start Date can not after End Date ")
                    //println("Date 1 occurs after Date 2")
                }else {
                    //showToastMessage("Success")

                     callSubmit()
                }

            }

        }

    }

    //callback function to set set once date is selected from datepicker
    /*override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, month, dayOfMonth)
        val subDateString: String =
            DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.dd_MM_yy)
        cuurentDatePicketParentView?.setText(subDateString)
        if (cuurentDatePicketParentView == binding.etStartDate) {
            mBeatSharedViewModel.beats.value?.startDate = tempDate.time
        } else if (cuurentDatePicketParentView == binding.etEndDate) {
            mBeatSharedViewModel.beats.value?.endDate = tempDate.time
        }
    }*/

    //Function to show date picker dialog box for start and end date
    /*fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        DatePickerDialog(requireActivity(), this, year, month, dayOfMonth).show()
    }*/

    companion object{
        const val TAG = "CreateBeatFragment"
        const val DATE_PICKER = "DatePickerDialog"
    }
    var hud: KProgressHUD? = null
    fun showHud() {
        if (hud != null) {

            hud!!.show()
        }
    }

    fun hide() {
        hud?.dismiss()

    }

    private fun initSpinner() {
        hud = KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
        //https://github.com/Chivorns/SmartMaterialSpinner
        var provinceList: MutableList<String> = ArrayList()
        provinceList.add("Region")
        provinceList.add("Zone")
        provinceList.add("District")
        provinceList.add("Area")



        binding.spSalesperson.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.etAssignTo.setText(dataList[position].BM_Beat_Name)
                mBeatSharedViewModel.beats.value?.salesPersonId = position.toString()
                mBeatSharedViewModel.beats.value?.salesPersonName = dataList[position].BM_Beat_Name
                masterId = dataList[position].BM_ID!!
                //DataConstant.beatScheduleId
                GloblalDataRepository.getInstance().scheduleId = dataList[position].BM_ID!!

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        binding.spBeatName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.etBeatName.setText(provinceList[position])
                beatLevel = provinceList[position].get(0).toString().toUpperCase()
               // showToastMessage("beatLavel"+beatLevel)
                callApi(type = beatLevel)
                mBeatSharedViewModel.beats.value?.beatId = position.toString()
                mBeatSharedViewModel.beats.value?.beatName = provinceList[position]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        binding.spBeatName.setItem(provinceList)

        //https://stackoverflow.com/questions/48343622/how-to-fix-parameter-specified-as-non-null-is-null-on-rotating-screen-in-a-fragm
        // binding.spSalesperson.setSelection(2)
    }


    fun callApi(type: String) {
        showHud()
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBeatDetailList(
            BeatDetailListRequestParams(
               userId,
                type
            )
        )
        responseCall.enqueue(orderVSQualityResponseResponse as Callback<BeatDetailListResponse>)

    }

    private var dataList: List<BeatListDetails> = emptyList<BeatListDetails>()
    private val orderVSQualityResponseResponse =
        object : NetworkCallBack<BeatDetailListResponse>() {
            override fun onSuccessNetwork(
                data: Any?,
                response: NetworkResponse<BeatDetailListResponse>
            ) {
                response.data?.status?.let { status ->

                    hide()
                    dataList = response.data.BeatList
                    var statList: MutableList<String> = ArrayList()
                    if (dataList.size>0) {
                        for (i in dataList) {
                            statList.add(i.BM_Beat_Name!!)
                        }

                        binding.spSalesperson.setItem(statList)
                        binding.spSalesperson.setSelection(0)
                        //binding.spSalesperson.isEnabled=true

                    }else{
                        binding.etAssignTo.setText("Beat Name")
                        //binding.spSalesperson.isEnabled=false
                    }
                }

            }

            override fun onFailureNetwork(data: Any?, error: NetworkError) {
                hide()
            }

        }


    fun callSubmit() {
        showHud()
        val inpFormat = SimpleDateFormat(DateUtility.dd_MM_yy, Locale.US);
        val outputformat = SimpleDateFormat("yyyy-MM-dd", Locale.US);
       /* val stdate =
            DateUtils.parseDate(binding.etStartDate.text.toString(), inpFormat, outputformat)
        val endate = DateUtils.parseDate(binding.etEndDate.text.toString(), inpFormat, outputformat)*/
        //showToastMessage(stdate)
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.createBeatSchedule(
            CreateBeatScheduleRequestParams(
                userId,
                masterId,
                binding.etStartDate.text.toString(),
                binding.etEndDate.text.toString()
            )
            // CreateBeatScheduleRequestParams((SharedPreferenceUtils.getLoggedInUserId(context as Context),masterId,"2020-07-22","2020-07-25")
        )


        responseCall.enqueue(createBeatScheduleResponseResponse as Callback<CreateBeatReportResponse>)

    }

    private val createBeatScheduleResponseResponse =
        object : NetworkCallBack<CreateBeatReportResponse>() {
            override fun onSuccessNetwork(
                data: Any?,
                response: NetworkResponse<CreateBeatReportResponse>
            ) {
                hide()

                if (response.data!!.status==1){
                    showToastMessage(response.data?.respMessage!!)
                    DataConstant.beat_level=binding.etBeatName.text.toString()
                    DataConstant.beatScheduleId=response.data.beatScheduleId.toString()
                    val navDirection =
                        CreateBeatFragmentDirections.actionCreateBeatFragmentToAssignBeatToLocation(
                            binding.etStartDate.text.toString(),
                            binding.etEndDate.text.toString()
                        )
                    Navigation.findNavController(binding.btnAssignTask).navigate(navDirection)
                }else{
                    showToastMessage(response.data?.respMessage!!)

                }


            }

            override fun onFailureNetwork(data: Any?, error: NetworkError) {
                hide()
            }

        }



    private fun showCustomDatePicker(minStartDate:String = ""){
        var now = DateUtility.dateStrToCalendar(minStartDate)
        val year = now[Calendar.YEAR]
        val dpd: com.wdullaer.materialdatetimepicker.date.DatePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
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

    override fun onDateSet(
        view: com.wdullaer.materialdatetimepicker.date.DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, monthOfYear, dayOfMonth)
        val subDateString: String = DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.YYYY_DASH_MM_DASH_DD)
        currentDatePicketParentView?.setText(subDateString)
    }
}


