package com.velectico.rbm.products.models

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

data class ProductListRequest(
    @field:SerializedName("userId")
    val userMobile: String?
): BaseModel()