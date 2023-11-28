package com.muhammhassan.epatrol.domain.model

data class PatrolDetailModel(
    val id: Long,
    val patrolId: Long,
    val title: String,
    val status: String,
    val sprin: String,
    val tanggal: String,
    val jam: String,
    val tujuan: String,
    val lead: String,
)
