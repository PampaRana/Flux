package com.velectico.rbm.teamlist.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable


data class  TeamListRequestParams(
    @SerializedName("userId") var userId: String
): BaseModel()

data class TeamListResponse(
    val count: Int,
    @SerializedName("Details")
    val TeamList: List<TeamListDetails>,
    @SerializedName("status")
    val status: Int? = null

)
data class TeamListDetails(
    var UM_ID: String? = null,
    var UM_Name: String? = null,
    var UM_Login_Id:String? = null

): Serializable