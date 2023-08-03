package com.muhammhassan.epatrol.domain.usecase

import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface DashboardUseCase {
    fun getUser(): Flow<UserModel>
    suspend fun getTaskList(): Flow<UiState<List<PatrolModel>>>
}