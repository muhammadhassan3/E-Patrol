package com.muhammhassan.epatrol.domain.usecase

import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface TaskListUseCase {
    suspend fun getTaskList(): Flow<UiState<List<PatrolModel>>>
    fun getUser(): Flow<UserModel>
    suspend fun verifyPatrolTask(id: Long): Flow<UiState<Nothing>>
}