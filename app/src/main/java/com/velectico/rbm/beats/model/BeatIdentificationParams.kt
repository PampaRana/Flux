package com.velectico.rbm.beats.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BeatIdentificationParams(
    @SerializedName("userId") var userId: String
)

data class BeatIdentificationResponse(
    @SerializedName("status")
    val status: Int,

    @SerializedName("count")
    val count: Int,

    @SerializedName("incompleteBeat")
    val incompleteBeat: Int,

    @SerializedName("completeBeat")
    val completeBeat: Int,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("FINAL_Array")
    val Beat_Dates: List<BeatDateDetails>


)

data class BeatDateDetails(
    val date: String? = null,
    val status: String? = null
):Serializable


