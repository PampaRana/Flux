package com.velectico.rbm.expense.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import okhttp3.MultipartBody
import java.io.File
import java.io.Serializable

data class ExpenseCreateRequest(
    @SerializedName("userId") var userId: String?,
    @SerializedName("taskId") val taskId: String?,
    @SerializedName("beatId") val beatId: String?,
    @SerializedName("expDate") val expDate: String?,
    @SerializedName("ExpDetails") val expDetails:List<ExpDetailsRequest>?
): BaseModel()

data class ExpDetailsRequest(
    @SerializedName("expHeadId") var expHeadId: String?,
    @SerializedName("expAmt") var expAmt: String?,
    @SerializedName("km_run") var km_run: String?,
    @SerializedName("expDate") var expDate: String?
): BaseModel()

data class UpdateExpenseRequest(
    @SerializedName("userId") var userId: String?,
    @SerializedName("beatTaskId") val beatTaskId: String?,
    @SerializedName("beatId") val beatId: String?,
    @SerializedName("expenseId") var expenseId: String?,
    @SerializedName("expDate") val expDate: String?,
    @SerializedName("expenseStatus") var expenseStatus: String?,
    @SerializedName("ExpDetails") val expDetails:List<UpdateExpDetailsRequest>?
): BaseModel()

data class UpdateExpDetailsRequest(
    @SerializedName("ERD_ID") var ERD_ID: String?,
    @SerializedName("expHeadId") var expHeadId: String?,
    @SerializedName("expAmt") var expAmt: String?,
    @SerializedName("km_run") var km_run: String?,
    @SerializedName("expDate") var expDate: String?
): BaseModel()


data class SuccessResponse(
    @SerializedName("status") val status: Int?,
    @SerializedName("respMessage") val respMessage: String?,
    @SerializedName("complainId") val complainId: String?
    /*val status: Int?,
    val respMessage: String?,
    val expensId: Int?*/
):Serializable

data class CreateExpenseResponse(
    @SerializedName("status") val status: Int?,
    @SerializedName("respMessage") val respMessage: String?,
    @SerializedName("expensId") val expensId: Int?
    /*val status: Int?,
    val respMessage: String?,
    val expensId: Int?*/
):Serializable

data class ComplaintCreateRequest(
    val userId: String?,
    val complaintype: String?,
    val CR_Distrib_ID: String?,
    val CR_Dealer_ID: String?,
    val CR_Mechanic_ID: String?,
    val CR_Qty : String?,
    val CR_Batch_no : String?,
    val CR_Remarks : String?,
    val recPhoto : File?,
    val taskId : String?,
    val prodName : String?
): BaseModel()