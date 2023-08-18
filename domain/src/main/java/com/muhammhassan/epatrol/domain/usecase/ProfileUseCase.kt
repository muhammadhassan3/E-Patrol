package com.muhammhassan.epatrol.domain.usecase

import com.muhammhassan.epatrol.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface ProfileUseCase {
    fun getUser(): Flow<UserModel>

    suspend fun logout()
}