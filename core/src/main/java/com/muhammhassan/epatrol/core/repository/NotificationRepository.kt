package com.muhammhassan.epatrol.core.repository

import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun isSubsToGlobalChannel(): Flow<Boolean>
    suspend fun setSubsToGlobalChannel(value: Boolean)
}