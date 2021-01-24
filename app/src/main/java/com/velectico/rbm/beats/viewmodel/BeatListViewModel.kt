package com.velectico.rbm.beats.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.viewmodel.BaseViewModel
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.model.UIError
import com.velectico.rbm.beats.model.BeatDateListResponse
import com.velectico.rbm.beats.model.GetBeatDeatilsRequestParams
import com.velectico.rbm.beats.model.ScheduleDates
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.menuitems.model.ResourceListResponse
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
class BeatListViewModel(private val networkManager: INetworkManager) : BaseViewModel(){

    val loading = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<UIError>()
    var allResourcesResponse = MutableLiveData<ResourceListResponse>()
    var allUsersResponse = MutableLiveData<ResourceListResponse>()
    var loginResponse = MutableLiveData<LoginResponse>()

    fun readAllResources(){
        loading.postValue(true)

        val readResourceRequest = NetworkRequest<Unit>(
            apiName = READ_ALL_RESOURCES_DATA,
            endPoint = ENDPOINT_GET_RESOURCES
        )
        networkManager.makeAsyncCall(request = readResourceRequest, callBack = readAllResourcesResponse)
    }

    private val readAllResourcesResponse = object : NetworkCallBack<ResourceListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ResourceListResponse>) {
            allResourcesResponse.value = response.data
            loading.postValue(false)
        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            loading.postValue(false)
            errorLiveData.postValue(
                UIError(
                    error.errorCode ?: 0, error.errorMessage
                )
            )
        }

    }

    fun readAllUsers(){
        loading.postValue(true)

        val params = HashMap<String, String>()
        params["page"] = "2"

        val readUsersRequest = NetworkRequest<Unit>(
            apiName = READ_ALL_USERS_DATA,
            endPoint = ENDPOINT_GET_ALL_USERS,
            queryParameters = params
        )
        networkManager.makeAsyncCall(request = readUsersRequest, callBack = readAllUsersResponse)
    }

    private val readAllUsersResponse = object : NetworkCallBack<ResourceListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ResourceListResponse>) {
            allUsersResponse.value = response.data
            loading.postValue(false)
        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            loading.postValue(false)
            errorLiveData.postValue(
                UIError(
                    error.errorCode ?: 0, error.errorMessage
                )
            )
        }

    }


    fun getBeatListAPICall(userId:String,scheduleId:String){
        loading.postValue(true)
        val leaveListRequestObj = GetBeatDeatilsRequestParams(userId,scheduleId)
        val leaveListRequest = NetworkRequest(
            apiName = GET_TASK_DETAILS_LIST_BY_BEAT_ID,
            endPoint = ENDPOINT_GET_TASK_DETAILS_LIST_BY_BEAT_ID,
            request = leaveListRequestObj,
            fields = getLeaveListRequestFieldParams(userId),
            headers = getLoginRequestHeaderParams()
        )
        networkManager.makeAsyncCall(request = leaveListRequest, callBack = readLeaveListResponse)
    }

    var leaveListData :ArrayList<ScheduleDates> = ArrayList()

    var leaveListResponse = MutableLiveData<BeatDateListResponse>()

    private val readLeaveListResponse = object : NetworkCallBack<BeatDateListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<BeatDateListResponse>) {
            response.data?.status?.let { status ->
                Log.e("test","readLeaveListResponse status="+response.data)
                if(status == 1){
                    leaveListResponse.value = response.data
                    leaveListData = response.data.scheduleDates as ArrayList<ScheduleDates>
                } else{
                    errorLiveData.postValue(
                        UIError(status ?: 0, response.data.message ?: "")
                    )
                }
            }
            if(response.data?.status == null){
                errorLiveData.postValue(
                    UIError(
                        ERROR_CODE_OTHER ,
                        RBMLubricantsApplication.getAppContext()?.getString(R.string.generic_no_internet)?: "")
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


    private fun getLeaveListRequestFieldParams(userId:String):MutableMap<String, String>{
        return mutableMapOf(
            USER_ID to userId
        )
    }

    private fun getRequestFieldParams(userId:String):MutableMap<String, String>{
        return mutableMapOf(
            USER_ID to userId
        )
    }


    companion object{
        private val factory = MenuViewModelFactory()

        fun getInstance(activity: BaseActivity): BeatListViewModel {
            return ViewModelProviders.of(activity, factory)[BeatListViewModel::class.java]
        }
    }
}

class MenuViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BeatListViewModel(getNetworkManager(ManagerFactory.DEV)) as T
    }
}