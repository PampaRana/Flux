package com.velectico.rbm.teamlist.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.github.anastr.speedviewlib.components.Section
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentTeamPerformanceBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.products.view.ProductListFragmentDirections
import com.velectico.rbm.teamlist.model.*
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class TeamPerformanceFragment : BaseFragment() {

    private lateinit var binding: FragmentTeamPerformanceBinding
    var dateFormat: DateFormat? = null
    var date: Date? = null
    var today_date: String? = null
    var role=""
    override fun getLayout(): Int {
        return R.layout.fragment_team_performance
    }

    @SuppressLint("SimpleDateFormat")
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentTeamPerformanceBinding

        dateFormat = SimpleDateFormat("dd-MM-yyyy")
        date = Date()
        today_date = dateFormat!!.format(date)
        binding.tvTodayDate.text = "Till Today(" + today_date + ")"
        // Toast.makeText(context, GloblalDataRepository.getInstance().teamUserId,Toast.LENGTH_LONG).show()
        role= SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Role").toString()

        if (role== DEALER_ROLE){
            callDealDistApiList()

        }else if (role== DISTRIBUTER_ROLE){
            callDealDistApiList()

        }else if (role== DISTRIBUTER_ROLE){
            callDealDistApiList()

        }else {
            callApiList()
        }
        val getstring = arguments?.getString("userId").toString()


        //showToastMessage(arguments?.getString("userId").toString())
    }

    fun callApiList() {
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getTeamPerformanceList(

            TeamListPerformanceRequestParams( arguments?.getString("userId").toString())
        )
        responseCall.enqueue(TeamListPerformanceResponse as Callback<TeamListPerformanceResponse>)
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

    fun moveToDetails() {
        // val navDirection =  TeamPerformanceFragmentDirections.actionTeamPerformanceFragmentToTeamPerformanceDetailsFragment()
        // Navigation.findNavController(binding.lblMonthly).navigate(navDirection)
    }

    private val TeamListPerformanceResponse =
        object : NetworkCallBack<TeamListPerformanceResponse>() {
            @SuppressLint("LogNotTimber")
            override fun onSuccessNetwork(
                data: Any?,
                response: NetworkResponse<TeamListPerformanceResponse>
            ) {
                hide()
                binding.rlPerform.visibility=View.VISIBLE
                binding.speedViewDealDist.visibility=View.GONE
                binding.speedView.visibility=View.VISIBLE
                binding.llOne.visibility=View.GONE
                binding.llTwo.visibility=View.VISIBLE
                binding.llThree.visibility=View.VISIBLE
               // Toast.makeText(context, response.data?.Performance_outOf_5, Toast.LENGTH_LONG).show()
                binding.speedView.speedTo(response.data?.Performance_outOf_5!!.toFloat())
                Log.e("Performance", "onSuccessNetwork: "+response.data)

                if (RBMLubricantsApplication.globalRole == "Team") {
                    binding.tvPerformMark.text=response.data.Performance_outOf_5
                    binding.tvPerformMark.visibility = View.VISIBLE
                }else{
                    binding.tvPerformMark.visibility = View.GONE

                }

                binding.btnSales.setOnClickListener{
                    val navDirection =  TeamPerformanceFragmentDirections.actionTeamPerformanceFragmentToSalesOrderFragment(
                        response.data.Sales_Order.Monthly.order_target.toString(),
                        response.data.Sales_Order.Monthly.order_target_achieve.toString(),
                        response.data.Sales_Order.Quaterly.order_target.toString(),
                        response.data.Sales_Order.Quaterly.order_target_achieve.toString(),
                        response.data.Sales_Order.Hly.order_target.toString(),
                        response.data.Sales_Order.Hly.order_target_achieve.toString(),
                        response.data.Sales_Order.Annually.order_target.toString(),
                        response.data.Sales_Order.Annually.order_target_achieve.toString()

                    );
                    Navigation.findNavController(binding.btnSales).navigate(navDirection)


                }

                binding.btnCollections.setOnClickListener{
                    val navDirection =  TeamPerformanceFragmentDirections.actionTeamPerformanceFragmentToCollectionFragment(
                        response.data.CollectionPerformance.Monthly.collection_target.toString(),
                        response.data.CollectionPerformance.Monthly.collection_target_achieve.toString(),
                        response.data.CollectionPerformance.Quaterly.collection_target.toString(),
                        response.data.CollectionPerformance.Quaterly.collection_target_achieve.toString(),
                        response.data.CollectionPerformance.Hly.collection_target.toString(),
                        response.data.CollectionPerformance.Hly.collection_target_achieve.toString(),
                        response.data.CollectionPerformance.Annually.collection_target.toString(),
                        response.data.CollectionPerformance.Annually.collection_target_achieve.toString()

                    )
                    Navigation.findNavController(binding.btnSales).navigate(navDirection)


                }
                binding.btnLeaves.setOnClickListener{
                    val navDirection =  TeamPerformanceFragmentDirections.actionTeamPerformanceFragmentToLeavePerformFragment(
                        response.data.leaveTaken.toString()


                    )
                    Navigation.findNavController(binding.btnSales).navigate(navDirection)


                }
                binding.btnExpenses.setOnClickListener{
                    val navDirection =  TeamPerformanceFragmentDirections.actionTeamPerformanceFragmentToExpensePerformFragment(
                        response.data.AnnualExpense.toString(),
                        response.data.ExpensePerTask.toString()

                    )
                    Navigation.findNavController(binding.btnSales).navigate(navDirection)

                }

            }

            override fun onFailureNetwork(data: Any?, error: NetworkError) {
                 hide()
            }

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
                binding.btnExpenses.visibility=View.GONE
                binding.btnLeaves.visibility=View.GONE
                binding.speedViewDealDist.visibility=View.VISIBLE
                binding.speedView.visibility=View.GONE
                binding.llOne.visibility=View.VISIBLE
                binding.llTwo.visibility=View.GONE
                binding.llThree.visibility=View.GONE


                // Toast.makeText(context, response.data?.Performance_outOf_5, Toast.LENGTH_LONG).show()
                binding.speedViewDealDist.speedTo(response.data?.Performance_outOf_4!!.toFloat())
                Log.e("Performance", "onSuccessNetwork: "+response.data)

                if (RBMLubricantsApplication.globalRole == "Team") {
                    binding.tvPerformMark.text=response.data.Performance_outOf_4
                    binding.tvPerformMark.visibility = View.VISIBLE
                }else{
                    binding.tvPerformMark.visibility = View.GONE

                }

                binding.btnOrder.setOnClickListener{
                    val navDirection =  TeamPerformanceFragmentDirections.actionTeamPerformanceFragmentToOrderPerformFragment(
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
                    val navDirection =  TeamPerformanceFragmentDirections.actionTeamPerformanceFragmentToPaymentPerformFragment(
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
