package com.velectico.rbm.payment.models

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class PaymentCollectionRequestParams(
    @SerializedName("userId") var userId: String
): BaseModel()


data class PaymentFailedRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("areaId") var areaId: String
): BaseModel()

data class PaymentFailedResponse(
    @SerializedName("status")
    val status: Int,

    @SerializedName("count")
    val count: Int,

    @SerializedName("details")
    val paymentFailedList: List<PaymentFailedDetails>
)

data class PaymentFailedDetails(
    var Region_Name: String? = null,
    var Zone_Name: String? = null,
    var District_Name:String? = null,
    var Area_Name:String? = null,
    var OH_Dealer_Name:String?=null,
    var OH_Distrib_Name:String? = null,
    var Credit_Days:String?=null,
    var OutStandingAmount:String?=null,
    var Credit_Limit:String?=null

): Serializable


data class PaymentCollectionResponse(
    @SerializedName("status")
    val status: Int,

    @SerializedName("count")
    val count: Int,

    @SerializedName("details")
    val paymentConfirmationList: List<PaymentConfirmDetails>
)
data class PaymentConfirmDetails(
    var collectedAmt: String? = null,
    var OH_ID: String? = null,
    var collectedDate:String? = null,
    var OH_Collected_Confirm_Status:String? = null,
    var orderNo:String?=null,
    var dealName:String? = null,
    var distribName:String?=null,
    var SIH_Invoice_No:String?=null,
    var Pay_Mode: String?=null
): Serializable