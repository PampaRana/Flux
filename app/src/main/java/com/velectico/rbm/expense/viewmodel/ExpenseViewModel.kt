package com.velectico.rbm.expense.viewmodel

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
import com.velectico.rbm.expense.model.*
import com.velectico.rbm.network.apiconstants.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.INetworkManager
import com.velectico.rbm.network.manager.ManagerFactory
import com.velectico.rbm.network.manager.getNetworkManager
import com.velectico.rbm.network.request.NetworkRequest
import com.velectico.rbm.network.response.NetworkResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody


class ExpenseViewModel(private val networkManager: INetworkManager) : BaseViewModel(){
    val loading = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<UIError>()
    var expenseListResponse = MutableLiveData<ExpenseListResponse>()
    var expenseCreateResponse = MutableLiveData<ExpenseCreateResponse>()
    var expenseDeleteResponse = MutableLiveData<ExpenseDeleteResponse>()




//    fun expenseCreateAPICall(expenseCreateRequest : ExpenseCreateRequest){
//        loading.postValue(true)
//        //val expenseCreateRequestObj = ExpenseCreateRequest(userId,beatTaskId,Exp_Head_Id,misExpenseAmt,appliedOnDate,applieedByUserId,recPhoto)
//
//        val expenseCreateRequest = NetworkRequest(
//            apiName = EXPENSE_CREATE_EDIT,
//            endPoint = ENDPOINT_EXPENSE_CREATE_EDIT,
//            request = expenseCreateRequest,
//            requestBody= getExpenseCreateRequestBody(expenseCreateRequest),
//            headers = getLoginRequestHeaderParams()
//        )
//        networkManager.makeAsyncCall(request = expenseCreateRequest, callBack = readExpenseCreateResponse)
//    }

    fun expenseListAPICall(userId:String){
        loading.postValue(true)
        //val productListRequestObj = ExpenseListRequest(userId)
        val expenseListRequestObj = ExpenseListRequest(userId)

        val expenseListRequest = NetworkRequest(
            apiName = EXPENSE_LIST,
            endPoint = ENDPOINT_EXPENSE_LIST,
            request = expenseListRequestObj,
            fields = getExpenseListRequestFieldParams(userId),
            headers = getLoginRequestHeaderParams()
        )
        networkManager.makeAsyncCall(request = expenseListRequest, callBack = readExpenseListResponse)
    }

    fun expenseDeleteAPICall(expenseDeleteRequest : ExpenseDeleteRequest){
        loading.postValue(true)


        val expenseDeleteRequest = NetworkRequest(
            apiName = EXPENSE_DELETE,
            endPoint = ENDPOINT_DELETE_EXPENSE,
            request = expenseDeleteRequest,
            requestBody= getExpenseDeleteRequestBody(expenseDeleteRequest),
            headers = getLoginRequestHeaderParams()
        )
        networkManager.makeAsyncCall(request = expenseDeleteRequest, callBack = readExpenseDeleteResponse)
    }

    private fun getExpenseListRequestFieldParams(userId:String):MutableMap<String, String>{
        return mutableMapOf(
            USER_ID to userId
        )
    }
    //getExpenseDeleteRequestBody

    private fun getExpenseDeleteRequestBody(expensedeleteRequest : ExpenseDeleteRequest):RequestBody{
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart(USER_ID, expensedeleteRequest.userId.toString())
            .addFormDataPart(EXPENSE_ID, expensedeleteRequest.expenseId.toString())
        return builder.build();
    }

//    private fun getExpenseCreateRequestBody(expenseCreateRequest : ExpenseCreateRequest):RequestBody{
//        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
//        builder.addFormDataPart(USER_ID, expenseCreateRequest.userId)
//            .addFormDataPart(BEAT_TASK_ID, expenseCreateRequest.beatTaskId.toString())
//            .addFormDataPart(EXP_HEAD_ID, expenseCreateRequest.Exp_Head_Id.toString())
//            .addFormDataPart(MIS_EXPENSE_AMOUNT, expenseCreateRequest.Exp_Head_Id.toString())
//            .addFormDataPart(APPLIED_ON_DATE, expenseCreateRequest.appliedOnDate)
//            .addFormDataPart(APPLIED_BY_USER_ID, expenseCreateRequest.applieedByUserId)
//         if(expenseCreateRequest.recPhoto!=null){
//             if (expenseCreateRequest.recPhoto.exists()) {
//                 builder.addFormDataPart(FILE_TO_UPLOAD, expenseCreateRequest.recPhoto.getName(), RequestBody.create(MultipartBody.FORM, expenseCreateRequest.recPhoto));
//             }
//         }
//
//        return builder.build();
//    }

    private val readExpenseListResponse = object : NetworkCallBack<ExpenseListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ExpenseListResponse>) {
            response.data?.status?.let { status ->
                Log.e("test","status="+status)
                if(status == 1){
                    expenseListResponse.value = response.data

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

    private val readExpenseDeleteResponse = object : NetworkCallBack<ExpenseDeleteResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ExpenseDeleteResponse>) {
            response.data?.status?.let { status ->
                Log.e("test","readExpenseDeleteResponse status="+status)
                if(status == 1){
                    expenseDeleteResponse.value = response.data

                } else{
                    errorLiveData.postValue(
                        UIError(0, "Cannot delete")
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

    private val readExpenseCreateResponse = object : NetworkCallBack<ExpenseCreateResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ExpenseCreateResponse>) {
            response.data?.status?.let { status ->
                Log.e("test","status="+status)
                if(status == 1){
                    expenseCreateResponse.value = response.data

                } else{
                    errorLiveData.postValue(
                        UIError(0, "Cannot create")
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
        private val factory = ExpenseViewModelFactory()
        fun getInstance(activity: BaseActivity): ExpenseViewModel {
            return ViewModelProviders.of(activity, factory)[ExpenseViewModel::class.java]
        }
    }
}



class ExpenseViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExpenseViewModel(getNetworkManager(ManagerFactory.DEV)) as T
    }
}
