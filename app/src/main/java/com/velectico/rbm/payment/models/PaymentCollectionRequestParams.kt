package com.velectico.rbm.payment.models

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class PaymentCollectionRequestParams(
    @SerializedName("userId") var userId: String
): BaseModel()

data class PaymentCollectionResponse(
    @SerializedName("status")
    val status: Int,

    @SerializedName("count")
    val count: Int,

    @SerializedName("details")
    val paymentConfirmationList: List<PaymentConfirmDetails>
)
data class PaymentConfirmDetails(
    var collectedAmt: String? = null,
    var OH_ID: String? = null,
    var collectedDate:String? = null,
    var orderNo:String? = null
): Serializable