package com.velectico.rbm.reminder.view

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentReminderListBinding
import com.velectico.rbm.databinding.RowReminderListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.reminder.adapter.ReminderListAdapter
import com.velectico.rbm.reminder.model.ReminderListDetails
import com.velectico.rbm.reminder.model.ReminderListRequestParams
import com.velectico.rbm.reminder.model.ReminderListResponse
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback


class ReminderListFragment : BaseFragment() {
    private lateinit var binding: FragmentReminderListBinding;
    private var beatList : List<ReminderListDetails> = emptyList()
    private lateinit var adapter: ReminderListAdapter
    var type = ""
    override fun getLayout(): Int {
        return R.layout.fragment_reminder_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentReminderListBinding
       // beatList = ReminderList().getDummyBeatList()
        setUpRecyclerView()
        callApiList()
        binding.fab.setOnClickListener {
            moveToCreateReminder()
        }
        binding.button1.setOnClickListener{
            type = ""
            callApiList()
            //setUpRecyclerView()
        }
        binding.button2.setOnClickListener{
            type = "last"
            callApiList()
            //setUpRecyclerView()
        }
    }


    private fun setUpRecyclerView() {


       adapter = ReminderListAdapter(object : ReminderListAdapter.IBeatListActionCallBack{
            override fun moveToBeatTaskDetails(position: Int, beatTaskId: String?,binding: RowReminderListBinding) {

            }
        });

        binding.rvReminderList.adapter = adapter
        adapter.beatList = beatList
    }

    private fun moveToCreateReminder(){
       val navDirection =  ReminderListFragmentDirections.actionReminderListFragmentToCreateReminder()
        Navigation.findNavController(binding.fab).navigate(navDirection)

    }

    fun callApiList(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getReminderList(

            ReminderListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),type)
        )
        responseCall.enqueue(ReminderListResponse as Callback<ReminderListResponse>)
    }

    private val ReminderListResponse = object : NetworkCallBack<ReminderListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ReminderListResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test0000","OrderHistoryDetailsResponse status="+response.data)
                beatList.toMutableList().clear()
                if (response.data.count > 0){
                    beatList = response.data.ReminderListDetail!!.toMutableList()
                    binding.rvReminderList.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
                    setUpRecyclerView()


                }
                else{
                    showToastMessage("No data found")
                    binding.rvReminderList.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    //binding.resolvButton.visibility = View.GONE
                    //binding.pendButton.visibility = View.GONE
                    //binding.rvComplaintList.visibility = View.GONE
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
