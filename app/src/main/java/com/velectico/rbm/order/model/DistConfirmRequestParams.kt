package com.velectico.rbm.order.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

data class  DistConfirmRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("OH_ID") var OH_ID: String
): BaseModel()

data class DistConfirmResponse(
    @SerializedName("respMessage") var respMessage: String,
    @SerializedName("OH_ID") var OH_ID: String,
    @SerializedName("status") var status: Int
)