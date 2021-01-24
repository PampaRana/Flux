package com.velectico.rbm.products.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.model.UIError
import com.velectico.rbm.base.viewmodel.BaseViewModel
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.network.apiconstants.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.INetworkManager
import com.velectico.rbm.network.manager.ManagerFactory
import com.velectico.rbm.network.manager.getNetworkManager
import com.velectico.rbm.network.request.NetworkRequest
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.products.models.ProductListRequest
import com.velectico.rbm.products.models.ProductListResponse

class ProductViewModel(private val networkManager: INetworkManager) : BaseViewModel(){
    val loading = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<UIError>()
    var productListResponse = MutableLiveData<ProductListResponse>()


    fun productListAPICall(userId:String){
        loading.postValue(true)

        val productListRequestObj = ProductListRequest(userId)

        val productListRequest = NetworkRequest(
            apiName = PRODUCT_LIST,
            endPoint = ENDPOINT_PRODUCT_LIST,
            request = productListRequestObj,
            fields = getProductListRequestFieldParams(userId),
            headers = getLoginRequestHeaderParams()
        )
        networkManager.makeAsyncCall(request = productListRequest, callBack = readProductListResponse)
    }

    private fun getProductListRequestFieldParams(userId:String):MutableMap<String, String>{
        return mutableMapOf(
            USER_ID to userId
        )
    }

    private val readProductListResponse = object : NetworkCallBack<ProductListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ProductListResponse>) {
            response.data?.status?.let { status ->
                Log.e("test","status="+status)
                if(status == 1){
                    productListResponse.value = response.data

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

    companion object{
        private val factory = ProductViewModelFactory()

        fun getInstance(activity: BaseActivity): ProductViewModel {
            return ViewModelProviders.of(activity, factory)[ProductViewModel::class.java]
        }
    }
}



class ProductViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductViewModel(getNetworkManager(ManagerFactory.DEV)) as T
    }
}
