package com.velectico.rbm.beats.views

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils.isToday
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentDateWiseBeatListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.loginreg.viewmodel.LoginViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DateWiseBeatListFragment : BaseFragment()  {

    private lateinit var binding: FragmentDateWiseBeatListBinding
   // private  var beatDateList : MutableList<ScheduleDates> = mutableListOf()
    private var beatDateList : List<BeatDateDetails> = emptyList()

    private lateinit var adapter: BeatDateListAdapter
    var role=""
    var userId=""
    private lateinit var loginViewModel: LoginViewModel

    override fun getLayout(): Int {
        return R.layout.fragment_date_wise_beat_list
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentDateWiseBeatListBinding

        role= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Role").toString()
        //userId= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Login_Id").toString()

        if(role == SALES_LEAD_ROLE){
            binding.fab.show()
            binding.fab.setOnClickListener {
                moveToCreateBeat()
            }
        }else if(role == DISTRIBUTER_ROLE) {
            binding.fab.show()
            binding.fab.setOnClickListener {
                moveToCreateBeat()
            }

        } else{
            binding.fab.hide()

        }
        if (RBMLubricantsApplication.globalRole == "Team" ){
            userId = GloblalDataRepository.getInstance().teamUserId

        }
        else{
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }

        getBeatResponse(userId)
        getLoginInfo()

        /* if (RBMLubricantsApplication.globalRole == "Team" ){
             binding.fab.hide()
         }*/

    }
    private fun getLoginInfo() {
           loginViewModel = LoginViewModel.getInstance(activity as BaseActivity)

           loginViewModel.loginAPICall(SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Login_Id").toString(),
               SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Pass").toString())
           loginViewModel.userDataResponse.observe(this, Observer { listResponse ->
               listResponse?.let {
                   onLoginSuccess(it)
               }
           })
       }
       private fun onLoginSuccess(response: LoginResponse?) {

           SharedPreferencesClass.insertData(context as Context,"SUM_Attendance_Lock",  response?.userDetails?.get(0)?.SUM_Attendance_Lock);
           SharedPreferencesClass.insertData(context as Context,"SUM_Location_Lock",  response?.userDetails?.get(0)?.SUM_Location_Lock);
           SharedPreferencesClass.insertData(context as Context,"SUM_ScreenShot_Lock",  response?.userDetails?.get(0)?.SUM_ScreenShot_Lock);

       }
    /*override fun onResume() {
        super.onResume()
        showHud()
        beatDateList.clear()
        DateUtils.getLast3Days()
        val z = DateUtils.getLast3Days()
        val x =    DateUtils.getNextFifteenDays()
        beatDateList.addAll(z)
        beatDateList.addAll(x)
        setUpRecyclerView(beatDateList)


    }*/
    private fun getBeatResponse(userId: String) {

        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBeatIdentificationList(
            BeatIdentificationParams(userId)
        )
        responseCall.enqueue(beatIdentificationResponse as Callback<BeatIdentificationResponse>)

    }
    private val beatIdentificationResponse = object : NetworkCallBack<BeatIdentificationResponse>() {
        @SuppressLint("RestrictedApi")
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<BeatIdentificationResponse>
        ) {
            hide()
            beatDateList.toMutableList().clear()
            if (response.data!!.status==1) {
                beatDateList = response.data.Beat_Dates.toMutableList()
                //Collections.reverse(orderHeadList);
                binding.rvBeatListDateWise.visibility = View.VISIBLE
                setUpRecyclerView(beatDateList)


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

   /* private fun getLeaveListFromServer() {
        showHud()
      //  binding?.progressLayout?.visibility = View.VISIBLE
        menuViewModel.getBeatListAPICall(SharedPreferenceUtils.getLoggedInUserId(context as Context))
    }*/

    private fun setUpRecyclerView(data:List<BeatDateDetails>) {
        adapter = BeatDateListAdapter(object : BeatDateListAdapter.IBeatDateListActionCallBack{
            override fun moveToBeatDetails(position: Int, beatTaskId: String?,binding: RowBeatListDatesBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  DateWiseBeatListFragmentDirections.actionDateWiseBeatListFragmentToBeatListFragment(beatTaskId!!)
                Navigation.findNavController(binding.beatDateRow).navigate(navDirection)

            }
        },data);
        binding.rvBeatListDateWise.adapter = adapter
                adapter.beatList = data

        adapter.notifyDataSetChanged()
        hide()
    }

    private fun moveToCreateBeat(){
        val navDirection =  DateWiseBeatListFragmentDirections.actionDateWiseBeatListFragmentToCreateBeatFragment()
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }

}
