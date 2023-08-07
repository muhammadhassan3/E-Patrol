package com.muhammhassan.epatrol.core.model

data class PatrolDetailResponse(
    val id: Long,
    val title: String,
    val status: String,
    val sprin: String,
    val tanggal: String,
    val jam: String,
    val tujuan: String,
    val events: List<PatrolEventResponse>
)