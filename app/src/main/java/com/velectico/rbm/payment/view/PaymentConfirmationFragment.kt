package com.velectico.rbm.payment.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentPaymentConfirmationBinding
import com.velectico.rbm.databinding.FragmentPaymentListFragmentBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.payment.adapter.PaymentConfirmationAdapter
import com.velectico.rbm.payment.adapter.PaymentOutStandAdapter
import com.velectico.rbm.payment.models.*
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback

class PaymentConfirmationFragment : BaseFragment() {
    private lateinit var binding: FragmentPaymentConfirmationBinding;
    var paymentConfirmationAdapter: PaymentConfirmationAdapter? = null
    private var paymentConfirmList : List<PaymentConfirmDetails> = emptyList()
    var userId=""
    override fun getLayout(): Int {
       return R.layout.fragment_payment_confirmation
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentPaymentConfirmationBinding
        userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)

        callPaymentConfirmList()



    }

    private fun callPaymentConfirmList() {

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
                    binding.rvPaymentConfirmList.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE

                    paymentConfirmList = response.data.paymentConfirmationList.toMutableList()
                    paymentConfirmationAdapter = PaymentConfirmationAdapter(
                        context,
                        paymentConfirmList
                    )
                    binding.rvPaymentConfirmList.adapter=paymentConfirmationAdapter;


                }
                else{
                    binding.rvPaymentConfirmList.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE



                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
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