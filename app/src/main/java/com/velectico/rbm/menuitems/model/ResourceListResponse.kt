package com.velectico.rbm.menuitems.model


import com.velectico.rbm.base.model.BaseModel
import com.google.gson.annotations.SerializedName
import com.velectico.rbm.menuitems.model.Ad
import com.velectico.rbm.menuitems.model.Data

data class ResourceListResponse(
    @SerializedName("ad")
    val ad: Ad?,
    @SerializedName("data")
    val `data`: List<Data>?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("per_page")
    val perPage: Int?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("total_pages")
    val totalPages: Int?
): BaseModel()