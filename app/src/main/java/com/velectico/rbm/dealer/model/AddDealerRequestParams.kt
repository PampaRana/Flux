package com.velectico.rbm.dealer.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import com.velectico.rbm.beats.model.OrderListDetails
import com.velectico.rbm.beats.model.OrderProductListDetails
import com.velectico.rbm.expense.model.ExpDetailsRequest
import java.io.Serializable

data class AddDealerRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("DD_Name") var DD_Name: String,
    @SerializedName("DD_Mobile") var DD_Mobile: String,
    @SerializedName("DD_Mobile_Optional") var DD_Mobile_Optional: String,
    @SerializedName("DD_Address") var DD_Address: String,
    @SerializedName("DD_Area") var DD_Area: String,
    @SerializedName("DD_Contact_Name") var DD_Contact_Name: String,
    @SerializedName("DD_Segment") var DD_Segment: String,
    @SerializedName("DD_Sale_Per_Day") var DD_Sale_Per_Day: String,
    @SerializedName("DD_Grading") var DD_Grading: String,
    @SerializedName("DD_Feedback") var DD_Feedback: String,
    @SerializedName("DD_Reminder") var DD_Reminder: String,
    @SerializedName("DD_Present_Sup_Names") var DD_Present_Sup_Names: String,
    @SerializedName("collectionArray") val collectionArray: List<CollectionRequest>?

) : BaseModel()
data class UpdateDealerRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("DD_ID") var DD_ID: String,
    @SerializedName("DD_Name") var DD_Name: String,
    @SerializedName("DD_Mobile") var DD_Mobile: String,
    @SerializedName("DD_Mobile_Optional") var DD_Mobile_Optional: String,
    @SerializedName("DD_Address") var DD_Address: String,
    @SerializedName("DD_Area") var DD_Area: String,
    @SerializedName("DD_Contact_Name") var DD_Contact_Name: String,
    @SerializedName("DD_Segment") var DD_Segment: String,
    @SerializedName("DD_Sale_Per_Day") var DD_Sale_Per_Day: String,
    @SerializedName("DD_Grading") var DD_Grading: String,
    @SerializedName("DD_Feedback") var DD_Feedback: String,
    @SerializedName("DD_Reminder") var DD_Reminder: String,
    @SerializedName("DD_Present_Sup_Names") var DD_Present_Sup_Names: String,
    @SerializedName("collectionArray") val collectionArray: List<CollectionRequest>?,
    @SerializedName("feedbackArray") val feedbackArray: List<FeedbackRequest>?

)

data class FeedbackRequest(
    @SerializedName("feedBack") var feedBack: String?,
    @SerializedName("Reminder_Date") var Reminder_Date: String?
) : BaseModel()
data class CollectionRequest(
    @SerializedName("DD_Pref_Company") var DD_Pref_Company: String?,
    @SerializedName("DD_Grade") var DD_Grade: String?,
    @SerializedName("DD_Packaging") var DD_Packaging: String?,
    @SerializedName("DD_Price") var DD_Price: String?,
    @SerializedName("DD_Volume") var DD_Volume: String?
) : BaseModel()

data class ValidateMobileRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("DD_Mobile") var DD_Mobile: String
) : BaseModel()

data class MessageResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("respMessage") var respMessage: String
): Serializable



data class AddDealerResponse(
    @SerializedName("respMessage") var respMessage: String,
    @SerializedName("DD_ID") var DD_ID: Int,
    @SerializedName("status") var status: Int
)

data class DealerRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("DD_Area") var DD_Area: String
) : BaseModel()

data class ExistDealerRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("areaId") var areaId: String
) : BaseModel()

data class DealerAreaParams(
    @SerializedName("userId") var userId: String
) : BaseModel()

data class DealerListResponse(
    val count: Int,
    @SerializedName("Details")
    val DealerList: List<DealerListDetails>,
    @SerializedName("status")
    val status: Int? = null

)

data class AreaResponse(
    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("count")
    val count: Int? = null,

    @SerializedName("Details")
    val AreaList: List<AreaDetails>
): Serializable

data class DealerListDetails(
    val DD_ID: String? = null,
    val DD_Name: String? = null,
    val DD_Mobile: String? = null,
    val DD_Mobile_Optional: String? = null,
    val DD_Grading: String? = null,
    val DD_Grading_Name: String? = null,
    val DD_FeedBack: String? = null,
    val DD_Address: String? = null,
    val DD_Area: String? = null,
    val AreaName: String?=null,
    val Create_Date: String? = null,
    val Created_By: String? = null,
    val DD_Email: String? = null,
    val DD_Contact_Name: String? = null,
    val DD_Segment: String? = null,
    val Segment_Name: String? = null,
    val DD_Sale_Per_Day: String? = null,
    val DD_Present_Sup_Names: String? = null,
    val DD_Scheme_Offer: String? = null,
    val DD_Image: String? = null,
    val imagePath: String? = null,
    val DD_Reminder: String? = null,
    @SerializedName("details")
    val details: List<DealerInfo> = emptyList(),
    @SerializedName("feedback")
    val feedback: List<FeedbackDetails> = emptyList()
) : Serializable

data class AreaDetails(
    val AM_ID: String? = null,
    val AM_Area_Name: String? = null
) : Serializable


data class DealerInfo(
    val ID: String? = null,
    val DD_ID: String? = null,
    val DD_Pref_Company: String? = null,
    val DD_Grade: String? = null,
    val DD_Packaging: String? = null,
    val DD_Volume: String? = null,
    val DD_Price: String? = null,
    val Create_Date: String? = null,
    val Created_By: String? = null,
    val DD_Pref_Company_Name: String? = null,
    val DD_Grade_Name: String? = null,
    val DD_Packaging_Name: String? = null
    ) : Serializable

data class FeedbackDetails(
    val ID: String? = null,
    val DD_ID: String? = null,
    val Description: String? = null,
    val Reminder_Date: String? = null,
    val Reminder_Info: String? = null,
    val Create_Date: String? = null,
    val Created_By  : String? = null
) : Serializable

data class ExistingDealerResponse(
    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("count")
    val count: Int? = null,

    @SerializedName("dealerDetails")
    val ExistDealerList: List<ExistDealerDetails>
): Serializable

data class ExistDealerDetails(
    val UM_ID: String? = null,
    val UM_Name: String? = null,
    val UM_Login_Id: String? = null,
    val DM_Area: String? = null,
    val UM_Phone: String? = null,
    val DM_Contact_Person: String? = null,
    val DM_Address  : String? = null,
    val DM_Segment  : String? = null,
    val DM_Segment_Name  : String? = null,
    val DM_Area_Name  : String? = null
) : Serializable

data class UpdateExistingDealerParams(
    @SerializedName("UM_ID") var UM_ID: String,
    @SerializedName("UM_Login_Id") var UM_Login_Id: String,
    @SerializedName("DM_Contact_Person") var DM_Contact_Person: String,
    @SerializedName("DM_Address") var DM_Address: String,
    @SerializedName("DM_Segment") var DM_Segment: String
) : BaseModel()

data class UpdateExistingDealerResponse(
    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("respMessage")
    val respMessage: String? = null,

    @SerializedName("count")
    val UM_ID: String? = null
): Serializable

