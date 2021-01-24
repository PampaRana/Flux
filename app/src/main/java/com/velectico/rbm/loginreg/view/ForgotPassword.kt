package com.velectico.rbm.loginreg.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.databinding.ActivityForgotPasswordBinding
import com.velectico.rbm.databinding.ActivityLoginBinding
import com.velectico.rbm.loginreg.model.forgotPasswordRequestParams
import com.velectico.rbm.loginreg.model.forgotPasswordResponse
import com.velectico.rbm.loginreg.viewmodel.LoginViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.reminder.model.CreateReminderRequestParams
import com.velectico.rbm.reminder.model.CreateReminderResponse
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

class ForgotPassword: BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun getLayout(): Int = R.layout.activity_forgot_password

    override fun init(savedInstanceState: Bundle?, binding: ViewDataBinding) {
        this.binding = binding as ActivityForgotPasswordBinding
        binding.btnSend.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }

    }


}