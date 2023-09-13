package com.muhammhassan.epatrol.core.model

import com.google.gson.annotations.SerializedName

data class PatrolEventResponse(
    @SerializedName("list data kejadian")
    val list: List<PatrolEventData>
)

data class PatrolEventData(
    val id: Long,
    @SerializedName("uraian_kejadian")
    val summary: String,
    @SerializedName("penemuan_kejadian")
    val title: String,
    @SerializedName("tgl_kejadian")
    val createdAt: String,
    @SerializedName("writer")
    val author: String,
    @SerializedName("tindakan")
    val action: String
)