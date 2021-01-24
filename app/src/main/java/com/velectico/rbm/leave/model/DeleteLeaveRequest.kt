package com.velectico.rbm.leave.model

data class DeleteLeaveRequest(
    val userId: String,
    val leaveId: Int
)