package com.velectico.rbm.leave.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentLeaveListBinding
import com.velectico.rbm.leave.model.LeaveListModel
import com.velectico.rbm.leave.model.LeaveListRequest
import com.velectico.rbm.leave.model.LeaveListResponse
import com.velectico.rbm.leave.view.adapter.LeaveListAdapter
import com.velectico.rbm.leave.viewmodel.LeaveViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Created by mymacbookpro on 2020-04-26
 * TODO: Add a class header comment!
 */
class LeaveListFragment : BaseFragment(){

    private lateinit var binding:FragmentLeaveListBinding
    private lateinit var leaveViewModel: LeaveViewModel
    private var leaveListArray:ArrayList<LeaveListModel> = ArrayList<LeaveListModel>()


    override fun getLayout(): Int = R.layout.fragment_leave_list

    @SuppressLint("RestrictedApi", "SimpleDateFormat", "NewApi")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentLeaveListBinding
        initHud()
        if (RBMLubricantsApplication.globalRole == "Team" ){
            binding.applyLeavesFB.visibility = View.GONE

        }
        binding.applyLeavesFB.setOnClickListener {
            activity?.let {

                val navController = Navigation.findNavController(it, R.id.nav_host_fragment)
                val action = LeaveListFragmentDirections.leaveListToApplyLeave(CREATE, "0")
                navController.popBackStack(R.id.leaveListFragment, false)
                navController.navigate(action)
            }
        }
      //  observeViewModelData()
        //getLeaveListFromServer()
        getLeaveList()
        /*val myFormat = SimpleDateFormat("yyyy-MM-dd")
        val inputString1 = "2019-08-20"
        val inputString2 = "2019-08-22"

        try {
            val date1: Date = myFormat.parse(inputString1)
            val date2: Date = myFormat.parse(inputString2)
            val diff: Long = (date2.getTime() - date1.getTime())
            val days= TimeUnit.DAYS.convert(
                diff,
                TimeUnit.MILLISECONDS
            ).toInt()+1

            System.out.println(
                "Days: " + TimeUnit.DAYS.convert(
                    diff,
                    TimeUnit.MILLISECONDS
                )
            )
            showToastMessage("Days"+days.toString())


        } catch (e: ParseException) {
            e.printStackTrace()
        }*/

        /*val sdformat = SimpleDateFormat("yyyy-MM-dd")
        val d1 = sdformat.parse("2019-08-20")
        val d2 = sdformat.parse("2019-08-20")
        println("The date 1 is: " + sdformat.format(d1))
        println("The date 2 is: " + sdformat.format(d2))
        if (d1.compareTo(d2) > 0) {
            showToastMessage("Date 1 occurs after Date 2")
            println("Date 1 occurs after Date 2")
        } else if (d1.compareTo(d2) < 0) {
            showToastMessage("Date 1 occurs before Date 2")

           // println("Date 1 occurs before Date 2")
        } else if (d1.compareTo(d2) === 0) {
            showToastMessage("Both dates are equal")

            println("Both dates are equal")
        }*/



    }

    private fun getLeaveListFromServer() {
        binding?.progressLayout?.visibility = View.VISIBLE
        leaveViewModel.getLeaveListAPICall(SharedPreferenceUtils.getLoggedInUserId(context as Context))
    }

    private fun deleteLeaveFromServer(leaveId:Int){
        binding?.progressLayout?.visibility = View.VISIBLE
        val userId : String = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        leaveViewModel.deleteLeaveAPICall(userId,leaveId)
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

    fun getLeaveList(){
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
        val responseCall = apiInterface.getLeaveList(
            LeaveListRequest(userId
        ))
        responseCall.enqueue(createResponse as Callback<LeaveListResponse>)

    }


    val createResponse = object : NetworkCallBack<LeaveListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<LeaveListResponse>) {
            hide()
            if (response.data!!.status==1){
                binding.tvNoData.visibility=View.GONE
                binding.leaveList.visibility=View.VISIBLE
                leaveListArray =  response.data?.leaveDetails as ArrayList<LeaveListModel>
                binding.leaveList.layoutManager = activity?.let { LinearLayoutManager(it) }
                binding.leaveList.adapter =
                    LeaveListAdapter(
                        response.data?.leaveDetails,
                        onClickListener = { view, pos ,action ->
                            onRowItemClick(view, pos,action)
                        })
            }else{
                binding.tvNoData.visibility=View.VISIBLE
                binding.leaveList.visibility=View.GONE

            }
            /*response.data?.status?.let { status ->
                hide()
                leaveListArray =  response.data?.leaveDetails as ArrayList<LeaveListModel>
                binding.leaveList.layoutManager = activity?.let { LinearLayoutManager(it) }
                binding.leaveList.adapter =
                    LeaveListAdapter(
                        response.data?.leaveDetails,
                        onClickListener = { view, pos ,action ->
                            onRowItemClick(view, pos,action)
                        })

            }*/

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }

    private fun setUpRecyclerView(){
        binding.leaveList.layoutManager = activity?.let { LinearLayoutManager(it) }
        binding.leaveList.adapter =
            LeaveListAdapter(
                leaveListArray,
                onClickListener = { view, pos ,action ->
                    onRowItemClick(view, pos,action)
                })
    }

    private fun observeViewModelData() {
        leaveViewModel = LeaveViewModel.getInstance(activity as BaseActivity)
        leaveViewModel.leaveListResponse.observe(viewLifecycleOwner, Observer { listResponse ->
            listResponse?.let {
              //  leaveListArray = leaveViewModel.leaveListResponse.value?.leaveDetails as ArrayList<LeaveListModel>
               // setUpRecyclerView();
            }
        })

        leaveViewModel.deleteLeaveResponse.observe(viewLifecycleOwner, Observer { listResponse ->
            listResponse?.let {
                (activity as BaseActivity).showAlertDialog(listResponse.respMessage)
              //  getLeaveListFromServer()

            }
        })

        leaveViewModel.loading.observe(viewLifecycleOwner, Observer { progress ->
            binding?.progressLayout?.visibility = if(progress) View.VISIBLE else View.GONE
        })

        leaveViewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            (activity as BaseActivity).showAlertDialog(it.errorMessage ?: getString(R.string.no_data_available))
        })
    }

    private fun onRowItemClick(view: View, pos: Int , action :Int) {
        activity?.let {
            if(ACTION_LEAVE_DELETE == action)
            {
                deleteLeaveFromServer(leaveListArray[pos].leaveID!!.toInt())
            }
            else if(ACTION_LEAVE_EDIT == action){
                DataConstant.leaveStatus=leaveListArray[pos].LD_Status.toString()
                GloblalDataRepository.getInstance().leaveListModel = leaveListArray[pos]
                val navController = Navigation.findNavController(it, R.id.nav_host_fragment)
                val action = LeaveListFragmentDirections.leaveListToApplyLeave(EDIT, pos.toString())
                navController.popBackStack(R.id.leaveListFragment, false)
                navController.navigate(action)
            }

        }
    }

    companion object{
        const val TAG = "LeaveListFragment"
        const val CREATE = "Create"
        const val EDIT = "Edit"
        const val ACTION_LEAVE_DELETE = 1
        const val ACTION_LEAVE_EDIT = 2
    }
}