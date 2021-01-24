package com.velectico.rbm.complaint.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import com.velectico.rbm.beats.model.AssigntoList
import java.io.Serializable

data class  ComplaintListRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("taskId") var taskId: String,
    @SerializedName("dealreId") var dealreId: String,
    @SerializedName("distribId") var distribId: String,
    @SerializedName("mechId") var mechId: String,
    @SerializedName("CR_Res_Status") var CR_Res_Status: String
): BaseModel()


data class ComplaintListResponse(
    val count: Int,
    @SerializedName("Details")
    val ComplaintList: List<ComplainListDetails>,
    @SerializedName("status")
    val status: Int? = null
)
data class ComplainListDetails(
    var CR_ID: String? = null,
    var CR_BSD_ID: String? = null,
    var ComplaintType: String? = null,
    var prodName: String? = null,
    var CR_Qty: String? = null,
    var CR_Batch_no: String? = null,
    var CR_Remarks: String? = null,
    var CR_Date: String? = null,
    var imagePath: String? = null


): Serializable