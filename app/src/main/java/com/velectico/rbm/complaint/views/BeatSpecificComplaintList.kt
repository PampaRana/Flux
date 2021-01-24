package com.velectico.rbm.complaint.views

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.beats.model.DealerDetails
import com.velectico.rbm.beats.views.BeatTaskDealerDetailsFragmentDirections
import com.velectico.rbm.complaint.adapter.ComplaintListAdapter
import com.velectico.rbm.complaint.model.*
import com.velectico.rbm.databinding.FragmentBeatSpecificComplaintListBinding
import com.velectico.rbm.databinding.RowComplaintListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback

/**
 * A simple [Fragment] subclass.
 */
class BeatSpecificComplaintList : BaseFragment() {

    private lateinit var binding: FragmentBeatSpecificComplaintListBinding;
    private var complaintList : List<ComplainListDetails> = emptyList()
    private lateinit var adapter: ComplaintListAdapter
    var orderStatus = "O"
    var taskDetails = BeatTaskDetails()
    var dlrDtl = DealerDetails()
    var userId = ""
    var  dealerId=""
    var taskId=""
    var distributorId=""
    private var complaintList11 = ComplainListDetails()

    override fun getLayout(): Int {
        return R.layout.fragment_beat_specific_complaint_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatSpecificComplaintListBinding
        if (RBMLubricantsApplication.globalRole == "Team" ){
            userId = GloblalDataRepository.getInstance().teamUserId
            binding.fab.hide()
        }
        else{
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            binding.fab.show()
        }
        taskDetails = arguments!!.get("taskDetails") as BeatTaskDetails
        dlrDtl = arguments!!.get("dealerDetails") as DealerDetails
        //showToastMessage(taskDetails.toString())
        //binding.delrName.text = taskDetails.dealerName.toString()
        binding.tvProdNetPrice.text = dlrDtl.dealerPhone.toString()
        binding.tvProdTotalPrice.text = dlrDtl.DM_Contact_Person.toString()
        if (DataConstant.orderAmt!="") {
            binding.tvOrdrAmt.text="₹ "+DataConstant.orderAmt
        }else{
            binding.tvOrdrAmt.text="₹ 0"

        }
        if (DataConstant.collectionAmt!="") {
            binding.collectionAmt33.text = "₹ " + DataConstant.collectionAmt

        }else{
            binding.collectionAmt33.text = "₹ 0"

        }

        /*binding.tvOrdrAmt.text = "₹" +dlrDtl.orderAmt.toString()
        binding.collectionAmt33.text = "₹" +dlrDtl.collectionAmt.toString()*/
        binding.gradeval.text = DataConstant.dealerGrade
        binding.type.text = DataConstant.nameValue
        binding.delrName.text = DataConstant.name
        if (taskDetails.taskId!=null) {
            dealerId=taskDetails.dealerId.toString()
            distributorId=taskDetails.distribId.toString()
            taskId=taskDetails.taskId.toString()
            /*if (taskDetails.distribName != null) {
                binding.gradeval.text = taskDetails.distribGrade
                binding.type.text = "Distributor"
            } else {
                binding.gradeval.text = taskDetails.dealerGrade
                binding.type.text = "Dealer"
            }*/
            callApiList()
        }else{
            dealerId=DataConstant.dealerId
            distributorId=DataConstant.distributorId
            taskId=DataConstant.taskId

            callComplaintApiList()
        }
       // complaintList = ComplaintModel().getDummyComplaintList()
        binding.resolvButton.setOnClickListener{
            orderStatus = "C"
            callApiList()
            //setUpRecyclerView()
        }
        binding.pendButton.setOnClickListener{
            orderStatus = "O"
            callApiList()
            //setUpRecyclerView()
        }

        binding.fab.setOnClickListener {
                   moveToCreateComplaint()
               }
       // setUpRecyclerView()
        //showToastMessage(userId+"\n"+DataConstant.taskId+"\n"+DataConstant.dealerId+"\n"+DataConstant.distributorId+"\n"+""+"\n"+orderStatus)

    }

    private fun moveToCreateComplaint(){
        val navDirection =  BeatSpecificComplaintListDirections.actionBeatSpecificComplaintListToAddComplaintFragment(taskDetails,dlrDtl,complaintList11)
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }

    private fun setUpRecyclerView() {

        adapter = ComplaintListAdapter(object : ComplaintListAdapter.IComplaintListActionCallBack{
            override fun moveToComplainDetails(position: Int, beatTaskId: String?,binding: RowComplaintListBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection = BeatSpecificComplaintListDirections.actionBeatSpecificComplaintListToAddComplaintFragment(taskDetails,dlrDtl,complaintList[position])
                Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)

            }
        }, context as Context)
        binding.rvComplaintList.adapter = adapter
        adapter.complaintList = complaintList;
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
    fun callComplaintApiList(){
        showHud()

        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getComplaintList(
            //BeatAllOrderListRequestParams("7001507620","61")
            //ComplaintListRequestParams("7001507620","109","61","0","",orderStatus)

            ComplaintListRequestParams(userId,DataConstant.taskId,DataConstant.dealerId,DataConstant.distributorId,"",orderStatus)
        )
        responseCall.enqueue(ComplaintListResponse as Callback<ComplaintListResponse>)
    }
    fun callApiList(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getComplaintList(
            //BeatAllOrderListRequestParams("7001507620","61")
            //ComplaintListRequestParams("7001507620","109","61","0","",orderStatus)

            ComplaintListRequestParams(userId,taskDetails.taskId.toString(),taskDetails.dealerId.toString(),taskDetails.distribId.toString(),"",orderStatus)
        )
        responseCall.enqueue(ComplaintListResponse as Callback<ComplaintListResponse>)
    }

    private val ComplaintListResponse = object : NetworkCallBack<ComplaintListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ComplaintListResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test0000","OrderHistoryDetailsResponse status="+response.data)
                complaintList.toMutableList().clear()
                if (response.data.count > 0){
                    complaintList = response.data.ComplaintList!!.toMutableList()
                    binding.rvComplaintList.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
                    setUpRecyclerView()
                   // binding.rvComplaintList.visibility = View.VISIBLE
                }
                else{
                    showToastMessage("No data found")
                    //binding.resolvButton.visibility = View.GONE
                    //binding.pendButton.visibility = View.GONE
                    binding.rvComplaintList.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }

    }
}
