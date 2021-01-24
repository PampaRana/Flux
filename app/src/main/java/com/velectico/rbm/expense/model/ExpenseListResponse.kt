package com.velectico.rbm.expense.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ExpenseListResponse(
    val count: Int,
    val expenseDetails: List<Expense>,
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String?
)

data class BidListResponse(
    val count: Int,
    @SerializedName("Details")
    val expenseDetails: List<Details>,
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String?
)

data class Details(
    val BSD_Dealer_ID: String?,
    val BSD_Distrib_ID: String?,
    val taskId: String?,
    val beatId: String?,
    val taskName: String?
) : Serializable


data class ExpenseResponse(
    @SerializedName("expenseHead")
    val expenseDetails: List<ExpenseDetails>,
    @SerializedName("status")
    val status: Int,
    @SerializedName("count")
    val count: Int?
) : Serializable

data class ExpenseDetails(
    val expenseId: String? = null,
    val expDate: String? = null,
    val beatName: String? = null,
    val totalexpAmt: String? = null,
    val expenseStatus: String? = null,
    val beatTaskId: String? = null,
    val beatId: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val approvedBy: String? = null,
    val approvedByName: String? = null,
    val appliedOnDate: String? = null,
    val ER_Approve_Date: String? = null,
    val recPhoto1: String? = null,
    val recPhoto2: String? = null,
    val recPhoto3: String? = null,
    val recPhoto4: String? = null,
    val recPhoto5: String? = null,
    val recPhoto6: String? = null,
    @SerializedName("details")
    val expenseDtls: List<EetailsA>?=null
) : Serializable

data class EetailsA(
    val ERD_ID: String?=null,
    val expAmt: String?=null,
    val expDate: String?=null,
    val km_run: String?=null,
    val expType: String?=null

) : Serializable