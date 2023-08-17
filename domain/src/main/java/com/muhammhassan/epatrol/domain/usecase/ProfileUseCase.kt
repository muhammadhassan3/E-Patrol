package com.muhammhassan.epatrol.domain.usecase

import com.muhammhassan.epatrol.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface ProfileUseCase {
    suspend fun getUser(): Flow<UserModel>
}