package com.muhammhassan.epatrol.core.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("nama_lengkap") val name: String?, val email: String?, val nrp: String?
)