package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.repository.TaskRepository
import com.muhammhassan.epatrol.domain.model.PatrolModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.usecase.TaskListUseCase
import com.muhammhassan.epatrol.domain.utils.Mapper.mapToUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskListInteractor(private val task: TaskRepository): TaskListUseCase {
    override suspend fun getTaskList(): Flow<UiState<List<PatrolModel>>> {
        return task.getTaskList().map { response ->
            response.mapToUiState { list ->
                list.map {
                    PatrolModel(
                        id = it.id,
                        status = it.status,
                        title = it.judul,
                        date = it.tanggal,
                        hour = it.jam,
                        lead = it.ketua,
                        verified = it.verified,
                        address = it.alamat
                    )
                }
            }
        }
    }
}