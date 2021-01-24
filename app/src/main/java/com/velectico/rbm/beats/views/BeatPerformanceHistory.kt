package com.velectico.rbm.beats.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentBeatPerformanceBinding
import com.velectico.rbm.databinding.FragmentBeatPerformanceHistoryBinding
import com.velectico.rbm.databinding.FragmentTeamPerformanceBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.teamlist.model.TeamListDealDistPerformanceResponse
import com.velectico.rbm.teamlist.model.TeamListPerformanceRequestParams
import com.velectico.rbm.teamlist.view.TeamPerformanceFragmentDirections
import retrofit2.Callback
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BeatPerformanceHistory : BaseFragment() {
    private lateinit var binding: FragmentBeatPerformanceBinding

    var dateFormat: DateFormat? = null
    var date: Date? = null
    var today_date: String? = null

    override fun getLayout(): Int {
        return R.layout.fragment_beat_performance
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatPerformanceBinding
        dateFormat = SimpleDateFormat("dd-MM-yyyy")
        date = Date()
        today_date = dateFormat!!.format(date)
        binding.tvTodayDate.text = "Till Today(" + today_date + ")"

        callDealDistApiList()

    }
    fun callDealDistApiList() {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealerDistPerformance(

            TeamListPerformanceRequestParams( arguments?.getString("userId").toString())
        )
        responseCall.enqueue(TeamListDealDistPerformanceResponse as Callback<TeamListDealDistPerformanceResponse>)
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

    private val TeamListDealDistPerformanceResponse =
        object : NetworkCallBack<TeamListDealDistPerformanceResponse>() {
            @SuppressLint("LogNotTimber")
            override fun onSuccessNetwork(
                data: Any?,
                response: NetworkResponse<TeamListDealDistPerformanceResponse>
            ) {
                hide()
                binding.rlPerform.visibility=View.VISIBLE
                binding.speedViewDealDist.visibility=View.VISIBLE
                binding.llOne.visibility=View.VISIBLE
                // Toast.makeText(context, response.data?.Performance_outOf_5, Toast.LENGTH_LONG).show()
                binding.speedViewDealDist.speedTo(response.data?.Performance_outOf_4!!.toFloat())
                Log.e("Performance", "onSuccessNetwork: "+response.data)
                binding.tvPerformMark.text=response.data.Performance_outOf_4

               /* if (RBMLubricantsApplication.globalRole == "Team") {
                    binding.tvPerformMark.text=response.data.Performance_outOf_4
                    binding.tvPerformMark.visibility = View.VISIBLE
                }else{
                    binding.tvPerformMark.visibility = View.GONE

                }*/

                binding.btnOrder.setOnClickListener{
                    val navDirection =  BeatPerformanceHistoryDirections.actionBeatPerformanceHistoryToOrderPerformFragment(
                        response.data.Sales_Order.Monthly.order_target.toString(),
                        response.data.Sales_Order.Monthly.order_target_achieve.toString(),
                        response.data.Sales_Order.Quaterly.order_target.toString(),
                        response.data.Sales_Order.Quaterly.order_target_achieve.toString(),
                        response.data.Sales_Order.Hly.order_target.toString(),
                        response.data.Sales_Order.Hly.order_target_achieve.toString(),
                        response.data.Sales_Order.Annually.order_target.toString(),
                        response.data.Sales_Order.Annually.order_target_achieve.toString()

                    )
                    Navigation.findNavController(binding.btnOrder).navigate(navDirection)


                }

                binding.btnPayment.setOnClickListener{
                    val navDirection =  BeatPerformanceHistoryDirections.actionBeatPerformanceHistoryToPaymentPerformFragment(
                        response.data.CollectionPerformance.Monthly.collection_target.toString(),
                        response.data.CollectionPerformance.Monthly.collection_target_achieve.toString(),
                        response.data.CollectionPerformance.Quaterly.collection_target.toString(),
                        response.data.CollectionPerformance.Quaterly.collection_target_achieve.toString(),
                        response.data.CollectionPerformance.Hly.collection_target.toString(),
                        response.data.CollectionPerformance.Hly.collection_target_achieve.toString(),
                        response.data.CollectionPerformance.Annually.collection_target.toString(),
                        response.data.CollectionPerformance.Annually.collection_target_achieve.toString()

                    )
                    Navigation.findNavController(binding.btnPayment).navigate(navDirection)

                }


            }

            override fun onFailureNetwork(data: Any?, error: NetworkError) {
                hide()
            }

        }
}
