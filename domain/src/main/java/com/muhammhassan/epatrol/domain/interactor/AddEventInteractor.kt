package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.repository.TaskRepository
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.usecase.AddEventUseCase
import com.muhammhassan.epatrol.domain.utils.Mapper.mapToUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

class AddEventInteractor(private val task: TaskRepository) : AddEventUseCase {
    override suspend fun addEvent(
        patrolId: Long,
        event: String,
        summary: String,
        action: String,
        image: File,
        lat: Double,
        long: Double
    ): Flow<UiState<Nothing>> {
        return task.addPatrolEvent(patrolId, event, summary, action, image, lat, long).map {
            it.mapToUiState { body ->
                body
            }
        }
    }
}