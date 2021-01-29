package com.velectico.rbm.beats.views

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.annotations.SerializedName
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentBeatReportBinding
import com.velectico.rbm.databinding.FragmentBeatReportListBinding
import com.velectico.rbm.dealer.model.AreaResponse
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import kotlinx.android.synthetic.main.fragment_beat_report.view.*
import retrofit2.Callback
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BeatReportFragment : BaseFragment(), DatePickerDialog.OnDateSetListener{
    private lateinit var binding: FragmentBeatReportBinding

    var orderReceive = ""
    var paymentReceived = ""
    var complain = ""
    var priceProblem = ""
    var prefCompany = ""
    var prefReason = ""
    var turnOver = ""
    var taskDetail = BeatTaskDetails()
    var dlrDtl = DealerDetails()
    var userId = ""
    var dealerId = ""
    var taskId = ""
    var distributorId = ""
    //var date = ""
    var dateFormat: DateFormat? = null
    var date: Date? = null
    var today_date: String? = null
    private var cuurentDatePicketParentView : TextInputEditText? = null;

    override fun getLayout(): Int {
        return R.layout.fragment_beat_report
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatReportBinding
        taskDetail = arguments!!.get("taskDetails") as BeatTaskDetails
        dlrDtl = arguments!!.get("dealerDetails") as DealerDetails
        if (RBMLubricantsApplication.globalRole == "Team" ){
            userId= GloblalDataRepository.getInstance().teamUserId
        }else{
            userId= SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }

        binding.tvProdNetPrice.text = dlrDtl.dealerPhone.toString()
        binding.tvProdTotalPrice.text = dlrDtl.DM_Contact_Person.toString()
        if (DataConstant.orderAmt != "") {
            binding.tvOrdrAmt.text = "₹ " + DataConstant.orderAmt
        } else {
            binding.tvOrdrAmt.text = "₹ 0"

        }
        if (DataConstant.collectionAmt != "") {
            binding.collectionAmt.text = "₹ " + DataConstant.collectionAmt

        } else {
            binding.collectionAmt.text = "₹ 0"

        }

        binding.gradeval.text = DataConstant.dealerGrade
        binding.type.text = DataConstant.nameValue
        binding.name.text = DataConstant.name
        if (taskDetail.taskId != null) {
            dealerId = taskDetail.dealerId.toString()
            distributorId = taskDetail.distribId.toString()
            taskId = taskDetail.taskId.toString()


        } else {
            dealerId = DataConstant.dealerId
            distributorId = DataConstant.distributorId
            taskId = DataConstant.taskId

        }

        binding.etFollowupdate.setOnClickListener {
            cuurentDatePicketParentView = this.binding.etFollowupdate;
            showDatePickerDialog()
            //showDatePickerDialog()
        }


        callApi("Order Not received Or Less")
        callApi("Payment Not Received")
        callApi("Complain vs Quality")
        callApi("Prices problem")
        callApi("Preference Make")
        callApi("Preferring reasons")
        callApi("Turnover range")
    }
    fun callApi(type: String) {
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        when (type) {
            "Order Not received Or Less" -> {
                showHud()
                responseCall.enqueue(orderReceiveResponse as Callback<OrderVSQualityResponse>)
            }

            "Payment Not Received" -> {
                responseCall.enqueue(paymentResponse as Callback<OrderVSQualityResponse>)
            }

            "Complain vs Quality" -> {
                responseCall.enqueue(complainResponse as Callback<OrderVSQualityResponse>)
            }

            "Prices problem" -> {
                responseCall.enqueue(priceResponse as Callback<OrderVSQualityResponse>)

            }

            "Preference Make" -> {
                responseCall.enqueue(prefCompanyResponse as Callback<OrderVSQualityResponse>)

            }

            "Preferring reasons" -> {
                responseCall.enqueue(prefReasonResponse as Callback<OrderVSQualityResponse>)

            }

            "Turnover range" -> {
                responseCall.enqueue(turnoverResponse as Callback<OrderVSQualityResponse>)

            }

        }

        binding.btnSubmit.setOnClickListener {




            if (binding.etFeedback.text.toString()==""){
                showToastMessage("Enter Feedback")
            }else {
                /*val inputformat = SimpleDateFormat(DateUtility.dd_MM_yy, Locale.US);
                val outputformat = SimpleDateFormat("yyyy-MM-dd", Locale.US);
                date = DateUtils.parseDate(binding.etFollowupdate.text.toString(),inputformat,outputformat)*/
                val param = CreateBeatReportRequestParams(
                    userId,
                    taskId,
                    dealerId,
                    distributorId,
                    orderReceive,
                    paymentReceived,
                    complain,
                    priceProblem,
                    "0",
                    prefReason,
                    prefCompany,
                    turnOver,
                    binding.etFollowupdate.text.toString().trim(),
                    binding.tilFollowUpReasonTxt.text.toString().trim(),
                    binding.etFeedback.text.toString().trim()
                )
                showHud()
                val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
                val responseCall = apiInterface.createBeatReport(param)
                responseCall.enqueue(cretaeBeatResponse as Callback<CreateBeatReportResponse>)
            }
        }
    }
    private val cretaeBeatResponse = object : NetworkCallBack<CreateBeatReportResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<CreateBeatReportResponse>
        ) {

            if (response.data!!.status == 1) {
                Toast.makeText(activity!!, "${response.data.respMessage}", Toast.LENGTH_SHORT)
                    .show()
                hide()
                activity!!.onBackPressed()
            } else {
                Toast.makeText(activity!!, "${response.data.respMessage}", Toast.LENGTH_SHORT)
                    .show()
                hide()
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }
    private var prefReasonList: List<DropdownDetails> = emptyList<DropdownDetails>()
    private val prefReasonResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                prefReasonList = response.data.BeatReportList
                if (prefReasonList.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Preferring reasons")
                    for (i in prefReasonList) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.spinnerPreferringReason.adapter = adapter2

                    binding.spinnerPreferringReason.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding.spinnerPreferringReason.selectedItem == "Preferring reasons") {
                                    prefReason="0"
                                } else {

                                    prefReason = prefReasonList[position - 1].Exp_Head_Id!!

                                }


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }

                }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }


    private var turnoverList: List<DropdownDetails> = emptyList<DropdownDetails>()
    private val turnoverResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                turnoverList = response.data.BeatReportList
                if (turnoverList.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Turnover")
                    for (i in turnoverList) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.spinnerTurnOverRange.adapter = adapter2

                    binding.spinnerTurnOverRange.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding.spinnerTurnOverRange.selectedItem == "Turnover") {
                                    turnOver="0"

                                } else {

                                    turnOver = turnoverList[position - 1].Exp_Head_Id!!

                                }


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }

                }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }





    private var prefCompanyList: List<DropdownDetails> = emptyList<DropdownDetails>()
    private val prefCompanyResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                prefCompanyList = response.data.BeatReportList
                if (prefCompanyList.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Preferred Company")
                    for (i in prefCompanyList) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.prefCompanySpinner.adapter = adapter2

                    binding.prefCompanySpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding.prefCompanySpinner.selectedItem == "Preferred Company") {
                                    prefCompany="0"

                                } else {

                                    prefCompany = prefCompanyList[position - 1].Exp_Head_Id!!

                                }


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }

                }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }





    private var priceList: List<DropdownDetails> = emptyList<DropdownDetails>()
    private val priceResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                priceList = response.data.BeatReportList
                if (priceList.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Prices problem")
                    for (i in priceList) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.priceProbSpinner.adapter = adapter2

                    binding.priceProbSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding.priceProbSpinner.selectedItem == "Prices problem") {
                                    priceProblem="0"

                                } else {

                                    priceProblem = priceList[position - 1].Exp_Head_Id!!

                                }


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }

                }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }





    private var complainList: List<DropdownDetails> = emptyList<DropdownDetails>()
    private val complainResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                complainList = response.data.BeatReportList
                if (complainList.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Complain vs Quality")
                    for (i in complainList) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.complainVsQualitySpinner.adapter = adapter2

                    binding.complainVsQualitySpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding.complainVsQualitySpinner.selectedItem == "Complain vs Quality") {
                                    complain="0"

                                } else {

                                    complain = complainList[position - 1].Exp_Head_Id!!

                                }


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }

                }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    private var paymentReceiveList: List<DropdownDetails> = emptyList<DropdownDetails>()
    private val paymentResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                paymentReceiveList = response.data.BeatReportList
                if (paymentReceiveList.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Payment Not Received")
                    for (i in paymentReceiveList) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.paymentSpinner.adapter = adapter2

                    binding.paymentSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding.paymentSpinner.selectedItem == "Payment Not Received") {
                                    paymentReceived="0"

                                } else {

                                    paymentReceived = paymentReceiveList[position - 1].Exp_Head_Id!!

                                }


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }

                }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    private var orderReceiveList: List<DropdownDetails> = emptyList<DropdownDetails>()
    private val orderReceiveResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                orderReceiveList = response.data.BeatReportList
                if (orderReceiveList.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Order Not received Or Less")
                    for (i in orderReceiveList) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.spinnerOrderNotReceived.adapter = adapter2

                    binding.spinnerOrderNotReceived.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding.spinnerOrderNotReceived.selectedItem == "Order Not received Or Less") {
                                    orderReceive="0"
                                } else {

                                    orderReceive = orderReceiveList[position - 1].Exp_Head_Id!!

                                }


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }

                }
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        DatePickerDialog(requireActivity(), this, year, month, dayOfMonth).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, month, dayOfMonth)
        val subDateString: String =
            DateUtility.getStringDateFromTimestamp(
                (tempDate.time),
                DateUtility.YYYY_DASH_MM_DASH_DD
            )
        if (cuurentDatePicketParentView == binding.etFollowupdate) {
            dateFormat = SimpleDateFormat("HH:mm:ss")
            date = Date()
            today_date = dateFormat!!.format(date)
            binding.etFollowupdate.setText(subDateString + " "+today_date)
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
