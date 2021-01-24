package com.velectico.rbm.loginreg.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class LoginResponse(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("userDashboardDetails")
    val userDashboardDetails: UserDashboardDetails?,
    @SerializedName("userDetails")
    val userDetails: List<UserDetail>?,
    @SerializedName("message")
    val message: String?


):Parcelable, BaseModel()


data class  forgotPasswordRequestParams(
    @SerializedName("userId") var userId: String?


): BaseModel()
data class forgotPasswordResponse(
    val status: Int,
    val respMessage:String,
    val email:String


): Serializable

data class  ResetPasswordRequestParams(
    @SerializedName("userId") var userId: String?,
    @SerializedName("UM_Password") var UM_Password: String?



): BaseModel()
data class ResetPasswordResponse(
    val status: Int,
    val respMessage:String


): Serializable

