package com.velectico.rbm.redeem.view

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RedeemViewModel
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.RedeemFragmentBinding
import com.velectico.rbm.databinding.RowRedeemListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.payment.view.FragmentPaymentListDirections
import com.velectico.rbm.redeem.adapter.RedeemListAdapter
import com.velectico.rbm.redeem.model.GetRedeemDetailsRequestParams
import com.velectico.rbm.redeem.model.GetRedeemDetailsResponse
import com.velectico.rbm.redeem.model.RedeemInfo
import com.velectico.rbm.redeem.model.RedeemListDetails
import com.velectico.rbm.reminder.model.ReminderListRequestParams
import com.velectico.rbm.reminder.model.ReminderListResponse
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class Redeem : BaseFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: RedeemFragmentBinding;
    private  var beatList : List<RedeemListDetails> = emptyList()
    private lateinit var adapter: RedeemListAdapter
    private var cuurentDatePicketParentView : com.google.android.material.textfield.TextInputEditText? = null;
    var startDate = ""
    var enddate = ""
    override fun getLayout(): Int {
        return R.layout.redeem_fragment
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as RedeemFragmentBinding
        binding.totalPoints.visibility = View.GONE
        //beatList = RedeemInfo().getDummyBeatList()
        setUpRecyclerView()
        binding.searchBtn.setOnClickListener(){
            if (binding.paymentFromEt.text.toString().isEmpty() || binding.leaveFromEt.text.toString().isEmpty() ){
                Toast.makeText(activity,"Date is Manadatory", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var inputformat =  SimpleDateFormat(DateUtility.dd_MM_yy, Locale.US);
            var  outputformat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
            startDate = DateUtils.parseDate(binding.paymentFromEt.text.toString(),inputformat,outputformat)
            enddate = DateUtils.parseDate(binding.leaveFromEt.text.toString(),inputformat,outputformat)
            callApiList(startDate,enddate)
        }
        binding.paymentFromEt?.setOnClickListener {
            cuurentDatePicketParentView = this.binding.paymentFromEt;
            showDatePickerDialog()
        }
        binding.leaveFromEt?.setOnClickListener {
            cuurentDatePicketParentView = this.binding.leaveFromEt;
            showDatePickerDialog1()
        }
    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        DatePickerDialog(requireActivity(), this, year, month, dayOfMonth).show()
    }
    private fun showDatePickerDialog1() {
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
        if( cuurentDatePicketParentView == binding.paymentFromEt) {
            binding.paymentFromEt?.setText(subDateString)
        }
        else if ( cuurentDatePicketParentView == binding.leaveFromEt) {
            binding.leaveFromEt?.setText(subDateString)
        }

    }


    private fun setUpRecyclerView() {

        adapter = RedeemListAdapter(object : RedeemListAdapter.IBeatListActionCallBack{
            override fun moveToBeatTaskDetails(position: Int, beatTaskId: String?,binding: RowRedeemListBinding) {

            }
        });

        binding.rvRedeemList.adapter = adapter
        adapter.beatList = beatList
    }
    fun callApiList(date1:String,date2:String){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getRedeemList(

            GetRedeemDetailsRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),date1,date2)
        )
        responseCall.enqueue(GetRedeemDetailsResponse as Callback<GetRedeemDetailsResponse>)
    }

    private val GetRedeemDetailsResponse = object : NetworkCallBack<GetRedeemDetailsResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<GetRedeemDetailsResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test0000","OrderHistoryDetailsResponse status="+response.data)

                beatList.toMutableList().clear()
                if (response.data.count > 0){
                    beatList = response.data.RedeemListDetail!!.toMutableList()
                    binding.totalPoints.visibility = View.VISIBLE
                    binding.totalPoints.text = "Current Balance :" + response.data.TotalPoint
                    binding.tvNoData.visibility = View.GONE
                    binding.rvRedeemList.visibility = View.VISIBLE
                    setUpRecyclerView()
                    // binding.rvComplaintList.visibility = View.VISIBLE
                }
                else{
                    showToastMessage("No data found")
                    //binding.resolvButton.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.rvRedeemList.visibility = View.GONE
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
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

}
