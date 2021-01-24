package com.velectico.rbm.redeem.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class SendQrRequestParams(
    @SerializedName("userId") var userId: String?,
    @SerializedName("QR_Code") var QR_Code: String?,
    @SerializedName("QR_Value") var QR_Value: String?,
    @SerializedName("QR_Points") var QR_Points: String?

) : BaseModel()

data class ManualQrRequestParams(
    @SerializedName("userId") var userId: String?,
    @SerializedName("QR_Code") var QR_Code: String?

) : BaseModel()

data class SendQrResponse(
    @SerializedName("status") val status: Int?,
    @SerializedName("respMessage") val respMessage: String?,
    @SerializedName("scanned") val scanned: String?,
    @SerializedName("QAD_ID") val QAD_ID: String?

) : Serializable

data class TotalPointRequestParams(
    @SerializedName("userId") var userId: String?

) : BaseModel()

data class TotalPointResponse(
    @SerializedName("status") val status: Int?,
    @SerializedName("totalScanned") val totalScanned: String?,
    @SerializedName("totalRedeem") val totalRedeem: String?,
    @SerializedName("Total_Point_To_B_Redeem") val Total_Point_To_B_Redeem: Int?
): Serializable


data class ReedemRequestParams(
    @SerializedName("userId") var userId: String?,
    @SerializedName("QR_Points") var QR_Points: String?

) : BaseModel()

data class ReedemResponse(
    val count: Int,
    val respMessage: String,
    val QRD_ID: String


) : Serializable

data class GetRedeemDetailsRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("startDate") var startDate: String,
    @SerializedName("endDate") var endDate: String
) : BaseModel()


data class GetRedeemDetailsResponse(
    val count: Int,
    @SerializedName("Details")
    val RedeemListDetail: List<RedeemListDetails>,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("TotalPoint")
    val TotalPoint: String? = null
)

data class RedeemListDetails(
    var QRD_ID: String? = null,
    var QRD_Req_Date: String? = null,
    var QRD_Payment_Date: String? = null,
    var QRD_Status: String? = null,
    var QRD_Point_Amt: String? = null

) : Serializable