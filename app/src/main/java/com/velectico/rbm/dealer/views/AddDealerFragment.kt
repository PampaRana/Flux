package com.velectico.rbm.dealer.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.DropdownDetails
import com.velectico.rbm.beats.model.OrderVSQualityRequestParams
import com.velectico.rbm.beats.model.OrderVSQualityResponse
import com.velectico.rbm.databinding.FragmentAddDealerBinding
import com.velectico.rbm.dealer.model.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.InternetCheck
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.util.*


class AddDealerFragment : BaseFragment(),
    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private lateinit var binding: FragmentAddDealerBinding
    var userId = ""
    var mobilePattern = ""
    var emailPattern = ""
    var segId = ""
    var package1 = ""
    var package2 = ""
    var package3 = ""
    var price1 = ""
    var price2 = ""
    var price3 = ""

    //var volume1 = ""
    //var volume2 = ""
    //var volume3 = ""
    var spinner1Grade = ""
    var spinner2Grade = ""
    var spinner3Grade = ""
    var spinner1Company = ""
    var spinner2Company = ""
    var spinner3Company = ""
    var gradingValue = ""
    var areaValue = ""
    var packageValue = ""

    private var currentDatePicketParentView: TextInputEditText? = null

    var collectionList: java.util.ArrayList<CollectionRequest> =
        java.util.ArrayList<CollectionRequest>()


    override fun getLayout(): Int {
        return R.layout.fragment_add_dealer
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentAddDealerBinding
        userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        mobilePattern = "[0-9]{10}";
        emailPattern = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"
        binding.inputReminder.setOnClickListener {
            currentDatePicketParentView = this.binding.inputReminder;
            showCustomDatePicker(binding.inputReminder.text.toString());
            // showDatePickerDialog();
        }
        /*binding.etPack1.addTextChangedListener(object : TextWatcher {
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
                package1 = binding.etPack1.text.toString()
            }
        })
        binding.etPack2.addTextChangedListener(object : TextWatcher {
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
                package2 = binding.etPack2.text.toString()
            }
        })

        binding.etPack3.addTextChangedListener(object : TextWatcher {
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
                package3 = binding.etPack3.text.toString()
            }
        })*/


        binding.etPrice1.addTextChangedListener(object : TextWatcher {
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
                price1 = binding.etPrice1.text.toString()
            }
        })
        binding.etPrice2.addTextChangedListener(object : TextWatcher {
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
                price2 = binding.etPrice2.text.toString()
            }
        })
        binding.etPrice3.addTextChangedListener(object : TextWatcher {
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
                price3 = binding.etPrice3.text.toString()
            }
        })


        /*binding.etVol1.addTextChangedListener(object : TextWatcher {
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
                volume1 = binding.etVol1.text.toString()
            }
        })

        binding.etVol2.addTextChangedListener(object : TextWatcher {
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
                volume2 = binding.etVol2.text.toString()
            }
        })


        binding.etVol3.addTextChangedListener(object : TextWatcher {
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
                volume3 = binding.etVol3.text.toString()
            }
        })*/

        binding.inputDealerMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().length > 9) {
                    if (InternetCheck.isConnected(context as Context)) {
                        //showHud()
                        validateMobileNumber(userId,s.toString())
                    } else {
                        showToastMessage("No Internet Connection")
                    }
                }
            }
        })

        binding.inputDealerMobileOptional.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().length > 9) {
                    if (InternetCheck.isConnected(context as Context)) {
                        showHud()
                        validateMobileNumber(userId,s.toString())
                    } else {
                        showToastMessage("No Internet Connection")
                    }
                }
            }
        })

        binding.btnAdd.setOnClickListener {
            val reminder = binding.inputReminder.text.toString()?.trim();
            if (binding.inputDealerName.text.toString() == "") {
                showToastMessage("Enter Dealer Name")

            } /*else if (binding.inputDealerMobile.text.toString() == "") {
                showToastMessage("Enter Dealer Mobile")

            } else if (!binding.inputDealerMobile.text.toString()
                    .matches(mobilePattern.toRegex())
            ) {
                showToastMessage("Invalid Mobile Number")

            } else if (binding.inputDealerAddress.text.toString() == "") {
                showToastMessage("Enter Dealer Address")

            } */ else if (binding.spinnerArea.selectedItem == "Select Area") {
                showToastMessage("Select Area")

            } /*else if (binding.inputContactName.text.toString() == "") {
                showToastMessage("Enter Contact Name")

            } else if (binding.spinnerSegment.selectedItem == "Select Segment") {
                showToastMessage("Select Segment")

            } else if (binding.inputSaleMnthy.text.toString() == "") {
                showToastMessage("Enter Sale Monthly")

            } else if (binding.spinnerGrading.selectedItem == "Select Grading") {
                showToastMessage("Select Grading")

            } else if (binding.inputFeedback.text.toString() == "") {
                showToastMessage("Enter Feedback")

            } else if (binding.inputReminder == null || reminder == "") {
                showToastMessage("Please select Reminder")
            } else if (binding.etCreditDays.text.toString() == "") {
                showToastMessage("Enter Credit Days")

            } else if (binding.etCreditDays.text.toString() > "365") {
                showToastMessage("Invalid Credit Days")

            }*/ else {
                collectionList.clear()
                if (spinner1Company != "") {
                    collectionList.add(
                        CollectionRequest(
                            spinner1Company,
                            spinner1Grade,
                            package1.toString(), price1.toString(),
                            /*volume1.toString()*/"0"

                        )
                    )
                }
                if (spinner2Company != "") {
                    collectionList.add(
                        CollectionRequest(
                            spinner2Company,
                            spinner2Grade,
                            package2.toString(), price2.toString(),
                            /*volume2.toString()*/"0"

                        )
                    )
                }

                if (spinner3Company != "") {
                    collectionList.add(
                        CollectionRequest(
                            spinner3Company,
                            spinner3Grade,
                            package3.toString(), price3.toString(),
                            /*volume3.toString()*/"0"

                        )
                    )
                }
                if (binding.inputDealerMobile.text.toString().trim() != "") {
                    if (!binding.inputDealerMobile.text.toString().trim()
                            .matches(mobilePattern.toRegex())
                    ) {

                        showToastMessage("Invalid Mobile Number")
                    }else{

                        //showToastMessage("Success")
                        //validateMobile(userId,binding.inputDealerMobile.text.toString().trim())
                        if (collectionList.size == 0) {

                            collectionList.add(
                                CollectionRequest(
                                    "","","","",""

                                )
                            )
                        }
                        showHud()
                        val apiInterface =
                            ApiClient.getInstance().client.create(ApiInterface::class.java)
                        val responseCall = apiInterface.addDealerInfo(
                            AddDealerRequestParams(
                                userId,
                                binding.inputDealerName.text.toString().trim(),
                                binding.inputDealerMobile.text.toString().trim(),
                                binding.inputDealerMobileOptional.text.toString().trim(),
                                binding.inputDealerAddress.text.toString().trim(),
                                areaValue,
                                binding.inputContactName.text.toString().trim(),
                                segId,
                                binding.inputSaleMnthy.text.toString().trim(),
                                gradingValue,
                                binding.inputFeedback.text.toString().trim(),
                                binding.inputReminder.text.toString().trim(),
                                binding.etCreditDays.text.toString().trim(),
                                collectionList
                            )
                        )
                        responseCall.enqueue(addDealerResponse as Callback<AddDealerResponse>)


                    }
                }else if (binding.inputDealerMobileOptional.text.toString() != ""){
                    if (!binding.inputDealerMobileOptional.text.toString()
                            .matches(mobilePattern.toRegex())
                    ) {

                        showToastMessage("Invalid Mobile Number")
                    }else{
                       // showToastMessage("Success")
                        if (collectionList.size == 0) {

                            collectionList.add(
                                CollectionRequest(
                                    "","","","",""

                                )
                            )
                        }
                        showHud()
                        val apiInterface =
                            ApiClient.getInstance().client.create(ApiInterface::class.java)
                        val responseCall = apiInterface.addDealerInfo(
                            AddDealerRequestParams(
                                userId,
                                binding.inputDealerName.text.toString().trim(),
                                binding.inputDealerMobile.text.toString().trim(),
                                binding.inputDealerMobileOptional.text.toString().trim(),
                                binding.inputDealerAddress.text.toString().trim(),
                                areaValue,
                                binding.inputContactName.text.toString().trim(),
                                segId,
                                binding.inputSaleMnthy.text.toString().trim(),
                                gradingValue,
                                binding.inputFeedback.text.toString().trim(),
                                binding.inputReminder.text.toString().trim(),
                                binding.etCreditDays.text.toString().trim(),
                                collectionList
                            )
                        )
                        responseCall.enqueue(addDealerResponse as Callback<AddDealerResponse>)

                       // validateMobile(userId,binding.inputDealerMobile.text.toString().trim())

                    }
                } else if (binding.inputDealerMobile.text.toString() != "" &&
                    binding.inputDealerMobileOptional.text.toString() != "") {
                    if (!binding.inputDealerMobile.text.toString()
                            .matches(mobilePattern.toRegex())
                    ) {

                        showToastMessage("Invalid Mobile Number")
                    }/*else if (binding.inputDealerMobile.text.toString()
                            .matches(mobilePattern.toRegex())
                    ) {
                        validateMobileNumber(userId,binding.inputDealerMobile.text.toString().trim())

                       // showToastMessage("Invalid Mobile Number")
                    }*/else if (!binding.inputDealerMobileOptional.text.toString()
                            .matches(mobilePattern.toRegex())
                    ) {

                        showToastMessage("Invalid Mobile Number")
                    }/*else if (binding.inputDealerMobileOptional.text.toString()
                            .matches(mobilePattern.toRegex())
                    ) {
                        validateMobileNumber(userId,binding.inputDealerMobileOptional.text.toString().trim())

                    }*/else{
                        //showToastMessage("Success")
                        if (collectionList.size == 0) {

                            collectionList.add(
                                CollectionRequest(
                                    "","","","",""

                                )
                            )
                        }
                        showHud()
                        val apiInterface =
                            ApiClient.getInstance().client.create(ApiInterface::class.java)
                        val responseCall = apiInterface.addDealerInfo(
                            AddDealerRequestParams(
                                userId,
                                binding.inputDealerName.text.toString().trim(),
                                binding.inputDealerMobile.text.toString().trim(),
                                binding.inputDealerMobileOptional.text.toString().trim(),
                                binding.inputDealerAddress.text.toString().trim(),
                                areaValue,
                                binding.inputContactName.text.toString().trim(),
                                segId,
                                binding.inputSaleMnthy.text.toString().trim(),
                                gradingValue,
                                binding.inputFeedback.text.toString().trim(),
                                binding.inputReminder.text.toString().trim(),
                                binding.etCreditDays.text.toString().trim(),
                                collectionList
                            )
                        )
                        responseCall.enqueue(addDealerResponse as Callback<AddDealerResponse>)
                    }
                }else{
                    //showToastMessage("Success")
                    if (collectionList.size == 0) {

                        collectionList.add(
                            CollectionRequest(
                                "","","","",""

                            )
                        )
                    }
                    showHud()
                    val apiInterface =
                        ApiClient.getInstance().client.create(ApiInterface::class.java)
                    val responseCall = apiInterface.addDealerInfo(
                        AddDealerRequestParams(
                            userId,
                            binding.inputDealerName.text.toString().trim(),
                            binding.inputDealerMobile.text.toString().trim(),
                            binding.inputDealerMobileOptional.text.toString().trim(),
                            binding.inputDealerAddress.text.toString().trim(),
                            areaValue,
                            binding.inputContactName.text.toString().trim(),
                            segId,
                            binding.inputSaleMnthy.text.toString().trim(),
                            gradingValue,
                            binding.inputFeedback.text.toString().trim(),
                            binding.inputReminder.text.toString().trim(),
                            binding.etCreditDays.text.toString().trim(),
                            collectionList
                        )
                    )
                    responseCall.enqueue(addDealerResponse as Callback<AddDealerResponse>)
                }

            }
        }
        callApiSegment("Product Segment")
        callApiCompany1("Preference Make")
        callApiCompany2("Preference Make")
        callApiCompany3("Preference Make")
        callApiCategory1("Product Types")
        callApiCategory2("Product Types")
        callApiCategory3("Product Types")
        callApiPackage1("Packaging")
        callApiPackage2("Packaging")
        callApiPackage3("Packaging")

        callApiGrading("Grading")



        callApiArea(SharedPreferenceUtils.getLoggedInUserId(context as Context))


    }
    private fun validateMobileNumber(userId: String, mobile: String) {

        showHud()
        val apiInterface =
            ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.validateMobile(
            ValidateMobileRequestParams(  userId,mobile)
        )
        responseCall.enqueue(validateMobileNumberResponse as Callback<MessageResponse>)

    }
    private val validateMobileNumberResponse = object : NetworkCallBack<MessageResponse>() {
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<MessageResponse>) {
            hide()

            if (response.data!!.status == 1) {
                showToastMessage(response.data.respMessage)

            } else {

                showToastMessage(response.data.respMessage)
            }


        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
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
                var statList1: MutableList<String> = ArrayList()
                statList1.add("Select Area")
                var statList: MutableList<String> = ArrayList()
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

    private fun showCustomDatePicker(minStartDate: String = "") {
        var now = DateUtility.dateStrToCalendar(minStartDate)
        val year = now[Calendar.YEAR]
        val dpd: com.wdullaer.materialdatetimepicker.date.DatePickerDialog =
            com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
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

    companion object {
        const val TAG = "AddDealerFragment"
        const val DATE_PICKER = "DatePickerDialog"
    }


    fun callApiPackage1(type: String) {

        // showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(packageResponse1 as Callback<OrderVSQualityResponse>)

    }

    fun callApiPackage2(type: String) {

        // showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(packageResponse2 as Callback<OrderVSQualityResponse>)

    }

    fun callApiPackage3(type: String) {

        // showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(packageResponse3 as Callback<OrderVSQualityResponse>)

    }


    var packagedataList1: List<DropdownDetails> = emptyList<DropdownDetails>()
    var packagedataList2: List<DropdownDetails> = emptyList<DropdownDetails>()
    var packagedataList3: List<DropdownDetails> = emptyList<DropdownDetails>()
    val packageResponse1 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                println()
                packagedataList1 = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Packaging")
                for (i in packagedataList1) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerPackage1.adapter = adapter2

                binding.spinnerPackage1.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerPackage1.selectedItem == "Select Packaging") {

                            } else {
                                val x = packagedataList1[position - 1]
                                package1 = x.Exp_Head_Id!!
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

    val packageResponse2 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                println()
                packagedataList2 = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Packaging")
                for (i in packagedataList2) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerPackage2.adapter = adapter2

                binding.spinnerPackage2.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerPackage2.selectedItem == "Select Packaging") {

                            } else {
                                val x = packagedataList2[position - 1]
                                package2 = x.Exp_Head_Id!!
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


    val packageResponse3 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                println()
                packagedataList3 = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Packaging")
                for (i in packagedataList3) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerPackage3.adapter = adapter2

                binding.spinnerPackage3.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerPackage3.selectedItem == "Select Packaging") {

                            } else {
                                val x = packagedataList3[position - 1]
                                package1 = x.Exp_Head_Id!!
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


    fun callApiCategory1(type: String) {

        // showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(catResponse1 as Callback<OrderVSQualityResponse>)

    }

    fun callApiCategory2(type: String) {

        // showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(catResponse3 as Callback<OrderVSQualityResponse>)

    }

    fun callApiCategory3(type: String) {

        //showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(catResponse2 as Callback<OrderVSQualityResponse>)

    }


    var catdataList1: List<DropdownDetails> = emptyList<DropdownDetails>()
    var catdataList2: List<DropdownDetails> = emptyList<DropdownDetails>()
    var catdataList3: List<DropdownDetails> = emptyList<DropdownDetails>()
    val catResponse1 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                println()
                catdataList1 = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Product Type")
                for (i in catdataList1) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerCategory1.adapter = adapter2

                binding.spinnerCategory1.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerCategory1.selectedItem == "Select Product Type") {

                            } else {
                                val x = catdataList1[position - 1]
                                spinner1Grade = x.Exp_Head_Id!!
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
    val catResponse2 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                println()
                catdataList2 = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Product Type")
                for (i in catdataList2) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerCategory2.adapter = adapter2
                binding.spinnerCategory2.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerCategory2.selectedItem == "Select Product Type") {

                            } else {
                                val x = catdataList2[position - 1]
                                spinner2Grade = x.Exp_Head_Id!!
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
    val catResponse3 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                catdataList3 = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Product Type")
                for (i in catdataList3) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerCategory3.adapter = adapter2
                binding.spinnerCategory3.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerCategory3.selectedItem == "Select Product Type") {

                            } else {
                                val x = catdataList3[position - 1]
                                spinner3Grade = x.Exp_Head_Id!!
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

    private fun callApiSegment(type: String) {
         showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(segmentResponse as Callback<OrderVSQualityResponse>)

    }

    private fun callApiGrading(type: String) {
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(gradeResponse as Callback<OrderVSQualityResponse>)


    }

    private fun callApiCompany1(type: String) {
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(prefResponseResponse1 as Callback<OrderVSQualityResponse>)


    }

    private fun callApiCompany2(type: String) {
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(prefResponseResponse3 as Callback<OrderVSQualityResponse>)


    }

    private fun callApiCompany3(type: String) {
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(prefResponseResponse2 as Callback<OrderVSQualityResponse>)


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

    var dataList: List<DropdownDetails> = emptyList<DropdownDetails>()
    val segmentResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                dataList = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Segment")
                for (i in dataList) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerSegment.adapter = adapter2

                binding.spinnerSegment.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                            if (binding.spinnerSegment.selectedItem == "Select Segment") {

                            } else {

                                val x = dataList[position - 1]
                                segId = x.Exp_Head_Id!!
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

    private var gradedataList: List<DropdownDetails> = emptyList<DropdownDetails>()

    private val gradeResponse = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                gradedataList = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Grading")
                for (i in gradedataList) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerGrading.adapter = adapter2
                binding.spinnerGrading.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerGrading.selectedItem == "Select Grading") {

                            } else {
                                val x = gradedataList[position - 1]
                                gradingValue = x.Exp_Head_Id!!
                                /*Toast.makeText(activity, "gradingValue" + gradingValue, Toast.LENGTH_SHORT)
                                    .show()*/

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


    private var prefdataList1: List<DropdownDetails> = emptyList<DropdownDetails>()
    private var prefdataList2: List<DropdownDetails> = emptyList<DropdownDetails>()
    private var prefdataList3: List<DropdownDetails> = emptyList<DropdownDetails>()

    private val prefResponseResponse1 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                prefdataList1 = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Preferred Company")
                for (i in prefdataList1) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerCompany1.adapter = adapter2
                binding.spinnerCompany1.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerCompany1.selectedItem == "Select Preferred Company") {

                            } else {
                                val x = prefdataList1[position - 1]
                                spinner1Company = x.Exp_Head_Id!!

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

    private val prefResponseResponse2 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                prefdataList2 = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Preferred Company")
                for (i in prefdataList2) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerCompany2.adapter = adapter2
                binding.spinnerCompany2.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerCompany2.selectedItem == "Select Preferred Company") {

                            } else {
                                val x = prefdataList2[position - 1]
                                spinner2Company = x.Exp_Head_Id!!

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
    private val prefResponseResponse3 = object : NetworkCallBack<OrderVSQualityResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<OrderVSQualityResponse>
        ) {
            response.data?.status?.let { status ->

                hide()
                prefdataList3 = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Preferred Company")
                for (i in prefdataList3) {
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList
                    )
                }
                binding.spinnerCompany3.adapter = adapter2
                binding.spinnerCompany3.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (binding.spinnerCompany3.selectedItem == "Select Preferred Company") {

                            } else {
                                val x = prefdataList3[position - 1]
                                spinner3Company = x.Exp_Head_Id!!


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


    private val addDealerResponse = object : NetworkCallBack<AddDealerResponse>() {
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AddDealerResponse>) {

            if (response.data!!.status == 1) {
                hide()

                //showToastMessage("Add Dealer Successfully...")
                var dialog = Dialog(context!!)
                dialog.setContentView(R.layout.add_dialog_view)
                val handler = Handler()
                val runnable = Runnable {
                    if (dialog.isShowing()) {
                        dialog.dismiss()
                        val navDirection =
                            AddDealerFragmentDirections.actionAddDealerFragmentToDealerImageFragment(
                                response.data.DD_ID.toString(),""
                            )
                        Navigation.findNavController(binding.btnAdd).navigate(navDirection)
                    } else {
                        dialog.show()
                    }
                }
                handler.postDelayed(runnable, 1000)
                dialog.show()


                // activity!!.onBackPressed()

            } else {
                hide()

                showToastMessage(response.data.respMessage)
            }


        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    override fun onDateSet(
        view: com.wdullaer.materialdatetimepicker.date.DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, monthOfYear, dayOfMonth)
        val subDateString: String = DateUtility.getStringDateFromTimestamp(
            (tempDate.time),
            DateUtility.YYYY_DASH_MM_DASH_DD
        )
        currentDatePicketParentView?.setText(subDateString)
    }

}


