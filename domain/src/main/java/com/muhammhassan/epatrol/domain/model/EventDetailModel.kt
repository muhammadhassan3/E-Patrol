package com.muhammhassan.epatrol.domain.model

data class EventDetailModel(
    val id: Long,
    val image: String,
    val summary: String,
    val title: String,
    val action: String,
    val lat: Double,
    val long: Double,
    val createdAt: String,
    val author: String
)
