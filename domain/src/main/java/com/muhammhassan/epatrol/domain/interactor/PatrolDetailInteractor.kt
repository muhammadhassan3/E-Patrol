package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.repository.TaskRepository
import com.muhammhassan.epatrol.core.repository.UserRepository
import com.muhammhassan.epatrol.domain.model.PatrolDetailModel
import com.muhammhassan.epatrol.domain.model.PatrolEventModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.usecase.PatrolDetailUseCase
import com.muhammhassan.epatrol.domain.utils.Mapper.mapToUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PatrolDetailInteractor(
    private val task: TaskRepository, private val user: UserRepository
) : PatrolDetailUseCase {
    override suspend fun getDetailPatrol(id: Long): Flow<UiState<PatrolDetailModel>> {
        return task.getDetailPatrol(id).map {
            it.mapToUiState { data ->
                PatrolDetailModel(
                    data.id,
                    data.judul,
                    data.status,
                    data.sprin,
                    data.tanggal,
                    data.jam,
                    data.alamat,
                    lead = data.ketua
                )
            }
        }
    }

    override suspend fun getEventList(patrolId: Long): Flow<UiState<List<PatrolEventModel>>> {
        return task.getEventList(patrolId).map {
            it.mapToUiState { list ->
                list.map { data ->
                    PatrolEventModel(
                        title = data.title,
                        summary = data.summary,
                        action = data.action,
                        author = data.author,
                        createdAt = data.createdAt,
                        id = data.id
                    )
                }
            }
        }
    }

    override suspend fun getSavedEmail(): Flow<String?> {
        return user.getEmail()
    }

    override suspend fun markAsDone(id: Long): Flow<UiState<Nothing>> {
        return task.markAsDone(id).map {
            it.mapToUiState { data ->
                data
            }
        }
    }
}