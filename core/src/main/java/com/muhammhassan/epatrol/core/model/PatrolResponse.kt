package com.muhammhassan.epatrol.core.model

data class PatrolResponse(
    val status: String,
    val alamat: String,
    val tanggal: String,
    val jam: String,
    val ketua: String,
    val verified: Boolean,
    val judul: String,
    val id: Long,
)