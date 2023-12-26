package com.muhammhassan.epatrol.domain.usecase

import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {
    suspend fun login(email: String, password: String, token: String?): Flow<UiState<UserModel>>
    suspend fun getSavedEmail(): Flow<String?>

    fun getToken(): Flow<String?>
}