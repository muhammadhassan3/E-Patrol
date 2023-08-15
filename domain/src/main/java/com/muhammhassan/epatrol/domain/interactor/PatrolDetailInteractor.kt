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
    private val task: TaskRepository,
    private val user: UserRepository
) : PatrolDetailUseCase {
    override suspend fun getDetailPatrol(id: Long): Flow<UiState<PatrolDetailModel>> {
        return task.getDetailPatrol(id).map {
            it.mapToUiState { data ->
                PatrolDetailModel(
                    data.id,
                    data.title,
                    data.status,
                    data.sprin,
                    data.tanggal,
                    data.jam,
                    data.tujuan,
                    data.events.map { event ->
                        PatrolEventModel(
                            event.id,
                            event.image,
                            event.summary,
                            event.title,
                            event.action,
                            event.lat,
                            event.long,
                            event.createdAt,
                            author = event.author
                        )
                    }, lead = data.lead
                )
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