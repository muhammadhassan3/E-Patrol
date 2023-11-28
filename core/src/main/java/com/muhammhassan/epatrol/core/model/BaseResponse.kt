package com.muhammhassan.epatrol.core.model

data class BaseResponse<T>(
    val error: Boolean,
    val data: T? = null,
    val message: String
)
