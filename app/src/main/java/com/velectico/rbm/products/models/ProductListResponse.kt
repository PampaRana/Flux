package com.velectico.rbm.products.models

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

data class ProductListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("prodDetails")
    val prodDetails: List<ProductInfo>,  //ArrayList<ProductInfo>
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String?
): BaseModel()