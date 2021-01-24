package com.velectico.rbm.beats.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatTaskDetailsViewAdapter
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.beats.model.BeatTaskDetailsListResponse
import com.velectico.rbm.beats.model.BeatTaskDetailsRequestParams
import com.velectico.rbm.beats.model.TaskDetails
import com.velectico.rbm.databinding.FragmentBeatTaskDetailsViewBinding
import com.velectico.rbm.databinding.RowBeatTaskDetailsViewBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DataConstant
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SALES_LEAD_ROLE
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class BeatTaskDetailsViewFragment : BaseFragment() {

    private lateinit var binding: FragmentBeatTaskDetailsViewBinding;
    private var beatList: List<BeatTaskDetails> = emptyList()
    private lateinit var adapter: BeatTaskDetailsViewAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_beat_task_details_view
    }

    var scheduleId = TaskDetails()
    var userId = ""
    var userIdB = ""
    var scheduleDate = ""
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatTaskDetailsViewBinding
        if (RBMLubricantsApplication.globalRole == "Team") {
            userIdB = GloblalDataRepository.getInstance().teamUserId
        } else {
            userIdB = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        scheduleId = arguments!!.get("taskDetails") as TaskDetails
        userId = arguments?.getString("userId").toString()
        val y = arguments!!.get("taskDetails")
        val x = y as TaskDetails
        binding.task = x

        //showToastMessage("userId"+userIdB)
        //showToastMessage("ScheduleId"+ scheduleId.schedule_id.toString())
        scheduleId = arguments!!.get("taskDetails") as TaskDetails

        //scheduleDate=arguments?.getString(  "scheduleId").toString() //changes pampa
        //showToastMessage(scheduleId.toString())
        /*val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MM-yyyy", Locale.US);
        val stdate =  DateUtils.parseDate(x.schedule_startDate,inpFormat,outputformat)
        val endate =  DateUtils.parseDate(x.schedule_endDate,inpFormat,outputformat)
        binding.dattxt.text = stdate + " To " + endate*/
        //setUpRecyclerView()
        // callApi2(scheduleDate)
        callApi2()
        /*binding.fab.setOnClickListener {
            moveToCreateBeat()
        }*/
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

    fun callApi2(/*scheduleDate:String*/) {
        showHud()
        val inpFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        val outputformat = SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getScheduleTaskDetailsByBeat(
            BeatTaskDetailsRequestParams(
                userIdB,
                scheduleId.schedule_id.toString()/*DateUtils.parseDate(scheduleDate,inpFormat,outputformat)*/
            )
        )

        responseCall.enqueue(beatTaskDetailsListResponse as Callback<BeatTaskDetailsListResponse>)
    }


    private val beatTaskDetailsListResponse =
        object : NetworkCallBack<BeatTaskDetailsListResponse>() {
            override fun onSuccessNetwork(
                data: Any?,
                response: NetworkResponse<BeatTaskDetailsListResponse>
            ) {
                hide()
                response.data?.status?.let { status ->
                    Log.e("test222", "BeatTaskDetailsListResponse status=" + response.data)
                    beatList.toMutableList().clear()
                    if (response.data.count!! > 0) {
                        binding.beatSummary.visibility = View.VISIBLE
                        // binding.tvVisit.text=response.data.count.toString()
                        // binding.tvOrdrAmt.text=response.data.order_amount.toString()
                        //binding.collectionAmt.text=response.data.collection_amount.toString()
                        //binding.beatName.text=response.data.BM_Beat_Name.toString()

                        if (RBMLubricantsApplication.globalRole == "Team") {
                            binding.llPerson.visibility = View.VISIBLE
                            binding.tvPersonName.text = response.data.UserName.toString()

                        } else {
                            binding.llPerson.visibility = View.GONE
                        }
                        if (response.data.BM_Resp_Level == "R") {
                            binding.tvBeatLevel.text = "Region"
                        } else if (response.data.BM_Resp_Level == "D") {
                            binding.tvBeatLevel.text = "District"
                        } else if (response.data.BM_Resp_Level == "A") {
                            binding.tvBeatLevel.text = "Area"
                        } else {
                            binding.tvBeatLevel.text = "Zone"

                        }

                        //binding.tvVisit.text=scheduleId.visit.toString()
                        @SuppressLint("SimpleDateFormat") val input =
                            SimpleDateFormat("yyyy-MM-dd")
                        @SuppressLint("SimpleDateFormat") val output =
                            SimpleDateFormat("dd-MM-yyyy")
                        val scheduleDate =
                            input.parse(response.data.schedule_startDate.toString()) // parse input
                        binding.dattxt.text = output.format(scheduleDate);//

                        beatList = response.data.Task_Details!!.toMutableList()
                        binding.rvBeatList.visibility = View.VISIBLE
                        binding.tvNoData.visibility = View.GONE
                        setUpRecyclerView()
                        //showToastMessage("Data found")

                    } else {
                        showToastMessage("No data found")
                        binding.tvNoData.visibility = View.VISIBLE
                        binding.rvBeatList.visibility = View.GONE
                        binding.beatSummary.visibility = View.GONE
                    }

                }

            }

            override fun onFailureNetwork(data: Any?, error: NetworkError) {
                hide()
            }

        }


    private fun setUpRecyclerView() {

        adapter = BeatTaskDetailsViewAdapter(object :
            BeatTaskDetailsViewAdapter.IBeatTaskDetailsViewActionCallBack {
            override fun moveToBeatTaskDetails(
                position: Int,
                beatTaskId: String?,
                binding: RowBeatTaskDetailsViewBinding
            ) {

                val navDirection =
                    BeatTaskDetailsViewFragmentDirections.actionBeatTaskDetailsViewFragmentToBeatTaskDealerDetailsFragment(
                        beatList[position]
                    )
                Navigation.findNavController(binding.cardDetailsForTask).navigate(navDirection)
            }
        });
        binding.rvBeatList.adapter = adapter
        adapter.beatList = beatList
    }

    private fun moveToCreateBeat() {
        //val navDirection =  BeatListFragmentDirections.actionMoveToCreateBeat()
        //Navigation.findNavController(binding.fab).navigate(navDirection)
    }


}
