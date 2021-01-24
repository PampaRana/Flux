package com.velectico.rbm.network

import android.util.Log
import com.velectico.rbm.base.model.BaseModel
import com.velectico.rbm.beats.model.GetBeatDateRequestParams
import com.velectico.rbm.beats.model.GetBeatDeatilsRequestParams
import com.velectico.rbm.expense.model.ExpenseCreateRequest
import com.velectico.rbm.expense.model.ExpenseDeleteRequest
import com.velectico.rbm.expense.model.ExpenseListRequest
import com.velectico.rbm.leave.model.ApplyLeaveRequest
import com.velectico.rbm.leave.model.DeleteLeaveRequest
import com.velectico.rbm.leave.model.LeaveListRequest
import com.velectico.rbm.loginreg.model.LoginRequest
import com.velectico.rbm.network.apiconstants.*
import com.velectico.rbm.network.request.BaseRequest
import com.velectico.rbm.network.request.NetworkRequest
import com.velectico.rbm.products.models.ProductListRequest


/**
 * Created by mymacbookpro on 2020-04-30
 * TODO: Add a class header comment!
 */
fun getCall(service: WebService, request: BaseRequest) : Any?{

    return when(request.apiName){

        READ_ALL_RESOURCES_DATA -> {
            request as NetworkRequest<Unit>
            service.getAllResources()
        }

        READ_ALL_USERS_DATA -> {
            request.queryParameters?.let {
                service.getAllUsers(it)
            }
        }

        USER_LOGIN -> {
            request as NetworkRequest<LoginRequest>
            service.getUserLoginData(request.fields ?: hashMapOf(), request.headers ?: hashMapOf())
        }

        PRODUCT_LIST-> {
            request as NetworkRequest<ProductListRequest>
            service.getProductList(request.fields ?: hashMapOf(), request.headers ?: hashMapOf())

        }

        EXPENSE_LIST -> {
            request as NetworkRequest<ExpenseListRequest>
            service.getExpenseList(request.fields ?: hashMapOf(), request.headers ?: hashMapOf())
        }

        MASTER_DATA_LIST -> {
            request as NetworkRequest<ExpenseListRequest>
            service.getMasterData(request.fields ?: hashMapOf(), request.headers ?: hashMapOf())
        }

        EXPENSE_CREATE_EDIT -> {
             request as NetworkRequest<ExpenseCreateRequest>
             service.createEditExpense(reqBody = request.requestBody,header = request.headers?: hashMapOf())
            //service.createEditExpense(request.fields ?: hashMapOf(), request.file ,request.headers ?: hashMapOf())
            /*service.createEditExpense(userId = request.fields?.get(USER_ID),beatTaskId = request.fields?.get(
                BEAT_TASK_ID),Exp_Head_Id = request.fields?.get(EXP_HEAD_ID),misExpenseAmt = request.fields?.get(
                MIS_EXPENSE_AMOUNT),appliedOnDate = request.fields?.get(APPLIED_ON_DATE),applieedByUserId = request.fields?.get(
                APPLIED_BY_USER_ID),header = request.headers ?: hashMapOf() ,recPhoto = null)*/

            /**
             *  fun createEditExpense(
            @HeaderMap header:Map<String, String>,
            @Body reqBody: RequestBody?
            ): Call<ExpenseCreateResponse?>?
             * */
        }

        EXPENSE_DELETE->{
            request as NetworkRequest<ExpenseDeleteRequest>
            service.deleteExpense(reqBody = request.requestBody,header = request.headers?: hashMapOf())
        }

        LEAVE_REASON -> {
            request as NetworkRequest<DeleteLeaveRequest>
            service.getLeaveReason( request.headers ?: hashMapOf())
        }

        LEAVE_APPLY->{
            request as NetworkRequest<ApplyLeaveRequest>
            service.applyLeave( request.fields ?: hashMapOf(),request.headers ?: hashMapOf())
        }

        LEAVE_EDIT->{
            request as NetworkRequest<ApplyLeaveRequest>
            service.editLeave( request.fields ?: hashMapOf(),request.headers ?: hashMapOf())
        }

        LEAVE_DELETE->{
            request as NetworkRequest<DeleteLeaveRequest>
            service.deleteLeave( request.fields ?: hashMapOf(),request.headers ?: hashMapOf())
        }

        LEAVE_LIST->{
            request as NetworkRequest<LeaveListRequest>
            service.getLeaveList( request.fields ?: hashMapOf(),request.headers ?: hashMapOf())
        }

        GET_ALL_BEAT_DATES->{
            request as NetworkRequest<GetBeatDateRequestParams>
            service.getDatesForBeat( request.fields ?: hashMapOf(),request.headers ?: hashMapOf())
        }

        GET_TASK_DETAILS_LIST_BY_BEAT_ID->{
            request as NetworkRequest<GetBeatDateRequestParams>
            service.getDatesForBeat( request.fields ?: hashMapOf(),request.headers ?: hashMapOf())
        }
        else -> null
    }
}

fun getCall(service: WebService, request: BaseRequest,model: BaseModel) : Any?{

    return when(request.apiName){

        GET_TASK_DETAILS_LIST_BY_BEAT_ID->{
            request as NetworkRequest<GetBeatDeatilsRequestParams>
            service.getTaskDetailsByBeat( model as GetBeatDeatilsRequestParams )
        }
        else -> null
    }



}