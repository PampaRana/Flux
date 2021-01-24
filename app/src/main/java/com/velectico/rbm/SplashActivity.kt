package com.velectico.rbm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.databinding.ActivitySplashBinding
import com.velectico.rbm.loginreg.view.LoginActivity
import com.velectico.rbm.navdrawer.views.DashboardActivity
import com.velectico.rbm.utils.SharedPreferencesClass


class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    var loggedIn=""

    //http://androidforbeginners.blogspot.com/2010/06/how-to-tile-background-image-in-android.html
    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun init(savedInstanceState: Bundle?, binding: ViewDataBinding) {
        this.binding = binding as ActivitySplashBinding
        loggedIn= SharedPreferencesClass.retriveData(applicationContext,"isLoggedIn").toString()

       // showToastMessage("ATTE"+SharedPreferencesClass.retriveData(applicationContext,"attendance_in").toString())

        Handler().postDelayed({
            if (loggedIn=="1"){
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            /*startActivity(Intent(this, LoginActivity::class.java))
            finish()*/
        }, SPLASH_TIME_OUT)
    }

    companion object{
        const val SPLASH_TIME_OUT:Long = 3000 // 3 sec
    }
}
