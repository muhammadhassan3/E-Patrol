package com.muhammhassan.epatrol.domain.model

data class PatrolEventModel(
    val id: Long,
    val summary: String,
    val title: String,
    val action: String,
    val createdAt: String,
    val author: String,
)