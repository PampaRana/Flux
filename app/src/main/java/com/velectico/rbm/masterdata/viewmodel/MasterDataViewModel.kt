package com.velectico.rbm.masterdata.viewmodel;



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
import com.velectico.rbm.masterdata.model.MasterDataRequest
import com.velectico.rbm.masterdata.model.MasterDataResponse
import com.velectico.rbm.network.apiconstants.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.INetworkManager
import com.velectico.rbm.network.manager.ManagerFactory
import com.velectico.rbm.network.manager.getNetworkManager
import com.velectico.rbm.network.request.NetworkRequest
import com.velectico.rbm.network.response.NetworkResponse


class MasterDataViewModel(private val networkManager: INetworkManager) : BaseViewModel() {
    val loading = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<UIError>()
    var masterDataListResponse = MutableLiveData<MasterDataResponse>()


    fun expenseListAPICall(dropdownName: String) {
        loading.postValue(true)
        val masterDataListRequestObj = MasterDataRequest(dropdownName)

        val masterDataListRequest = NetworkRequest(
            apiName = MASTER_DATA_LIST,
            endPoint = ENDPOINT_MASTER_DATA_LIST,
            request = masterDataListRequestObj,
            fields = getMasterDataListRequestFieldParams(dropdownName),
            headers = getLoginRequestHeaderParams()
        )
        networkManager.makeAsyncCall(
            request = masterDataListRequest,
            callBack = readMasterDataListResponse
        )
    }

    private fun getMasterDataListRequestFieldParams(dropdownName: String): MutableMap<String, String> {
        return mutableMapOf(
            DROP_DOWN_NAME to dropdownName
        )
    }


    private val readMasterDataListResponse = object : NetworkCallBack<MasterDataResponse>() {
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<MasterDataResponse>) {
            response.data?.size?.let { status ->
                masterDataListResponse.value = response.data
            }
            if (response.data == null) {
                errorLiveData.postValue(
                    UIError(
                        ERROR_CODE_OTHER,
                        RBMLubricantsApplication.getAppContext()
                            ?.getString(R.string.generic_no_internet) ?: ""
                    )
                )
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

    companion object {
        private val factory = MasterDataViewModelFactory()
        fun getInstance(activity: BaseActivity): MasterDataViewModel {
            return ViewModelProviders.of(activity, factory)[MasterDataViewModel::class.java]
        }
    }
}


class MasterDataViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MasterDataViewModel(getNetworkManager(ManagerFactory.DEV)) as T
    }
}

