package com.velectico.rbm.order.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class  CreditDaysOutStandingRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("dealerId") var dealerId: String,
    @SerializedName("distribId") var distribId: String
): BaseModel()

data class CreditDaysOutStandingResponse(
    @SerializedName("status")
    val status: Int,

    @SerializedName("totalOutStanding")
    val totalOutStanding: String,

    @SerializedName("Creadit_Days")
    val Creadit_Days: String
): Serializable