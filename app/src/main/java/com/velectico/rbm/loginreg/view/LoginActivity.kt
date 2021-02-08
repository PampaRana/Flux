package com.velectico.rbm.loginreg.view;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
/*import com.google.android.gms.tasks.OnCompleteListener*/
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.databinding.ActivityLoginBinding
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.loginreg.model.forgotPasswordRequestParams
import com.velectico.rbm.loginreg.model.forgotPasswordResponse
import com.velectico.rbm.loginreg.viewmodel.LoginViewModel
import com.velectico.rbm.navdrawer.views.DashboardActivity
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.InternetCheck
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.SharedPreferencesClass
import retrofit2.Callback


//https://github.com/kotlindroider/Kotlin-Login-Sample
//http://ticons.fokkezb.nl/

class LoginActivity : BaseActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    var mobilePattern = ""

    override fun getLayout(): Int = R.layout.activity_login

    override fun init(savedInstanceState: Bundle?, binding: ViewDataBinding) {
        this.binding = binding as ActivityLoginBinding
        setUp()
    }

    private fun setUp() {
        val animationSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        mobilePattern = "[0-9]{10}";
        /*FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Failed", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            //val token: String = task.getResult().getToken()
            Log.d("Token", "onCreate: $token")

            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })*/

        binding.btnLogin.setOnClickListener {
           // doLogin()
            val mobile = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            if (mobile == "") {
                showToastMessage("Enter Mobile Number")

            } else if (!mobile
                    .matches(mobilePattern.toRegex())
            ) {
                showToastMessage("Invalid Mobile Number")

            } else if (password == "") {
                showToastMessage("Enter Password")

            }else if (password.length < 4 || password.length > 10){
                showToastMessage("Password must be within  4 to 10 alphanumeric characters")
            } else {
                loginViewModel.loginAPICall(mobile, password)

            }
        }



        if (InternetCheck.isConnected(applicationContext)){
            observeViewModelData()

        }else{
            showToastMessage("Please check your internet connection")
        }
//        binding.linkForgotPwd.setOnClickListener {
//            val intent = Intent(this, DashboardActivity::class.java)
//            startActivity(intent)
//        }

        binding.linkForgotPwd.setOnClickListener {
            forgotPassword()
        }
    }

    private fun observeViewModelData() {
        loginViewModel = LoginViewModel.getInstance(this)
        loginViewModel.userDataResponse.observe(this, Observer { listResponse ->
            listResponse?.let {
                showToastMessage(getString(R.string.login_success))
                onLoginSuccess(it)
            }
        })

        loginViewModel.loading.observe(this, Observer { progress ->
            binding.progressLayout.visibility = if(progress) View.VISIBLE else View.GONE
        })

        loginViewModel.errorLiveData.observe(this, Observer {
            onLoginFailed()
            (this as BaseActivity).showAlertDialog(it.errorMessage ?: getString(R.string.no_data_available))
        })
    }

    private fun doLogin() {
        //binding.btnLogin.isEnabled = false
       /*
        if (!validate()) {
            onLoginFailed()
            return
        }*/

    }

    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

    private fun onLoginSuccess(response:LoginResponse?) {

        binding.btnLogin.isEnabled = true
        //binding.inputEmail.setText("7001507620");
       // binding.inputPassword.setText("123456")

        SharedPreferencesClass.insertData(applicationContext,"UM_ID", response?.userDetails?.get(0)?.uMID);
        SharedPreferencesClass.insertData(applicationContext,"UM_Name", response?.userDetails?.get(0)?.uMName);
        SharedPreferencesClass.insertData(applicationContext,"UM_Phone",  response?.userDetails?.get(0)?.uMPhone);
        SharedPreferencesClass.insertData(applicationContext,"UM_Email",  response?.userDetails?.get(0)?.uMEmail);
        SharedPreferencesClass.insertData(applicationContext,"UM_Login_Id",  response?.userDetails?.get(0)?.uMLoginId);
        SharedPreferencesClass.insertData(applicationContext,"UM_Role",  response?.userDetails?.get(0)?.uMRole);
        SharedPreferencesClass.insertData(applicationContext,"UM_Active_Inactive",  response?.userDetails?.get(0)?.uMActiveInactive);
        SharedPreferencesClass.insertData(applicationContext,"SUM_Address",  response?.userDetails?.get(0)?.mSMAddress);
        SharedPreferencesClass.insertData(applicationContext,"SUM_DOB",  response?.userDetails?.get(0)?.mSMDob);
        SharedPreferencesClass.insertData(applicationContext,"SUM_Blood_Grp",  response?.userDetails?.get(0)?.mSMBldGrp);
        SharedPreferencesClass.insertData(applicationContext,"SUM_Attendance_Lock",  response?.userDetails?.get(0)?.SUM_Attendance_Lock);
        SharedPreferencesClass.insertData(applicationContext,"SUM_Location_Lock",  response?.userDetails?.get(0)?.SUM_Location_Lock);
        SharedPreferencesClass.insertData(applicationContext,"SUM_ScreenShot_Lock",  response?.userDetails?.get(0)?.SUM_ScreenShot_Lock);

        SharedPreferencesClass.insertData(applicationContext,"UM_Pass",  binding.inputPassword.text.toString());
        SharedPreferencesClass.insertData(applicationContext,"noOfDaysPresent",  response?.userDashboardDetails!!.noOfDaysPresent);
        SharedPreferencesClass.insertData(applicationContext,"noOfDaysAbsent",  response?.userDashboardDetails!!.noOfDaysAbsent);
        SharedPreferencesClass.insertData(applicationContext,"targetShortfall",  response?.userDashboardDetails!!.targetShortfall);
        SharedPreferencesClass.insertData(applicationContext,"paymentShortfall",  response?.userDashboardDetails!!.paymentShortfall);
        SharedPreferencesClass.insertData(applicationContext,"incentiveAchived",  response?.userDashboardDetails!!.incentiveAchived);

        SharedPreferencesClass.insertData(applicationContext,"isLoggedIn", "1");

        response.userDetails?.get(0)?.uMLoginId?.let { SharedPreferenceUtils.addSessionDataToSharedPreference(it,applicationContext) };

        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun onLoginFailed() {
        binding.btnLogin.isEnabled = true
    }

    fun validate(): Boolean {
        var errMsg : String = "";
        var valid = true

        val email = binding.inputEmail!!.text.toString()
        val password = binding.inputPassword!!.text.toString()

        if (email.isEmpty() || !android.util.Patterns.PHONE.matcher(email).matches()) {
            errMsg = "Enter a valid phone no\n"
            valid = false
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            errMsg += "Enter a password between 4 and 10 alphanumeric characters";
            valid = false
        }
        if(!valid){
            Toast.makeText(applicationContext,errMsg,Toast.LENGTH_LONG).show()
        }
        return valid
    }

    fun forgotPassword(){
        if (binding.inputEmail.text.toString().trim() == "") {
            showToastMessage("Enter Mobile Number")

        } else if (!binding.inputEmail.text.toString().trim()
                .matches(mobilePattern.toRegex())
        ) {
            showToastMessage("Invalid Mobile Number")

        }
        /*if (binding.inputPassword.text.toString()?.trim() == ""){
            showToastMessage("Please enter mobile number")
        }*/
        else {
            val param = forgotPasswordRequestParams(
                binding.inputEmail.text.toString().trim()
                //SharedPreferenceUtils.getLoggedInUserId(this)

            )
            showHud()
            if (InternetCheck.isConnected(applicationContext)) {
                val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
                val responseCall = apiInterface.forgotPassword(param)
                responseCall.enqueue(forgotPasswordResponse as Callback<forgotPasswordResponse>)
            }else{
                showToastMessage("Please check your internet connection")

            }
        }
    }

    private val forgotPasswordResponse = object : NetworkCallBack<forgotPasswordResponse>(){
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<forgotPasswordResponse>
        ) {
            hide()

            if (response.data!!.status==1){
                showToastMessage(response.data.respMessage)
            }else{
                showToastMessage(response.data.respMessage)

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

    fun gotoReset(){
        val intent = Intent(this, ResetPassword::class.java)
        intent.putExtra("mobile",binding.inputEmail.text.toString())
        startActivity(intent)
    }
}
