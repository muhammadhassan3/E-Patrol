package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.repository.NotificationRepository
import com.muhammhassan.epatrol.domain.usecase.NotificationServiceUseCase

class NotificationServiceInteractor(private val repo: NotificationRepository): NotificationServiceUseCase {
    override suspend fun setToken(value: String) {
        repo.setToken(value)
    }

}