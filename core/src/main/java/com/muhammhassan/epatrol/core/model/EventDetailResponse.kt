package com.muhammhassan.epatrol.core.model

import com.google.gson.annotations.SerializedName

data class EventDetailResponse(
    val id: Long,
    @SerializedName("foto")
    val image: String,
    @SerializedName("uraian_kejadian")
    val summary: String,
    @SerializedName("penemuan_kejadian")
    val title: String,
    @SerializedName("tindakan")
    val action: String,
    @SerializedName("latitude")
    val lat: Double,
    @SerializedName("longitude")
    val long: Double,
    @SerializedName("tgl_kejadian")
    val createdAt: String,
    @SerializedName("writer")
    val author: String
)