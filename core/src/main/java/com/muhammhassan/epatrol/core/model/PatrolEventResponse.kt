package com.muhammhassan.epatrol.core.model

data class PatrolEventResponse(
    val id: Long,
    val image: String,
    val summary: String,
    val title: String,
    val action: String,
    val lat: Double,
    val long: Double,
    val createdAt: String,
)