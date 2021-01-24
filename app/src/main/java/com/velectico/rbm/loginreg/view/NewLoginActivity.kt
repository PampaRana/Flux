package com.velectico.rbm.loginreg.view

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.velectico.rbm.R
import kotlinx.android.synthetic.main.activity_login.*

class NewLoginActivity : AppCompatActivity() {
    var btnLogin: Button? = null
    var linkForgotPwd:Button? = null
    var inputEmail: TextInputEditText? = null
    var inputPassword:TextInputEditText? = null
    var progressBar: FrameLayout? = null
    var mobilePattern: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
       /* btnLogin = findViewById<Button>(R.id.btn_login)
        linkForgotPwd = findViewById<Button>(R.id.link_forgot_pwd)
        inputEmail = findViewById<TextInputEditText>(R.id.input_email)
        inputPassword = findViewById<TextInputEditText>(R.id.input_password)
        progressBar = findViewById<FrameLayout>(R.id.progressLayout)*/
        mobilePattern = "[0-9]{10}"
       /* btn_login.setOnClickListener {
            if (input_email.text.toString().trim { it <= ' ' }
                    .equals("", ignoreCase = true)
            ) {
                showToastMessage("Enter Mobile Number")
            } else if (!input_email.text.toString().trim { it <= ' ' }
                    .matches(mobilePattern)
            ) {
                showToastMessage("Enter Correct Mobile Number")
            } else if (input_password.text.toString().trim { it <= ' ' }
                    .equals("", ignoreCase = true)
            ) {
                showToastMessage("Enter Password")
            } else {
               *//* loginUser(
                    input_email.text.toString().trim { it <= ' ' },
                    input_password.text.toString().trim { it <= ' ' }
                )*//*
            }
        }*/
    }
    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}




