package com.muhammhassan.epatrol.domain.usecase

import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.domain.model.UiState
import kotlinx.coroutines.flow.Flow

interface TaskListUseCase {
    suspend fun getTaskList(): Flow<UiState<List<PatrolModel>>>

}