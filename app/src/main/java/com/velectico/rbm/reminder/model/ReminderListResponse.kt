package com.velectico.rbm.reminder.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class  ReminderListRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("type") var type: String
): BaseModel()


data class ReminderListResponse(
    val count: Int,
    @SerializedName("Details")
    val ReminderListDetail: List<ReminderListDetails>,
    @SerializedName("status")
    val status: Int? = null
)
data class ReminderListDetails(
    var remId: String? = null,
    var beatName: String? = null,
    var dealName: String? = null,
    var distribName: String? = null,
    var dummyDealerName: String? = null,
    var RM_Followup_Date: String? = null,
    var RM_Desc: String? = null



): Serializable


data class  CreateReminderRequestParams(
    @SerializedName("userId") var userId: String?,
    @SerializedName("taskId") var taskId: String?,
    @SerializedName("dealerId") var dealerId: String?,
    @SerializedName("distribId") var distribId: String?,
    @SerializedName("followupDate") var followupDate: String?,
    @SerializedName("followupDesc") var followupDesc: String?


): BaseModel()
data class CreateReminderResponse(
    val count: Int,
    val respMessage:String,
    val reportId:String


):Serializable