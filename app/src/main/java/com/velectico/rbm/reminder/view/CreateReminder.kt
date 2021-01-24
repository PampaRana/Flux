package com.velectico.rbm.reminder.view

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.CreateReminderFragmentBinding
import com.velectico.rbm.dealer.model.AreaDetails
import com.velectico.rbm.dealer.model.AreaResponse
import com.velectico.rbm.dealer.model.DealerAreaParams
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.reminder.model.CreateReminderRequestParams
import com.velectico.rbm.reminder.model.CreateReminderResponse
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

class CreateReminder :  BaseFragment() , DatePickerDialog.OnDateSetListener {
    private lateinit var binding: CreateReminderFragmentBinding
    var dealerId = "0"
    var distribId = "0"
    var role=""
    var umId=""
    var areaValue=""
    var userId=""
    override fun getLayout(): Int {
        return R.layout.create_reminder_fragment
    }
    override fun init(binding: ViewDataBinding) {

        this.binding = binding as CreateReminderFragmentBinding
        userId=SharedPreferenceUtils.getLoggedInUserId(context as Context)
        role= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Role").toString()
        umId= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_ID").toString()
        if(role == DEALER_ROLE){
            binding.llSpinnerType.visibility = View.GONE
            binding.llSpinnerDealDis.visibility = View.GONE
            dealerId = umId


        }
        if(role == DISTRIBUTER_ROLE){
            binding.llSpinnerType.visibility = View.GONE
            binding.llSpinnerDealDis.visibility = View.GONE
            distribId = umId


        }
        binding.etEndDate?.setOnClickListener {
            showDatePickerDialog()
        }
        binding.btnAssignTask.setOnClickListener {
            createReminder()
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
                        binding.llArea.visibility=View.VISIBLE
                        binding.llSpinnerDealDis.visibility=View.GONE
                        callApiArea(userId)
                        showToastMessage("Select Area")

                    }else if (binding.spinnerType.selectedItem.toString().equals("Distributor")) {
                        binding.llArea.visibility=View.GONE
                        binding.llSpinnerDealDis.visibility = View.GONE

                        callDistApi()

                    }else {
                        binding.llArea.visibility=View.GONE
                    }


                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }



    }
    private fun callApiArea(userId: String) {
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getArea(
            DealerAreaParams(userId)
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
                                callDealApi(userId,areaValue)
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
    fun callDealApi(userId: String, areaId : String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(userId, areaId)
        )

        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

    }

    fun callDistApi() {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.distDropDownList(
            DistListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context))
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

    fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        DatePickerDialog(requireActivity(), this, year, month, dayOfMonth).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, month, dayOfMonth)
        val subDateString: String =
            DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.dd_MM_yy)
        binding.etEndDate.setText(subDateString)
    }

    fun createReminder(){
        if (binding.etEndDate.text.toString()?.trim() == ""){
            showToastMessage("Please provide Followup Date")
        }
        else if (binding.etDesc.text.toString()?.trim() == ""){
            showToastMessage("Please provide reminder Description")
        }
        else {
            val inpFormat = SimpleDateFormat(DateUtility.dd_MM_yy, Locale.US);
            val outputformat = SimpleDateFormat("yyyy-MM-dd", Locale.US);
            val date =
                DateUtils.parseDate(binding.etEndDate.text.toString(), inpFormat, outputformat)
            val param = CreateReminderRequestParams(
                SharedPreferenceUtils.getLoggedInUserId(context as Context),
                "0",
                dealerId,
                distribId,
                date,
                binding.etDesc.text.toString()

            )
            showHud()
            val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
            val responseCall = apiInterface.addReminder(param)
            responseCall.enqueue(CreateReminderResponse as Callback<CreateReminderResponse>)
        }
    }

    private val CreateReminderResponse = object : NetworkCallBack<CreateReminderResponse>(){
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<CreateReminderResponse>
        ) {
            response.data?.respMessage?.let { status ->
                Toast.makeText(activity!!, "Reminder Added", Toast.LENGTH_SHORT)
                    .show()
                hide()
                activity!!.onBackPressed()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

}