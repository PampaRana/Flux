package com.velectico.rbm.beats.views

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatReportListAdapter
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentBeatReportListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class BeatReportListFragment : BaseFragment() , DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentBeatReportListBinding
    private var cuurentDatePicketParentView : com.google.android.material.textfield.TextInputEditText? = null;
    private var reportList : List<BeatReportListDetails> = emptyList()
    private lateinit var adapter: BeatReportListAdapter
    var taskDetails = BeatTaskDetails()
    var dlrDtl = DealerDetails()
    var startDate = "2020-07-28"
    var enddate = "2020-07-31"
    var userId = ""
    var  dealerId=""
    var taskId=""
    var distributorId=""
    var dateFormat: DateFormat? = null
    var date: Date? = null
    var today_date: String? = null
    override fun getLayout(): Int {
        return R.layout.fragment_beat_report_list

    }

    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentBeatReportListBinding
        if (RBMLubricantsApplication.globalRole == "Team" ){
            userId = GloblalDataRepository.getInstance().teamUserId
            binding.fab.hide()
        }
        else{
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            binding.fab.show()
        }
        taskDetails = arguments!!.get("taskDetail") as BeatTaskDetails
        dlrDtl = arguments!!.get("dealerDetails") as DealerDetails
        /*dateFormat = SimpleDateFormat("yyyy-MM-dd")
        date = Date()
        today_date = dateFormat!!.format(date)

        val myDate = (dateFormat as SimpleDateFormat).parse(today_date)
        val calendar = Calendar.getInstance()
        calendar.time = myDate
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val newDate = calendar.time
        val start_date = (dateFormat as SimpleDateFormat).format(newDate)*/


        if (taskDetails.taskId!=null){
            dealerId=taskDetails.dealerId.toString()
            distributorId=taskDetails.distribId.toString()
            taskId=taskDetails.taskId.toString()
            binding.paymentFromEt.setText("")
            binding.leaveFromEt.setText("")
            callDefaultBeatReportList(userId, dealerId, distributorId)
            //setUpRecyclerView()

        }else{
            dealerId= DataConstant.dealerId
            distributorId= DataConstant.distributorId
            taskId= DataConstant.taskId
            binding.paymentFromEt.setText("")
            binding.leaveFromEt.setText("")
            callDefaultBeatReportList(userId, dealerId, distributorId)
           // callBeatReportList(startDate,today_date.toString())
            //setUpRecyclerView()
        }

        //showToastMessage("Deal DisId"+DataConstant.dealerId+"\n"+DataConstant.distributorId)


        binding.fab.setOnClickListener {
             moveToBeatReport()
        }
        binding.paymentFromEt?.setOnClickListener {
            cuurentDatePicketParentView = this.binding.paymentFromEt;
            showDatePickerDialog()
        }
        binding.leaveFromEt?.setOnClickListener {
            cuurentDatePicketParentView = this.binding.leaveFromEt;
            showDatePickerDialog1()
        }
        binding.searchBtn?.setOnClickListener {
            if (binding.paymentFromEt.text.toString().isEmpty() || binding.leaveFromEt.text.toString().isEmpty() ){
                Toast.makeText(activity,"Date is Manadatory", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var inputformat =  SimpleDateFormat(DateUtility.dd_MM_yy, Locale.US);
            var  outputformat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
            startDate = DateUtils.parseDate(binding.paymentFromEt.text.toString(),inputformat,outputformat)
            enddate = DateUtils.parseDate(binding.leaveFromEt.text.toString(),inputformat,outputformat)
            if (taskDetails.taskId!=null) {
                callApiBeatReportList(startDate, enddate)
            }else{
                callBeatReportList(startDate,enddate)

            }
        }


    }

    private fun callDefaultBeatReportList(userId: String, dealerId: String, distributorId: String) {

        showHud()

        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getDefaultBeatReportList(
            //BeatAllOrderListRequestParams("7001507620","61")

            BeatReportDefaultListRequestParams(userId,dealerId, distributorId)

            //BeatReportListRequestParams("7001507620","109","61","0","2020-07-26","2020-07-26")
        )
        responseCall.enqueue(BeatReportListDetailsResponse as Callback<BeatReportListDetailsResponse>)
    }


    private fun moveToBeatReport(){
        val navDirection =  BeatReportListFragmentDirections.actionBeatReportListFragmentToBeatReportFragment(taskDetails,dlrDtl)
        Navigation.findNavController(binding.fab).navigate(navDirection)
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
        adapter = BeatReportListAdapter();
        binding.rvBeatComplaintList.adapter = adapter
        adapter.teamList = reportList
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
    fun callBeatReportList(date1:String,date2:String){
        showHud()

        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBeatReportList(
            //BeatAllOrderListRequestParams("7001507620","61")

            BeatReportListRequestParams(userId,DataConstant.taskId,DataConstant.dealerId,DataConstant.distributorId,date1,date2)

            //BeatReportListRequestParams("7001507620","109","61","0","2020-07-26","2020-07-26")
        )
        responseCall.enqueue(BeatReportListDetailsResponse as Callback<BeatReportListDetailsResponse>)
    }
    fun callApiBeatReportList(date1:String,date2:String){
        showHud()

        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBeatReportList(
            //BeatAllOrderListRequestParams("7001507620","61")

            BeatReportListRequestParams(userId,taskDetails.taskId.toString(),taskDetails.dealerId.toString(),taskDetails.distribId.toString(),date1,date2)

            //BeatReportListRequestParams("7001507620","109","61","0","2020-07-26","2020-07-26")
        )
        responseCall.enqueue(BeatReportListDetailsResponse as Callback<BeatReportListDetailsResponse>)
    }

    private val BeatReportListDetailsResponse = object : NetworkCallBack<BeatReportListDetailsResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<BeatReportListDetailsResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test444","OrderHistoryDetailsResponse status="+response.data)
                reportList.toMutableList().clear()
                if (response.data.count > 0){
                    reportList = response.data.BeatReportList!!.toMutableList()
                    binding.rvBeatComplaintList.visibility = View.VISIBLE
                    binding.tvNoData.visibility=View.GONE
                    setUpRecyclerView()
                }
                else{
                    showToastMessage("No data found")
                    binding.tvNoData.visibility=View.VISIBLE
                    binding.rvBeatComplaintList.visibility=View.GONE
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }

    }

}
