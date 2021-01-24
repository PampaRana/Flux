package com.velectico.rbm.payment.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TableRow
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentPaymentHistoryBinding
import com.velectico.rbm.databinding.FragmentPaymentListFragmentBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.payment.adapter.PaymentHistoryAdapter
import com.velectico.rbm.payment.adapter.PaymentOutStandAdapter
import com.velectico.rbm.payment.models.*
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PaymentHistoryFragment : BaseFragment() {
    private lateinit var binding: FragmentPaymentHistoryBinding;
    var userId = ""
    var dealerId = "0"
    var distribId = "0"
    var role=""
    var paymentHistoryAdapter: PaymentHistoryAdapter? = null
    //private var paymentHistoryList : List<PaymentHistoryDetails> = emptyList()

    override fun getLayout(): Int {
        return R.layout.fragment_payment_history
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentPaymentHistoryBinding

        binding.spinnerDeal.visibility = View.GONE

        role= SharedPreferencesClass.retriveData(context as Context,"UM_Role").toString()

        if (role=="L") {
            //Sales Lead
            if (RBMLubricantsApplication.globalRole == "Team") {
                userId = GloblalDataRepository.getInstance().teamUserId
            } else {
                userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            }

            if (RBMLubricantsApplication.fromBeat == "Beat") {
                binding.llSpinnerDealDis.visibility = View.GONE
                binding.llSpinnerType.visibility = View.GONE
                dealerId = DataConstant.dealerId
                distribId = DataConstant.distributorId
                showToastMessage("dealId" + dealerId)
                showToastMessage("distId" + distribId)
                callHistoryList()

            } else {
                dealerId = DataConstant.dealerId
                distribId = DataConstant.distributorId
                callHistoryList()

                binding.llSpinnerDealDis.visibility = View.GONE
                binding.llSpinnerType.visibility = View.GONE
            }

        }else if (role=="P"){
            //Sales Person
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            if (RBMLubricantsApplication.fromBeat == "Beat") {
                binding.llSpinnerDealDis.visibility = View.GONE
                binding.llSpinnerType.visibility = View.GONE
                dealerId = DataConstant.dealerId
                distribId = DataConstant.distributorId
                showToastMessage("dealId" + dealerId)
                showToastMessage("distId" + distribId)
                callHistoryList()
            } else {
                dealerId = DataConstant.dealerId
                distribId = DataConstant.distributorId
                binding.llSpinnerDealDis.visibility = View.GONE
                binding.llSpinnerType.visibility = View.GONE
                callHistoryList()
                showToastMessage("dealId" + dealerId)
                showToastMessage("distId" + distribId)

            }

        }else if (role =="R"){
            //Dealer
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            dealerId=SharedPreferencesClass.retriveData(context as Context,"UM_ID").toString()
            distribId=""
            callDealDistPaymentHistoryList(userId,role)

        }else if (role =="D"){
            //Distributor

            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            distribId=SharedPreferencesClass.retriveData(context as Context,"UM_ID").toString()
            dealerId=""
            callDealDistPaymentHistoryList(userId,role)


        }else{

            //mechanic
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)

        }



        /*binding.spinnerType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (binding.spinnerType.selectedItem.toString().equals("Dealer")) {
                        //showToastMessage("Select Payment Mode")
                        callDealApi()

                    } else {
                        callDistApi()


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

                        } else {
                            val x = dealNameList[position - 1]
                            dealerId = x.UM_ID!!
                            distribId = ""
                            showToastMessage("dealId" + dealerId)
                            showToastMessage("distId" + distribId)
                            callHistoryList()
                        }


                    } else if (binding.spinnerType.selectedItem == "Distributor") {
                        if (binding.spinnerDealDis.selectedItem == "Select Distributor Name") {

                        } else {
                            val x = distNameList[position - 1]
                            distribId = x.UM_ID!!
                            dealerId = ""
                            showToastMessage("dealId" + dealerId)
                            showToastMessage("distId" + distribId)
                            callHistoryList()
                        }

                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }*/


    }

    private fun callDealDistPaymentHistoryList(userId: String, role: String) {

        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getDealDistPaymentList(
            DealDistPaymentParams(userId, role)
        )
        responseCall.enqueue(paymentHistoryResponse as Callback<PaymentHistoryResponse>)

    }


    /*private var distNameList: List<DistDropdownDetails> = emptyList<DistDropdownDetails>()
    private val distNameResponse = object : NetworkCallBack<DistListResponse>() {
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DistListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DistList.toString())
                hide()
                distNameList = response.data.DistList
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Distributor Name")
                for (i in distNameList) {
                    statList.add(i.UM_Name!!)
                    distribId = i.UM_ID!!
                }
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

    }*/
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

   /* fun callDistApi() {
        showHud()
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
                var statList: MutableList<String> = ArrayList()
                statList.add("Select Dealer Name")

                for (i in dealNameList) {
                    statList.add(i.UM_Name!!)
                    dealerId = i.UM_ID!!
                }
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

    }*/

    /*fun callDealApi() {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(userId)
        )

        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

    }*/

    private fun callHistoryList() {

        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getPaymentHistoryList(
            PaymentHistoryRequestParams(userId, dealerId, distribId)
        )
        responseCall.enqueue(paymentHistoryResponse as Callback<PaymentHistoryResponse>)
    }

    private val paymentHistoryResponse = object : NetworkCallBack<PaymentHistoryResponse>() {
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<PaymentHistoryResponse>
        ) {
            hide()
            response.data?.status?.let { status ->
                // paymentHistoryList .toMutableList().clear()

                if (response.data.count > 0) {
                    binding.ptitle.visibility = View.VISIBLE
                    if (response.data.count == 1) {
                        binding.ptitle.text =
                            "Last " + response.data.count.toString() + " Paid Transaction"

                    } else {
                        binding.ptitle.text =
                            "Last " + response.data.count.toString() + " Paid Transactions"

                    }
                    binding.card.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
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

                        tv_amount_value.text = "₹ " + i.tran_amount
                        val inpFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        val outputformat = SimpleDateFormat("dd-MMM-yy", Locale.US);
                        val stdate = DateUtils.parseDate(i.payDate, inpFormat, outputformat)
                        tv_date_value.text = stdate
                        tv_invoice_no_value.text = i.PH_Invoice_No
                        binding.mTableLayout.addView(tableRow);

                    }

                    /*paymentHistoryList = response.data.paymentOutStandingList!!.toMutableList()
                    //for (i in paymentHistoryList)
                    paymentHistoryAdapter = PaymentHistoryAdapter(
                        context,
                        paymentHistoryList
                    )
                    binding.rvPaymentList.setAdapter(paymentHistoryAdapter)*/


                } else {

                    //showToastMessage("No data found")
                    binding.card.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.ptitle.visibility = View.GONE
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }

    }


}