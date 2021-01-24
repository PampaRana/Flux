package com.velectico.rbm.teamlist.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.BeatIdentificationParams
import com.velectico.rbm.beats.model.BeatIdentificationResponse
import com.velectico.rbm.databinding.FragmentTeamDashboardBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.SharedPreferencesClass
import retrofit2.Callback

/**
 * A simple [Fragment] subclass.
 */
class TeamDashboard : BaseFragment(){


    private lateinit var binding: FragmentTeamDashboardBinding
    var userId=""


    override fun getLayout(): Int = R.layout.fragment_team_dashboard

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentTeamDashboardBinding
        userId= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Login_Id").toString()
        getBeatResponse(userId)

        binding.beatButton.setOnClickListener {
            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment(binding.beatButton.text.toString())
            Navigation.findNavController(binding.performanceButton).navigate(navDirection)
        }

        binding.expenseIdButton.setOnClickListener {
            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment(binding.expenseIdButton.text.toString())
            Navigation.findNavController(binding.performanceButton).navigate(navDirection)
        }

        binding.leaveButton.setOnClickListener {
            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment(binding.leaveButton.text.toString())
            Navigation.findNavController(binding.performanceButton).navigate(navDirection)
        }

        binding.performanceButton.setOnClickListener{

            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment(binding.performanceButton.text.toString())
            Navigation.findNavController(binding.performanceButton).navigate(navDirection)
        }

        binding.dealerButton.setOnClickListener{

            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment(binding.dealerButton.text.toString())
            Navigation.findNavController(binding.dealerButton).navigate(navDirection)
        }


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
    private fun getBeatResponse(userId: String) {

        binding.progressLayout.visibility=View.VISIBLE
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
            binding.progressLayout.visibility=View.GONE
            //showToastMessage("Count"+response.data.count)
            if (response.data!!.status==1) {


                if (response.data.incompleteBeat>=1){
                    binding.tvNotiCount.visibility= View.VISIBLE
                    binding.tvNotiCount.text=response.data.incompleteBeat.toString()
                }else{
                    binding.tvNotiCount.visibility= View.GONE

                }


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }


    }
}
