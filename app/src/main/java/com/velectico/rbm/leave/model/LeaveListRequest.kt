package com.velectico.rbm.leave.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

data class LeaveListRequest(
    @SerializedName("userId")
    val userId: String
):BaseModel()


data class ApproveRejectLeaveListRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("leaveId")
    val leaveId: String

):BaseModel()