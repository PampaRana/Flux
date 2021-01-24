package com.velectico.rbm.loginreg.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDashboardDetails(
    @SerializedName("incentiveAchived")
    val incentiveAchived: String?,
    @SerializedName("noOfDaysAbsent")
    val noOfDaysAbsent: String?,
    @SerializedName("noOfDaysPresent")
    val noOfDaysPresent: String?,
    @SerializedName("paymentShortfall")
    val paymentShortfall: String?,
    @SerializedName("targetShortfall")
    val targetShortfall: String?
): Parcelable, BaseModel()