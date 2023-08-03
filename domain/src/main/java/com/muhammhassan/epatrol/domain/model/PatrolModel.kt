package com.muhammhassan.epatrol.domain.model

data class PatrolModel(
    val id: Long,
    val status: String,
    val title: String,
    val date: String,
    val hour: String,
    val lead: String,
    val verified: Boolean,
    val address: String
)