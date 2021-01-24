package com.velectico.rbm.beats.views

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.view.GravityCompat
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentBeatListBinding
import com.velectico.rbm.databinding.RowBeatListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment to display Beat List
 */
class BeatListFragment : BaseFragment() {
    private lateinit var binding: FragmentBeatListBinding;
    private  var beatList : List<TaskDetails> = emptyList<TaskDetails>()
    private lateinit var adapter: BeatListAdapter
    var userId = ""
    override fun getLayout(): Int {
        return R.layout.fragment_beat_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatListBinding
        if (RBMLubricantsApplication.globalRole == "Team" ){
            userId = GloblalDataRepository.getInstance().teamUserId
        }
        else{
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        //showToastMessage(userId)
        val  getstring = arguments?.getString(  "scheduleId").toString()
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val outputformat = SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate = DateUtils.parseDate(getstring, inpFormat, outputformat)
        binding.tvBeatScheduleName.text = stdate

        /*showToastMessage("Date"+getstring)
        val inpFormat =  SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        val outputformat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        showToastMessage(DateUtils.parseDate(getstring,inpFormat,outputformat))*/
        callApi(getstring)
        /*binding.fab.setOnClickListener {
            moveToCreateBeat()
        }*/
    }


    private fun setUpRecyclerView(data:String?) {
        hide()
        adapter = BeatListAdapter(object : BeatListAdapter.IBeatListActionCallBack{
            override fun moveToBeatTaskDetails(position: Int, beatTaskId: String?,binding: RowBeatListBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  BeatListFragmentDirections.actionBeatListFragmentToBeatTaskDetailsViewFragment(userId,"75",beatList[position])
                Navigation.findNavController(binding.card).navigate(navDirection)
            }
        },data.toString());
        binding.rvBeatList.adapter = adapter
        adapter.beatList = beatList
    }

    fun callApi(getstring:String){
        showHud()
       /* val inpFormat =  SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        val outputformat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);*/
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getTaskDetailsByBeat(GetBeatDeatilsRequestParams(userId,getstring/*DateUtils.parseDate(getstring,inpFormat,outputformat)*/))
        responseCall.enqueue(readLeaveListResponse as Callback<BeatWiseTakListResponse>)
    }
    private val readLeaveListResponse = object : NetworkCallBack<BeatWiseTakListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<BeatWiseTakListResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test","readLeaveListResponse status="+response.data)
                hide()
                beatList.toMutableList().clear()
                if (response.data.status==0){
                    showToastMessage("No data found")
                    binding.tvNoData.visibility= View.VISIBLE
                    binding.rvBeatList.visibility=View.GONE
                }else{
                    beatList = response.data.details!!.toMutableList()
                    binding.rvBeatList.visibility=View.VISIBLE
                    setUpRecyclerView(response.data.visit)
                    binding.tvNoData.visibility= View.GONE

                }
            }
        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }


     var hud:KProgressHUD? = null
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


    /* private fun moveToCreateBeat(){
      val navDirection =  BeatListFragmentDirections.actionMoveToCreateBeat()
         Navigation.findNavController(binding.fab).navigate(navDirection)
     }*/


}
