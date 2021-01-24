package com.velectico.rbm.payment.models

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class  PaymentHistoryRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("dealerId") var dealerId: String,
    @SerializedName("distribId") var distribId: String
): BaseModel()

data class DealDistPaymentParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("UM_Role") var UM_Role: String
):BaseModel()

data class PaymentHistoryResponse(

    @SerializedName("status")
    val status: Int,

    @SerializedName("count")
    val count: Int,

    @SerializedName("Details")
    val paymentOutStandingList: List<PaymentHistoryDetails>

)

data class PaymentHistoryDetails(
    var PH_Tran_ID: String? = null,
    var tran_amount: String? = null,
    var PH_Invoice_No:String? = null,
    var payDate:String? = null

): Serializable


data class OutStandingResponse(

    @SerializedName("status")
    val status: Int,

    @SerializedName("count")
    val count: Int,

    @SerializedName("totalOutStanding")
    val totalOutStanding: String,

    @SerializedName("Details")
    val paymentOutStandingList: List<OutStandingInfoDetails>

)

data class OutStandingInfoDetails(
    var SIH_ID: String? = null,
    var SIH_Invoice_No: String? = null,
    var SIH_Order_No:String? = null,
    var SIH_Invoice_Amt:String? = null,
    var SIH_Disc_Amt:String? = null,
    var SIH_Sale_Type:String? = null,
    var SIH_Pay_Mode:String? = null,
    var SIH_Due_Date:String? = null,
    var invoiceDate:String? = null,
    var Paid_Amount:String? = null,
    var Due_Amount:String? = null

): Serializable