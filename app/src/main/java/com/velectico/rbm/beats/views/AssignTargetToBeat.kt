package com.velectico.rbm.beats.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatTargetAssignmentAdapter
import com.velectico.rbm.beats.adapters.orderDetailsBeatTask
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentAssignTargetToBeatBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

/**
 * A simple [Fragment] subclass.
 */
class AssignTargetToBeat : BaseFragment() {

    private lateinit var binding: FragmentAssignTargetToBeatBinding;
    private lateinit var beatList: List<DealDistMechList>
    private lateinit var adapter: BeatTargetAssignmentAdapter
    var startDate = ""
    var endDate = ""
    var userUmId = ""
    override fun getLayout(): Int {
        return R.layout.fragment_assign_target_to_beat
    }

    companion object {
        var seletedItems = HashSet<DealDistMechList>()
        var orderDetailsBeatTaskList: MutableList<orderDetailsBeatTask> =
            mutableListOf<orderDetailsBeatTask>()
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentAssignTargetToBeatBinding
        startDate = arguments?.getString("startDate").toString()
        endDate = arguments?.getString("endDate").toString()
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val outputformat = SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate = DateUtils.parseDate(startDate, inpFormat, outputformat)
        val endate = DateUtils.parseDate(endDate, inpFormat, outputformat)
        binding.tvBeatScheduleName.text = "Date (" + stdate + " to " + endate + ")"

        if (RBMLubricantsApplication.globalRole == "Team") {
            userUmId = DataConstant.teamUmid
        } else {
            userUmId = SharedPreferencesClass.retriveData(context as Context, "UM_ID").toString()

        }
        //showToastMessage(userUmId)
        hud = KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)


        seletedItems.clear()
        binding.btnSaveTask.setOnClickListener {

            if (seletedItems.size > 0) {
                saveTaskToBeat()
            } else {
                showToastMessage("Select Task")
            }

        }

        callApi()
    }

    @SuppressLint("LogNotTimber")
    fun saveTaskToBeat() {

        // showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val list: MutableList<AssignDetailsParams> = mutableListOf()



        for (i in adapter.getTaskData()) {
            if (i.assignmentSelected == true) {
                if (i.UM_Role == "R") {
                    val x = AssignDetailsParams(i.UM_ID!!, "0", "0", i.amount!!, i.collectAmount!!, i.task!!)
                    list.add(x)

                } else if (i.UM_Role == "D") {
                    val x = AssignDetailsParams("0", i.UM_ID!!, "0", i.amount!!, i.collectAmount!!, i.task!!)
                    list.add(x)

                } else if (i.UM_Role == "M") {
                    val x = AssignDetailsParams("0", "0", i.UM_ID!!, i.amount!!, i.collectAmount!!, i.task!!)
                    list.add(x)

                }
            }


        }
        Log.e("TASK_DATA_Details", list.toString())
        if (list.isEmpty()){
            showToastMessage("Select Task")

        }else{
            //showToastMessage(list.toString())
            val responseCall = apiInterface.assignTask(
                        //DealDistMechListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),AssignBeatToLocation.source,AssignBeatToLocation.areaList)
                        AssignTaskRequestParams(
                            SharedPreferenceUtils.getLoggedInUserId(context as Context),
                            DataConstant.beatScheduleId,
                            userUmId,
                            list
                        )
                        //  AssignTaskRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),GloblalDataRepository.getInstance().scheduleId,list)
                    )

                    responseCall.enqueue(assignTaskResponseResponse as Callback<SaveTaskResponse>)
        }


    }


    private val assignTaskResponseResponse = object : NetworkCallBack<SaveTaskResponse>() {
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<SaveTaskResponse>) {
            hide()
            //Toast.makeText(activity!!,"${response.data?.respMessage}",Toast.LENGTH_SHORT).show()
            if (response.data!!.status == 1) {
                Toast.makeText(activity!!, "${response.data?.respMessage}", Toast.LENGTH_SHORT)
                    .show()
                moveToBeatList()

            } else {
                showToastMessage(response.data.respMessage.toString())
            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }


    private fun setUpRecyclerView() {
        adapter = BeatTargetAssignmentAdapter(context, beatList)
        binding.rvTargetList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun moveToBeatList() {
        val navDirection =
            AssignTargetToBeatDirections.actionAssignTargetToBeatToDateWiseBeatListFragment("")
        Navigation.findNavController(binding.btnSaveTask).navigate(navDirection)
    }

    fun callApi() {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        /* showToastMessage("Source"+AssignBeatToLocation.source)
         showToastMessage("LocId"+AssignBeatToLocation.areaList)
         showToastMessage("taskLevel"+AssignBeatToLocation.taskLevel)*/
        val responseCall = apiInterface.getDealDistMechList(
            DealDistMechListRequestParams(
                SharedPreferenceUtils.getLoggedInUserId(context as Context),
                AssignBeatToLocation.source,
                AssignBeatToLocation.areaList,
                AssignBeatToLocation.taskLevel
            )
            //DealDistMechListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),"R","5")
        )
        responseCall.enqueue(orderVSQualityResponseResponse as Callback<DealDistMechListResponse>)

    }

    private var dataList: List<TaskForList> = emptyList<TaskForList>()

    private val orderVSQualityResponseResponse =
        object : NetworkCallBack<DealDistMechListResponse>() {
            override fun onSuccessNetwork(
                data: Any?,
                response: NetworkResponse<DealDistMechListResponse>
            ) {
                hide()
                if (response.data?.count != null && response.data?.count!! > 0) {
                    beatList = response.data.DealDistMechList
                    binding.rvTargetList.visibility=View.VISIBLE
                    binding.tvNoData.visibility=View.GONE
                    binding.btnSaveTask.visibility=View.VISIBLE

                    setUpRecyclerView()
                } else {
                    Toast.makeText(activity!!, "No Data Found", Toast.LENGTH_SHORT).show()
                    binding.tvNoData.visibility=View.VISIBLE
                    binding.rvTargetList.visibility=View.GONE
                    binding.btnSaveTask.visibility=View.GONE
                   // moveToBeatList()
                }


            }

            override fun onFailureNetwork(data: Any?, error: NetworkError) {
                hide()
            }

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


}
