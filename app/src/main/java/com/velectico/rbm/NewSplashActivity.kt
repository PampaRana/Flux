package com.velectico.rbm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.velectico.rbm.loginreg.view.NewLoginActivity

class NewSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this, NewLoginActivity::class.java))
            finish()
        }, NewSplashActivity.SPLASH_TIME_OUT)
    }
    companion object{
        const val SPLASH_TIME_OUT:Long = 3000 // 3 sec
    }
}