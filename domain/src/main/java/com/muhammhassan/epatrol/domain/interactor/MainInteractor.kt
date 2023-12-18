package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.repository.NotificationRepository
import com.muhammhassan.epatrol.domain.usecase.MainUseCase
import kotlinx.coroutines.flow.Flow

class MainInteractor(private val notif: NotificationRepository): MainUseCase {
    override fun isSubsToGlobalChannel(): Flow<Boolean> {
        return notif.isSubsToGlobalChannel()
    }

    override suspend fun setSubsToGlobalChannel(value: Boolean) {
        notif.setSubsToGlobalChannel(value)
    }
}