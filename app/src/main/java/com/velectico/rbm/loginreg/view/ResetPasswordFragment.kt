package com.velectico.rbm.loginreg.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.ActivityResetPasswordBinding
import com.velectico.rbm.databinding.DefaultFragmentBinding
import com.velectico.rbm.databinding.FragmentResetPasswordBinding
import com.velectico.rbm.loginreg.model.ResetPasswordRequestParams
import com.velectico.rbm.loginreg.model.ResetPasswordResponse
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_reset_password.*
import retrofit2.Callback


class ResetPasswordFragment : BaseFragment() {
    private lateinit var binding: FragmentResetPasswordBinding

    override fun getLayout(): Int {
        return R.layout.fragment_reset_password
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentResetPasswordBinding
        binding.btnReset.setOnClickListener {
            if (binding.inputPassword.text.toString().trim() == ""){
                showToastMessage("Please enter password")
            }
            else if (binding.inputConfirmpassword.text.toString().trim() == ""){
                showToastMessage("Please enter confirm password")
            }else if (binding.inputConfirmpassword.text.toString().trim().length < 4 || binding.inputConfirmpassword.text.toString().trim().length > 10){
                showToastMessage("Password must be within  4 to 10 alphanumeric characters")
            }
            else if (binding.inputPassword.text.toString().trim() != binding.inputConfirmpassword.text.toString().trim()){
                showToastMessage("confirm password is not same")
            }
            else {
                val param = ResetPasswordRequestParams(
                    SharedPreferenceUtils.getLoggedInUserId(context as Context), binding.inputPassword.text.toString().trim()

                )
                showHud()
                val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
                val responseCall = apiInterface.resetPassword(param)
                responseCall.enqueue(ResetPasswordResponse as Callback<ResetPasswordResponse>)
            }
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
    private val ResetPasswordResponse = object : NetworkCallBack<ResetPasswordResponse>(){
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<ResetPasswordResponse>
        ) {
            hide()
            if (response.data!!.status==1){
                showToastMessage(response.data.respMessage)
                activity!!.onBackPressed()
            }else{
                showToastMessage(response.data.respMessage)

            }
            /*response.data?.respMessage?.let { status ->
                Toast.makeText(context as Context,response.data.respMessage, Toast.LENGTH_LONG).show()

            }*/

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

}