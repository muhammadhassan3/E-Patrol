package com.muhammhassan.epatrol.core.repository

import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun isSubsToGlobalChannel(): Flow<Boolean>
    suspend fun setSubsToGlobalChannel(value: Boolean)

    suspend fun setToken(value: String)

    fun getToken(): Flow<String?>

}