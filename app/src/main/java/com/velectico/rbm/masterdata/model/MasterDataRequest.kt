package com.velectico.rbm.masterdata.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

data class MasterDataRequest(
    val DM_Dropdown_Name: String?
): BaseModel()