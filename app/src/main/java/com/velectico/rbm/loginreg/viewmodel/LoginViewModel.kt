package com.velectico.rbm.loginreg.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.viewmodel.BaseViewModel
import com.velectico.rbm.base.model.UIError
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.loginreg.model.LoginRequest
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.network.apiconstants.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.INetworkManager
import com.velectico.rbm.network.manager.ManagerFactory
import com.velectico.rbm.network.manager.getNetworkManager
import com.velectico.rbm.network.request.NetworkRequest
import com.velectico.rbm.network.response.NetworkResponse

/**
 * Created by mymacbookpro on 2020-05-01
 * TODO: Add a class header comment!
 */
class LoginViewModel(private val networkManager: INetworkManager) : BaseViewModel(){

    val loading = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<UIError>()
    var userDataResponse = MutableLiveData<LoginResponse>()

    fun loginAPICall(mobile:String, password:String){
        loading.postValue(true)

        val userLoginRequestObj = LoginRequest(mobile, password)

        val loginRequest = NetworkRequest(
            apiName = USER_LOGIN,
            endPoint = ENDPOINT_USER_LOGIN,
            request = userLoginRequestObj,
            fields = getLoginRequestFieldParams(mobile, password),
            headers = getLoginRequestHeaderParams()
        )
        networkManager.makeAsyncCall(request = loginRequest, callBack = readLoginResponse)
    }

    private val readLoginResponse = object : NetworkCallBack<LoginResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<LoginResponse>) {
            response.data?.status?.let { status ->
                if(status == 1){
                    userDataResponse.value = response.data
                } else{
                    errorLiveData.postValue(
                        UIError(status ?: 0, response.data.message ?: "")
                    )
                }
            }
            if(response.data?.status == null){
                errorLiveData.postValue(UIError(ERROR_CODE_OTHER ,
                    RBMLubricantsApplication.getAppContext()?.getString(R.string.generic_no_internet)?: ""))
            }
            loading.postValue(false)
        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            loading.postValue(false)
            errorLiveData.postValue(
                UIError(error.errorCode ?: 0, error.errorMessage)
            )
        }
    }


    private fun getLoginRequestFieldParams(mobile:String, password:String):MutableMap<String, String>{
        return mutableMapOf(
            USER_LOGIN_MOBILE to mobile,
            USER_LOGIN_PASSWORD to password
        )
    }

    companion object{
        private val factory = LoginViewModelFactory()

        fun getInstance(activity: BaseActivity): LoginViewModel {
            return ViewModelProviders.of(activity, factory)[LoginViewModel::class.java]
        }
    }
}

class LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(getNetworkManager(ManagerFactory.DEV)) as T
    }
}