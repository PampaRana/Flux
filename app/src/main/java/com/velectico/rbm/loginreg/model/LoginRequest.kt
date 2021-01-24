package com.velectico.rbm.loginreg.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

/**
 * Created by mymacbookpro on 2020-05-06
 * TODO: Add a class header comment!
 */
data class LoginRequest(
    @field:SerializedName("userMobile")
    val userMobile: String?,
    @field:SerializedName("userPassword")
    val userPassword: String?
): BaseModel()