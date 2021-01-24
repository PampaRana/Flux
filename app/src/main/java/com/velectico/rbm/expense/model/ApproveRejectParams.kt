package com.velectico.rbm.expense.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

data class  ApproveRejectParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("status") var expenseStatus: String,
    @SerializedName("expenseId") var expenseId: String

):BaseModel()

