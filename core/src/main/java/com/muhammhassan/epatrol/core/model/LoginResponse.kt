package com.muhammhassan.epatrol.core.model

data class LoginResponse(
    val user: UserModel,
    val token: String
)