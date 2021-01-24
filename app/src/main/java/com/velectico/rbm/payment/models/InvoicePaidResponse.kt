package com.velectico.rbm.payment.models

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class  InvoiceRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("dealerId") var dealerId: String,
    @SerializedName("distribId") var distribId: String,
    @SerializedName("Pay_Mode") var payMode: String,
    @SerializedName("paidAmt") var paidAmt: String
): BaseModel()



data class InvoicePaidResponse(

    @SerializedName("status")
    val status: Int,

    @SerializedName("respMessage")
    val respMessage: String,

    @SerializedName("Tran_ID")
    val Tran_ID: String

):Serializable