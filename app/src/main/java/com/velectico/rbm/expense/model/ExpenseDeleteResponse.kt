package com.velectico.rbm.expense.model

import com.google.gson.annotations.SerializedName

data class ExpenseDeleteResponse(
    val respMessage: String,
    val status: Int
)