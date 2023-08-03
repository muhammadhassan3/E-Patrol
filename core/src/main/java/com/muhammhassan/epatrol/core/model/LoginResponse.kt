package com.muhammhassan.epatrol.core.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val user: UserModel,
    val token: String
)