package com.muhammhassan.epatrol.domain.usecase

interface NotificationServiceUseCase {
    suspend fun setToken(value: String)
}