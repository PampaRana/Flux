package com.velectico.rbm.menuitems.viewmodel

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class  AttendanceRequestParams(
    @SerializedName("userId") var userId: String
): BaseModel()


data class  AttendanceRequestOutParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("AM_ID") var AM_ID: String
): BaseModel()


data class AttendancOutResponse(
    @SerializedName("respMessage") var respMessage: String,
    @SerializedName("AM_ID") var AM_ID: String,
    @SerializedName("status") var status: Int
)
data class AttendancResponse(
    @SerializedName("respMessage") var respMessage: String,
    @SerializedName("AM_ID") var AM_ID: Int,
    @SerializedName("status") var status: Int
)