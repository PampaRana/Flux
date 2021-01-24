package com.velectico.rbm.loginreg.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.databinding.ActivityForgotPasswordBinding
import com.velectico.rbm.databinding.ActivityResetPasswordBinding
import com.velectico.rbm.loginreg.model.ResetPasswordRequestParams
import com.velectico.rbm.loginreg.model.ResetPasswordResponse
import com.velectico.rbm.loginreg.model.forgotPasswordRequestParams
import com.velectico.rbm.loginreg.model.forgotPasswordResponse
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import kotlinx.android.synthetic.main.activity_reset_password.*
import retrofit2.Callback

class ResetPassword : BaseActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    override fun getLayout(): Int = R.layout.activity_reset_password
    var mobile = ""
    override fun init(savedInstanceState: Bundle?, binding: ViewDataBinding) {
        this.binding = binding as ActivityResetPasswordBinding
        mobile = intent.getStringExtra("mobile")
        showToastMessage(mobile)
        binding.btnReset.setOnClickListener {
           resetPassword()
        }

    }

    fun resetPassword(){
        if (binding.inputPassword.text.toString()?.trim() == ""){
            showToastMessage("Please enter password")
        }
        else if (binding.inputConfirmpassword.text.toString()?.trim() == ""){
            showToastMessage("Please enter confirm password")
        }
        else if (binding.inputPassword.text.toString()?.trim() != binding.inputConfirmpassword.text.toString()?.trim()){
            showToastMessage("confirm password is not same")
        }
        else {
            val param = ResetPasswordRequestParams(
                mobile, input_password.text.toString()
                //SharedPreferenceUtils.getLoggedInUserId(this)

            )
            showHud()
            val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
            val responseCall = apiInterface.resetPassword(param)
            responseCall.enqueue(ResetPasswordResponse as Callback<ResetPasswordResponse>)
        }
    }

    private val ResetPasswordResponse = object : NetworkCallBack<ResetPasswordResponse>(){
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<ResetPasswordResponse>
        ) {
            response.data?.respMessage?.let { status ->
                Toast.makeText(applicationContext,response.data.respMessage, Toast.LENGTH_LONG).show()
                hide()
                gotoLogin()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
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

    fun gotoLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}