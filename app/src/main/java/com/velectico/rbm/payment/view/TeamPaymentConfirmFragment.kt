package com.velectico.rbm.payment.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentPaymentConfirmationBinding
import com.velectico.rbm.databinding.FragmentTeamPaymentConfirmBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.payment.adapter.PaymentConfirmationAdapter
import com.velectico.rbm.payment.models.PaymentCollectionRequestParams
import com.velectico.rbm.payment.models.PaymentCollectionResponse
import com.velectico.rbm.payment.models.PaymentConfirmDetails
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class TeamPaymentConfirmFragment : BaseFragment() {
    private lateinit var binding: FragmentTeamPaymentConfirmBinding;
    //var paymentConfirmationAdapter: PaymentConfirmationAdapter? = null
    private var paymentConfirmList : List<PaymentConfirmDetails> = emptyList()
    var userId=""

    override fun getLayout(): Int {
        return R.layout.fragment_team_payment_confirm
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentTeamPaymentConfirmBinding
        if (RBMLubricantsApplication.globalRole == "Team") {

            userId = GloblalDataRepository.getInstance().teamUserId
        } else {
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        callPaymentConfirmList(userId)

    }
    private fun callPaymentConfirmList(userId: String) {

        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getPaymentConfirmationList(
            PaymentCollectionRequestParams(userId)
        )
        responseCall.enqueue(paymentCollectionResponse as Callback<PaymentCollectionResponse>)


    }
    private val paymentCollectionResponse = object : NetworkCallBack<PaymentCollectionResponse>(){
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<PaymentCollectionResponse>) {
            hide()
            response.data?.status?.let { status ->
                paymentConfirmList .toMutableList().clear()

                if (response.data.count >0){
                    binding.card.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
                    paymentConfirmList = response.data.paymentConfirmationList.toMutableList()
                    binding.totalamt.visibility=View.VISIBLE
                    binding.mTableLayout.removeAllViews()
                    binding.mTableLayout.addView(View(context))
                    val tableRowHeader = layoutInflater.inflate(
                        R.layout.payment_confirm_table_header,
                        null
                    ) as TableRow
                    binding.mTableLayout.addView(tableRowHeader)
                    binding.card.setVisibility(View.VISIBLE)
                    binding.mTableLayout.setVisibility(View.VISIBLE)

                    for (i in paymentConfirmList){

                        val tableRow = layoutInflater.inflate(
                            R.layout.payment_confirm_table_item,
                            null
                        ) as TableRow
                        val tv_date_value = tableRow.findViewById(R.id.tv_date_value) as TextView
                        val tv_invoice_no_value =
                            tableRow.findViewById(R.id.tv_invoice_no_value) as TextView
                        val tv_amount_value =
                            tableRow.findViewById(R.id.tv_amount_value) as TextView

                        val tv_name_value =
                            tableRow.findViewById(R.id.tv_name_value) as TextView

                        val ic_status =
                            tableRow.findViewById(R.id.ic_status) as ImageView
                        tv_amount_value.text = i.collectedAmt
                        val inpFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        val outputformat = SimpleDateFormat("dd-MMM-yy", Locale.US);
                        val stdate = DateUtils.parseDate(i.collectedDate, inpFormat, outputformat)
                        tv_date_value.text = stdate
                        tv_invoice_no_value.text = i.SIH_Invoice_No
                        if (i.dealName!=null){
                            tv_name_value.text = i.dealName

                        }

                        if (i.distribName!=null){
                            tv_name_value.text = i.distribName


                        }

                        if (i.OH_Collected_Confirm_Status=="C"){

                            ic_status.setImageDrawable(resources.getDrawable(R.drawable.ic_check_mark))

                        }else{
                            ic_status.setImageDrawable(resources.getDrawable(R.drawable.ic_pending))

                        }

                        binding.mTableLayout.addView(tableRow);

                        var amount=0.0
                        amount+=i.collectedAmt!!.toDouble()

                        binding.totalamt.text="Total Amount : ₹ " +amount.toString()


                    }


                    //binding.btnFailedReport.visibility=View.VISIBLE

                    /*paymentConfirmationAdapter = PaymentConfirmationAdapter(object :
                        PaymentConfirmationAdapter.IBeatDateListActionCallBack {
                    }, context as Context);
                    binding.rvPaymentConfirmList.adapter = paymentConfirmationAdapter
                    paymentConfirmationAdapter!!.paymentConfirmList = paymentConfirmList*/

                }
                else{
                    binding.card.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE



                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
             hide()
        }

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

}