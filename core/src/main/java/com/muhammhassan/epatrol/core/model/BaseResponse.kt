package com.muhammhassan.epatrol.core.model

data class BaseResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String
)
