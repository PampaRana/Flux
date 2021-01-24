package com.velectico.rbm.payment.models

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

data class  GetInvoiceRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("dealerId") var dealerId: String,
    @SerializedName("distribId") var distribId: String,
    @SerializedName("Pay_Mode") var payMode: String
): BaseModel()