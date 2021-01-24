package com.velectico.rbm.beats.viewmodel

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
import com.velectico.rbm.beats.model.BeatAssignments
import com.velectico.rbm.beats.model.BeatDateListResponse
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.beats.model.ScheduleDates
import com.velectico.rbm.leave.model.LeaveListRequest
import com.velectico.rbm.network.apiconstants.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.INetworkManager
import com.velectico.rbm.network.manager.ManagerFactory
import com.velectico.rbm.network.manager.getNetworkManager
import com.velectico.rbm.network.request.NetworkRequest
import com.velectico.rbm.network.response.NetworkResponse

class BeatSharedViewModel(private val networkManager: INetworkManager) : BaseViewModel() {
    var assignmentList = MutableLiveData<MutableList<BeatAssignments>>()
    var beats = MutableLiveData<Beats>()


    val loading = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<UIError>()
    var leaveListData :ArrayList<ScheduleDates> = ArrayList()

    var leaveListResponse = MutableLiveData<BeatDateListResponse>()

    fun setAssignments(item: MutableList<BeatAssignments>) {
        assignmentList.value = item
    }

    fun setBeat(item: Beats){
        beats.value = item
    }


    fun getBeatListAPICall(userId:String){
        loading.postValue(true)
        val leaveListRequestObj = LeaveListRequest(userId)
        val leaveListRequest = NetworkRequest(
            apiName = GET_ALL_BEAT_DATES,
            endPoint = ENDPOINT_BEAT_DATES,
            request = leaveListRequestObj,
            fields = getLeaveListRequestFieldParams(userId),
            headers = getLoginRequestHeaderParams()
        )
        networkManager.makeAsyncCall(request = leaveListRequest, callBack = readLeaveListResponse)
    }


    private val readLeaveListResponse = object : NetworkCallBack<BeatDateListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<BeatDateListResponse>) {
            response.data?.status?.let { status ->
                Log.e("test","readLeaveListResponse status="+status)
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
        private val factory = BeatSharedViewModelFactory()

        fun getInstance(activity: BaseActivity): BeatSharedViewModel {
            return ViewModelProviders.of(activity, factory)[BeatSharedViewModel::class.java]
        }
    }


}

class BeatSharedViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BeatSharedViewModel(getNetworkManager(ManagerFactory.DEV)) as T
    }
}