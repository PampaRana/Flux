package com.velectico.rbm.leave.model

import java.io.Serializable

data class ApplyLeaveResponse(
    val leaveReqId: Int,
    val respMessage: String,
    val status: Int
):Serializable