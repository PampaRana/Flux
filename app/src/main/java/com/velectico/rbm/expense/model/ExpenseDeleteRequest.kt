package com.velectico.rbm.expense.model

import com.velectico.rbm.base.model.BaseModel
import okhttp3.MultipartBody
import java.io.File

data class ExpenseDeleteRequest(
    val userId: String?,
    val expenseId: String?
): BaseModel()