package com.velectico.rbm.redeem.view

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
import com.velectico.rbm.databinding.DefaultFragmentBinding
import com.velectico.rbm.databinding.FragmentManualScanBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.redeem.model.ManualQrRequestParams
import com.velectico.rbm.redeem.model.SendQrRequestParams
import com.velectico.rbm.redeem.model.SendQrResponse
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback


class ManualScanFragment :  BaseFragment() {
    private lateinit var binding: FragmentManualScanBinding

    override fun getLayout(): Int {
        return R.layout.fragment_manual_scan
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentManualScanBinding
        binding.btnScan.setOnClickListener {
            if (binding.inputQrCode.text.toString().trim()==""){
                showToastMessage("Enter QR Code")
            }else{
                initHud()
                showHud()
                scanQrApi(SharedPreferenceUtils.getLoggedInUserId(context as Context),binding.inputQrCode.text.toString().trim())
            }
        }

    }

    private fun scanQrApi(userId: String, qrcode: String) {
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.sendManualQrDetails(
            ManualQrRequestParams(userId,qrcode)
        )
        responseCall.enqueue(manualQrResponse as Callback<SendQrResponse>)



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
    fun initHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }
    private val manualQrResponse = object : NetworkCallBack<SendQrResponse>(){
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<SendQrResponse>
        ) {
            hide()
            if(response.data!!.status==1){
                showToastMessage(response.data.respMessage!!)
                activity!!.onBackPressed()

            }else{
                showToastMessage(response.data.respMessage!!)

            }


        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

}