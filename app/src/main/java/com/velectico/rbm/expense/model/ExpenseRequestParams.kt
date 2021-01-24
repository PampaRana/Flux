package com.velectico.rbm.expense.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class  ExpenseRequestParams(
    @SerializedName("userId") var userId: String
): BaseModel()

data class ExpenseBeatResponse(
    val count: Int,
    @SerializedName("Details")
    val expenseDetails: List<ExpenseBeatDetails>,
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String?
)
data class ExpenseBeatDetails(
    val BSD_Dealer_ID: String?,
    val BSD_Distrib_ID: String?,
    val taskName: String?
) : Serializable