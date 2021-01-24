package com.velectico.rbm.expense.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

data class ExpenseListRequest(
    val userId: String? //9123654934
): BaseModel()