package com.velectico.rbm.leave.viewmodel

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
import com.velectico.rbm.leave.model.*
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
class LeaveViewModel(private val networkManager: INetworkManager) : BaseViewModel(){

    val loading = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<UIError>()
    var leaveListData :ArrayList<LeaveListModel> = ArrayList()

    var leaveListResponse = MutableLiveData<LeaveListResponse>()
    var deleteLeaveResponse = MutableLiveData<DeleteLeaveResponse>()
    var leaveReasonResponse = MutableLiveData<LeaveReasonResponse>()
    var applyLeaveResponse = MutableLiveData<ApplyLeaveResponse>()



    fun getLeaveListAPICall(userId:String){
        loading.postValue(true)
        val leaveListRequestObj = LeaveListRequest(userId)
        val leaveListRequest = NetworkRequest(
            apiName = LEAVE_LIST,
            endPoint = ENDPOINT_LEAVE_LIST,
            request = leaveListRequestObj,
            fields = getLeaveListRequestFieldParams(userId),
            headers = getLoginRequestHeaderParams()
        )
        networkManager.makeAsyncCall(request = leaveListRequest, callBack = readLeaveListResponse)
    }

    fun deleteLeaveAPICall(userId:String,leaveId:Int){
        loading.postValue(true)
        val deleteLeaveRequestObj = DeleteLeaveRequest(userId,leaveId)
        val leaveDeleteRequest = NetworkRequest(
            apiName = LEAVE_DELETE,
            endPoint = ENDPOINT_LEAVE_DELETE,
            request = deleteLeaveRequestObj,
            fields = getDeleteLeaveRequestFieldParams(userId,leaveId.toString()),
            headers = getLoginRequestHeaderParams()
        )
        networkManager.makeAsyncCall(request = leaveDeleteRequest, callBack = readDeleteLeaveResponse)
    }

    fun getLeaveReasonAPICall(){
        loading.postValue(true)
        val leaveReasonRequest = NetworkRequest(
            apiName = LEAVE_REASON,
            endPoint = ENDPOINT_LEAVE_REASON,
            request = null,
            fields = null,
            headers = getLoginRequestHeaderParams()
        )
        networkManager.makeAsyncCall(request = leaveReasonRequest, callBack = readLeaveReasonResponse)
    }

    fun applyLeaveAPICall(applyLeaveRequest:ApplyLeaveRequest,isEdit:Boolean){
        loading.postValue(true)
       // val deleteLeaveRequestObj = ApplyLeaveRequest(userId,leaveId)
        val applyLeaveRequest = NetworkRequest(
            apiName = if(isEdit) LEAVE_EDIT  else LEAVE_APPLY,
            endPoint = if(isEdit) ENDPOINT_LEAVE_EDIT  else ENDPOINT_LEAVE_APPLY,
            request = applyLeaveRequest,
            fields = getApplyLeaveRequestFieldParams(applyLeaveRequest,isEdit),
            headers = getLoginRequestHeaderParams()
        )
        networkManager.makeAsyncCall(request = applyLeaveRequest, callBack = readApplyLeaveResponse)
    }

    private fun getLeaveListRequestFieldParams(userId:String):MutableMap<String, String>{
        return mutableMapOf(
            USER_ID to userId
        )
    }

    private fun getDeleteLeaveRequestFieldParams(userId:String,leaveId: String):MutableMap<String, String>{
        return mutableMapOf(
            USER_ID to userId,
            LEAVE_ID to leaveId
        )
    }

    private fun getApplyLeaveRequestFieldParams(applyLeaveRequest:ApplyLeaveRequest,isEdit:Boolean):MutableMap<String, String>{
        if(isEdit)
        {
            return mutableMapOf(
                USER_ID to applyLeaveRequest.userId,
                LEAVE_FROM to applyLeaveRequest.leaveFromDate,
                LEAVE_TO to applyLeaveRequest.leaveToDate,
                LEAVE_REASON_ID to applyLeaveRequest.leaveReasonId.toString(),
                LEAVE_REASON_OTHER to applyLeaveRequest.leaveReasonOther,
                LEAVE_ID to applyLeaveRequest.leaveId
            )
        }
        else{
            return mutableMapOf(
                USER_ID to applyLeaveRequest.userId,
                LEAVE_FROM to applyLeaveRequest.leaveFromDate,
                LEAVE_TO to applyLeaveRequest.leaveToDate,
                LEAVE_REASON_ID to applyLeaveRequest.leaveReasonId.toString(),
                LEAVE_REASON_OTHER to applyLeaveRequest.leaveReasonOther
            )
        }

    }

    private val readLeaveListResponse = object : NetworkCallBack<LeaveListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<LeaveListResponse>) {
            response.data?.status?.let { status ->
                Log.e("test","readLeaveListResponse status="+status)
                if(status == 1){
                    leaveListResponse.value = response.data
                    leaveListData = response.data.leaveDetails as ArrayList<LeaveListModel>

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

    private val readDeleteLeaveResponse = object : NetworkCallBack<DeleteLeaveResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DeleteLeaveResponse>) {
            response.data?.status?.let { status ->
                Log.e("test","DeleteLeaveResponse status="+status)
                if(status == 1){
                    deleteLeaveResponse.value = response.data

                } else{
                    errorLiveData.postValue(
                        UIError(status ?: 0, response.data.respMessage ?: "")
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

    private val readLeaveReasonResponse = object : NetworkCallBack<LeaveReasonResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<LeaveReasonResponse>) {
            response.data?.let { status ->
                Log.e("test","readLeaveReasonResponse status="+status)
                leaveReasonResponse.value = response.data
                /*if(status == 1){


                } else{
                    errorLiveData.postValue(
                        UIError(status ?: 0, response.data.respMessage ?: "")
                    )
                }*/
            }
            if(response.data == null){
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


    private val readApplyLeaveResponse = object : NetworkCallBack<ApplyLeaveResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ApplyLeaveResponse>) {
            response.data?.status?.let { status ->
                Log.e("test","readApplyLeaveResponse status="+status)
                if(status == 1){
                    applyLeaveResponse.value = response.data

                } else{
                    errorLiveData.postValue(
                        UIError(status ?: 0, response.data.respMessage ?: "")
                    )
                }
            }
            if(response.data == null){
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
        private val factory = LeaveViewModelFactory()
        fun getInstance(activity: BaseActivity): LeaveViewModel {
            return ViewModelProviders.of(activity, factory)[LeaveViewModel::class.java]
        }
    }
}

class LeaveViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LeaveViewModel(getNetworkManager(ManagerFactory.DEV)) as T
    }
}