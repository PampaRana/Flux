package com.velectico.rbm.base.views

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.loginreg.viewmodel.LoginViewModel
import com.velectico.rbm.utils.SharedPreferencesClass
import androidx.lifecycle.Observer


/**
 * Created by mymacbookpro on 2020-04-26
 * Base class of all the fragments within this project
 */

abstract class BaseFragment : Fragment(), IBaseFragment {

    //open lateinit var networkManager: INetworkManager
    lateinit var baseActivity: BaseActivity
    //private lateinit var loginViewModel: LoginViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is BaseActivity){
            baseActivity = context as BaseActivity

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, getLayout(), container, false)
        binding?.let {
            init(binding)
        }
        if (SharedPreferencesClass.retriveData(baseActivity,"SUM_ScreenShot_Lock").toString()=="Y"){
            baseActivity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        }
       // getLoginInfo()

        /*baseActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);*/
        return binding.root
    }
    /*private fun getLoginInfo() {
        loginViewModel = LoginViewModel.getInstance(baseActivity)

        loginViewModel.loginAPICall(SharedPreferencesClass.retriveData(activity as BaseActivity,"UM_Login_Id").toString(),
            SharedPreferencesClass.retriveData(baseActivity,"UM_Pass").toString())
        loginViewModel.userDataResponse.observe(baseActivity, Observer { listResponse ->
            listResponse?.let {
                onLoginSuccess(it)
            }
        })
    }
    private fun onLoginSuccess(response: LoginResponse?) {

        SharedPreferencesClass.insertData(context as Context,"SUM_Attendance_Lock",  response?.userDetails?.get(0)?.SUM_Attendance_Lock);
        SharedPreferencesClass.insertData(context as Context,"SUM_Location_Lock",  response?.userDetails?.get(0)?.SUM_Location_Lock);
        SharedPreferencesClass.insertData(context as Context,"SUM_ScreenShot_Lock",  response?.userDetails?.get(0)?.SUM_ScreenShot_Lock);

    }*/
    protected fun showToastMessage(message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    protected fun printLogMessage(tag:String, message:String){
        Log.d(tag, message)
    }

    fun addFragment(fragment: BaseFragment, viewId: Int){
        childFragmentManager.beginTransaction()
            .add(viewId, fragment, fragment::class.java.simpleName)
            .commit()
    }


    fun replaceFragment(fragment: BaseFragment, tag:String, viewId: Int){
        childFragmentManager.beginTransaction()
            .replace(viewId, fragment, fragment::class.java.simpleName)
            .commit()
    }

    fun removeFragment(fragment: BaseFragment, tag:String, viewId: Int){
        childFragmentManager.beginTransaction()
            .remove(fragment)
            .commitNow()
    }

    companion object{
        const val BASE_FRAGMENT = "BaseFragment"
    }
}