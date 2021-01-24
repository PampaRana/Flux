package com.velectico.rbm.menuitems.model


import com.velectico.rbm.base.model.BaseModel
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("color")
    val color: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("pantone_value")
    val pantoneValue: String?,
    @SerializedName("year")
    val year: Int?
): BaseModel()