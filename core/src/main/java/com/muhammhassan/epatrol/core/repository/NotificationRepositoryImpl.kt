package com.muhammhassan.epatrol.core.repository

import com.muhammhassan.epatrol.core.datasource.local.LocalDataSource
import kotlinx.coroutines.flow.Flow

class NotificationRepositoryImpl(private val local: LocalDataSource) : NotificationRepository {
    override fun isSubsToGlobalChannel(): Flow<Boolean> {
        return local.isSubsToGlobalChannel()
    }

    override suspend fun setSubsToGlobalChannel(value: Boolean) {
        local.setSubsToGlobalChannel(value)
    }

    override suspend fun setToken(value: String) {
        local.setToken(value)
    }

    override fun getToken(): Flow<String?> {
        return local.getToken()
    }
}