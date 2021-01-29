package com.velectico.rbm.expense.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.DropdownDetails
import com.velectico.rbm.beats.model.OrderVSQualityRequestParams
import com.velectico.rbm.beats.model.OrderVSQualityResponse
import com.velectico.rbm.databinding.FragmentCreateMultipleExpenseBinding
import com.velectico.rbm.dealer.views.AddDealerFragmentDirections
import com.velectico.rbm.expense.model.*
import com.velectico.rbm.leave.view.ApplyLeaveFragment
import com.velectico.rbm.menuitems.viewmodel.AttendanceRequestParams
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import retrofit2.Callback
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class CreateMultipleExpenseFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {

    private var binding: FragmentCreateMultipleExpenseBinding? = null
    var currInstance: TextInputEditText? = null
    var amount = 0.0
    var amount1 = 0.0
    var amount2 = 0.0
    var amount3 = 0.0
    var amount4 = 0.0
    var amount5 = 0.0
    var spinner1Value = ""
    var spinner2Value = ""
    var spinner3Value = ""
    var spinner4Value = ""
    var spinner5Value = ""

    var dateFormat: DateFormat? = null
    var date: Date? = null
    var today_date: String? = null
    var km1 = "0"
    var km2 = "0"
    var km3 = "0"
    var km4 = "0"
    var km5 = "0"
    var taskId = ""
    var expenceList: java.util.ArrayList<ExpDetailsRequest> =
        java.util.ArrayList<ExpDetailsRequest>()
    var userId=""

    override fun getLayout(): Int {
        return R.layout.fragment_create_multiple_expense
    }


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentCreateMultipleExpenseBinding
        if (RBMLubricantsApplication.globalRole == "Team" ){
            userId= GloblalDataRepository.getInstance().teamUserId
        }else{
            userId= SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        getBeatList()
        //dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

        binding.etDate.setOnClickListener {
            currInstance = binding.etDate
            showCustomDatePicker(binding.etDate.text.toString())

        }

        binding.btnCancel.setOnClickListener {
            Navigation.findNavController(binding.btnCancel as Button).popBackStack()

        }

        callApi1("Expense Head")
        callApi2("Expense Head")
        callApi3("Expense Head")
        callApi4("Expense Head")
        callApi5("Expense Head")

        binding.etEx1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (binding.etEx1.text.toString() != "") {
                    amount1 = binding.etEx1.text.toString().toDouble()
                    Handler().postDelayed({
                        callApiCalculate()
                    }, 2000)
                } else {
                    amount1 = 0.0
                    Handler().postDelayed({
                        callApiCalculate()
                    }, 2000)
                }

            }
        })


        binding.etEx2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {


            }

            override fun afterTextChanged(editable: Editable) {
                if (binding.etEx2.text.toString() != "") {
                    amount2 = binding.etEx2.text.toString().toDouble()
                    Handler().postDelayed({
                        callApiCalculate()
                    }, 2000)
                } else {
                    amount2 = 0.0
                    Handler().postDelayed({
                        callApiCalculate()
                    }, 2000)
                }
            }
        })

        binding.etEx3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {


            }

            override fun afterTextChanged(editable: Editable) {
                if (binding.etEx3.text.toString() != "") {
                    amount3 = binding.etEx3.text.toString().toDouble()
                    Handler().postDelayed({
                        callApiCalculate()
                    }, 2000)
                } else {
                    amount3 = 0.0
                    Handler().postDelayed({
                        callApiCalculate()
                    }, 2000)
                }
            }
        })


        binding.etEx4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {


            }

            override fun afterTextChanged(editable: Editable) {
                if (binding.etEx4.text.toString() != "") {
                    amount4 = binding.etEx4.text.toString().toDouble()
                    Handler().postDelayed({
                        callApiCalculate()
                    }, 2000)
                } else {
                    amount4 = 0.0
                    Handler().postDelayed({
                        callApiCalculate()
                    }, 2000)
                }
            }
        })


        binding.etEx5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {


            }

            override fun afterTextChanged(editable: Editable) {
                if (binding.etEx5.text.toString() != "") {
                    amount5 = binding.etEx5.text.toString().toDouble()
                    Handler().postDelayed({
                        callApiCalculate()
                    }, 2000)
                } else {
                    amount5 = 0.0
                    Handler().postDelayed({
                        callApiCalculate()
                    }, 2000)
                }
            }
        })


        binding.etKm1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {


            }

            override fun afterTextChanged(editable: Editable) {
                km1 = binding.etKm1.text.toString()
            }
        })

        binding.etKm2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {


            }

            override fun afterTextChanged(editable: Editable) {
                km2 = binding.etKm2.text.toString()
            }
        })

        binding.etKm3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {


            }

            override fun afterTextChanged(editable: Editable) {
                km3 = binding.etKm3.text.toString()
            }
        })

        binding.etKm4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {


            }

            override fun afterTextChanged(editable: Editable) {
                km4 = binding.etKm4.text.toString()
            }
        })

        binding.etKm5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {


            }

            override fun afterTextChanged(editable: Editable) {
                km5 = binding.etKm5.text.toString()
            }
        })

        binding.btnSave.setOnClickListener {

            if (binding.spinnerBeatList.selectedItem == "Select Beat Name") {
                showToastMessage("Select Beat Name")
            } else if (binding.etDate.text.toString() == "") {
                showToastMessage("Select Date")

            } else {

                expenceList.clear()
                if (spinner1Value != "") {
                    expenceList.add(
                        ExpDetailsRequest(
                            spinner1Value,
                            amount1.toString(),
                            km1.toString(), binding.etDate.text.toString()
                        )
                    )
                }
                if (spinner2Value != "") {
                    expenceList.add(
                        ExpDetailsRequest(
                            spinner2Value,
                            amount2.toString(),
                            km2.toString(), binding.etDate.text.toString()
                        )
                    )
                }
                if (spinner3Value != "") {
                    expenceList.add(
                        ExpDetailsRequest(
                            spinner3Value,
                            amount3.toString(),
                            km3.toString(), binding.etDate.text.toString()
                        )
                    )
                }
                if (spinner4Value != "") {
                    expenceList.add(
                        ExpDetailsRequest(
                            spinner4Value,
                            amount4.toString(),
                            km4.toString(), binding.etDate.text.toString()
                        )
                    )
                }
                if (spinner5Value != "") {
                    expenceList.add(
                        ExpDetailsRequest(
                            spinner5Value,
                            amount5.toString(),
                            km5.toString(), binding.etDate.text.toString()
                        )
                    )
                }

                if (expenceList.size <= 0) {
                    showToastMessage("Select Expense")
                } else {

                     showHud()
                     val apiInterface =
                         ApiClient.getInstance().client.create(ApiInterface::class.java)
                     val model = ExpenseCreateRequest(
                         userId,
                         taskId,
                         beatId,
                         binding.etDate.text.toString(),
                         expenceList
                     )
                     val responseCall = apiInterface.createExpense(
                         model
                     )
                     responseCall.enqueue(createExpenseResponse as Callback<CreateExpenseResponse>)
                }

            }

        }


    }

    @SuppressLint("SetTextI18n")
    private fun callApiCalculate() {

        amount = amount1 + amount2 + amount3 + amount4 + amount5
        if (amount == 0.0) {
            binding!!.tvTotalAmount.visibility = View.GONE
        } else {
            binding!!.tvTotalAmount.visibility = View.VISIBLE

            binding!!.tvTotalAmount.text = "₹" + amount.toString()
        }

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

    fun getBeatList() {
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        //showHud()

        val userId = if (RBMLubricantsApplication.globalRole == "Team") {
            GloblalDataRepository.getInstance().teamUserId
        } else {
            SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBitList(
            AttendanceRequestParams(
                userId
            )
        )
        responseCall.enqueue(createResponse as Callback<BidListResponse>)

    }

    var beatId: String? = ""
    var detailsList: List<Details> = emptyList()
    val createResponse = object : NetworkCallBack<BidListResponse>() {
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<BidListResponse>) {
            response.data?.status?.let { status ->
                hide()

                if (response.data.expenseDetails != null) {
                    detailsList = response.data.expenseDetails
                    if (detailsList.size > 0) {
                        var itemList: MutableList<String> = ArrayList()
                        itemList.add("Select Beat Name")
                        for (i in detailsList) {
                            itemList.add(i.taskName!!)
                        }
                        val adapter2 = context?.let {
                            ArrayAdapter(
                                it,
                                android.R.layout.simple_spinner_dropdown_item, itemList
                            )
                        }
                        binding?.spinnerBeatList?.adapter = adapter2
                        binding?.spinnerBeatList?.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    adapterView: AdapterView<*>,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    if (binding!!.spinnerBeatList.selectedItem == "Select Beat Name") {

                                    } else {
                                        beatId = detailsList[position - 1].beatId
                                        taskId = detailsList[position - 1].taskId.toString()

                                        /* if (detailsList[position - 1].BSD_Dealer_ID == "0") {
                                             beatId = detailsList[position - 1].BSD_Distrib_ID
                                         } else {
                                             beatId = detailsList[position - 1].BSD_Dealer_ID

                                         }*/
                                    }
                                    // showToastMessage(beatId.toString())


                                }

                                override fun onNothingSelected(adapterView: AdapterView<*>) {}
                            }
                    }
                }


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }
    private val createExpenseResponse = object : NetworkCallBack<CreateExpenseResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<CreateExpenseResponse>
        ) {
            hide()
            if (response.data!!.status == 1) {
                Toast.makeText(activity!!, "${response.data?.respMessage}", Toast.LENGTH_SHORT)
                    .show()
                var expenseId = response.data.expensId
                var dialog = Dialog(context!!)
                dialog.setContentView(R.layout.add_dialog_view)
                val handler = Handler()
                val runnable = Runnable {
                    if (dialog.isShowing()) {
                        dialog.dismiss()
                        val navDirection=CreateMultipleExpenseFragmentDirections.actionCreateMultipleExpenseFragmentToTempCreateExpense(response.data.expensId.toString())
                        Navigation.findNavController(binding!!.btnSave).navigate(navDirection)
                    } else {
                        dialog.show()
                    }
                }
                handler.postDelayed(runnable, 1000)
                dialog.show()
               // activity!!.onBackPressed()



            } else {
                Toast.makeText(context, response.data.respMessage, Toast.LENGTH_LONG).show()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
             hide()
        }


    }

    private fun showCustomDatePicker(minStartDate: String = "") {
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
            dpd.show(it, ApplyLeaveFragment.DATE_PICKER)
        }
        val holidays = arrayOf("20-05-2020", "21-05-2020", "25-05-2020")
        val disabledDays = DateUtility.getDisabledDatesArr(holidays).toTypedArray()
        dpd.highlightedDays = disabledDays
        dpd.disabledDays = disabledDays
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, monthOfYear, dayOfMonth)
        val subDateString: String = DateUtility.getStringDateFromTimestamp(
            (tempDate.time),
            DateUtility.YYYY_DASH_MM_DASH_DD
        )
        if (currInstance != null) {
            dateFormat = SimpleDateFormat("HH:mm:ss")
            date = Date()
            today_date = dateFormat!!.format(date)
            currInstance?.setText(subDateString + " "+today_date)
        }
    }


    fun callApi1(type: String) {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(expenseResponse1 as Callback<OrderVSQualityResponse>)

    }

    fun callApi2(type: String) {
       // showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(expenseResponse2 as Callback<OrderVSQualityResponse>)

    }

    fun callApi3(type: String) {
       // showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(expenseResponse3 as Callback<OrderVSQualityResponse>)

    }

    fun callApi4(type: String) {
       // showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(expenseResponse4 as Callback<OrderVSQualityResponse>)

    }

    fun callApi5(type: String) {
        //showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(expenseResponse5 as Callback<OrderVSQualityResponse>)

    }


    private var expenseList1: List<DropdownDetails> = emptyList<DropdownDetails>()
    private var expenseList2: List<DropdownDetails> = emptyList<DropdownDetails>()
    private var expenseList3: List<DropdownDetails> = emptyList<DropdownDetails>()
    private var expenseList4: List<DropdownDetails> = emptyList<DropdownDetails>()
    private var expenseList5: List<DropdownDetails> = emptyList<DropdownDetails>()

    private val expenseResponse1 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                expenseList1 = response.data.BeatReportList
                if (expenseList1.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Select Expense Name")
                    for (i in expenseList1) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding!!.spinner1.adapter = adapter2

                    binding!!.spinner1.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding!!.spinner1.selectedItem == "Select Expense Name") {

                                } else {
                                    if (binding!!.spinner1.selectedItem == "Petrol") {
                                        binding!!.rlKm1.visibility = View.VISIBLE
                                    } else {
                                        binding!!.rlKm1.visibility = View.GONE
                                    }
                                    spinner1Value = expenseList1[position - 1].Exp_Head_Id!!
                                    //showToastMessage("spinner1Value"+spinner1Value)

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


    private val expenseResponse2 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                expenseList2 = response.data.BeatReportList
                if (expenseList2.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Select Expense Name")
                    for (i in expenseList2) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding!!.spinner2.adapter = adapter2

                    binding!!.spinner2.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding!!.spinner2.selectedItem == "Select Expense Name") {

                                } else {
                                    if (binding!!.spinner2.selectedItem == "Petrol") {
                                        binding!!.rlKm2.visibility = View.VISIBLE
                                    } else {
                                        binding!!.rlKm2.visibility = View.GONE
                                    }
                                    spinner2Value = expenseList2[position - 1].Exp_Head_Id!!
                                    //showToastMessage("spinner2Value"+spinner2Value)

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


    private val expenseResponse3 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                expenseList3 = response.data.BeatReportList
                if (expenseList3.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Select Expense Name")
                    for (i in expenseList3) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding!!.spinner3.adapter = adapter2

                    binding!!.spinner3.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding!!.spinner3.selectedItem == "Select Expense Name") {

                                } else {
                                    if (binding!!.spinner3.selectedItem == "Petrol") {
                                        binding!!.rlKm3.visibility = View.VISIBLE
                                    } else {
                                        binding!!.rlKm3.visibility = View.GONE
                                    }
                                    spinner3Value = expenseList3[position - 1].Exp_Head_Id!!
                                    //showToastMessage("spinner3Value"+spinner3Value)

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


    private val expenseResponse4 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                expenseList4 = response.data.BeatReportList
                if (expenseList4.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Select Expense Name")
                    for (i in expenseList4) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding!!.spinner4.adapter = adapter2

                    binding!!.spinner4.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding!!.spinner4.selectedItem == "Select Expense Name") {

                                } else {
                                    if (binding!!.spinner4.selectedItem == "Petrol") {
                                        binding!!.rlKm4.visibility = View.VISIBLE

                                    } else {
                                        binding!!.rlKm4.visibility = View.GONE
                                    }
                                    spinner4Value = expenseList4[position - 1].Exp_Head_Id!!
                                    //showToastMessage("spinner4Value"+spinner4Value)

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


    private val expenseResponse5 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                expenseList5 = response.data.BeatReportList
                if (expenseList5.size > 0) {
                    var statList: MutableList<String> = ArrayList()
                    statList.add("Select Expense Name")
                    for (i in expenseList5) {
                        statList.add(i.Exp_Head_Name!!)
                    }
                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding!!.spinner5.adapter = adapter2

                    binding!!.spinner5.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (binding!!.spinner5.selectedItem == "Select Expense Name") {

                                } else {
                                    /*spinner5Value = expenseList5[position - 1].Exp_Head_Id!!
                                    showToastMessage("spinner5Value" + spinner5Value)*/
                                    if (binding!!.spinner5.selectedItem == "Petrol") {
                                        binding!!.rlKm5.visibility = View.VISIBLE

                                    } else {
                                        binding!!.rlKm5.visibility = View.GONE
                                    }
                                    spinner5Value = expenseList5[position - 1].Exp_Head_Id!!
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


}