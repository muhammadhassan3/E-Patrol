package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.repository.TaskRepository
import com.muhammhassan.epatrol.core.repository.UserRepository
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.usecase.DetailPatrolEventUseCase
import com.muhammhassan.epatrol.domain.utils.Mapper.mapToUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DetailPatrolEventInteractor(
    private val user: UserRepository,
    private val task: TaskRepository
) : DetailPatrolEventUseCase {
    override suspend fun deleteEvent(patrolId: Long, eventId: Long): Flow<UiState<Nothing>> {
        return task.deletePatrolEvent(patrolId, eventId).map {
            it.mapToUiState { data ->
                data
            }
        }
    }

    override suspend fun getEmail(): Flow<String> {
        return user.getEmail().map {
            it ?: ""
        }
    }

}