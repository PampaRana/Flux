package com.velectico.rbm.leave.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class LeaveListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("leaveDetails")
    val leaveDetails: List<LeaveListModel>,
    @SerializedName("status")
    val status: Int? = null
):BaseModel()

data class LeaveListModel(
    val leaveID: String?,
    val leaveReasonId: String?,
    val leaveReasonName: String?,
    val leaveFrom: String?,
    val leaveTo: String?,
    val LD_SUM_UM_ID: String?,
    val LD_Other_Reason: String?,
    val leaveAppliedOn: String?,
    val leaveModifiedOn: String?,
    val LD_Status: String?,
    val LD_Approved_By: String?,
    val LD_Rejected_By: String?,
    val LD_Approved_Date: String?,
    val LD_Rejected_Date: String?
):Serializable