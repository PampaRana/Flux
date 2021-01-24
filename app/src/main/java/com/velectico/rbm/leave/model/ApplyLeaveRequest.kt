package com.velectico.rbm.leave.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

data class ApplyLeaveRequest(
    @SerializedName("leaveReasonId")
    val leaveReasonId: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("leaveFromDate")
    val leaveFromDate: String,
    @SerializedName("leaveToDate")
    val leaveToDate: String,
    @SerializedName("leaveReasonOther")
    val leaveReasonOther: String,
    @SerializedName("leaveId")
    val leaveId: String
):BaseModel()

