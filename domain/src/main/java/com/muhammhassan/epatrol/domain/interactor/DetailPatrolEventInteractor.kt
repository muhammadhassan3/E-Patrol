package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.datasource.local.LocalDataSource
import com.muhammhassan.epatrol.core.datasource.remote.RemoteDataSource
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.usecase.DetailPatrolEventUseCase
import com.muhammhassan.epatrol.domain.utils.Mapper.mapToUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DetailPatrolEventInteractor(private val remote: RemoteDataSource, private val local: LocalDataSource): DetailPatrolEventUseCase {
    override suspend fun deleteEvent(patrolId: Long, eventId: Long): Flow<UiState<Nothing>> {
        return remote.deletePatrolEvent(patrolId, eventId).map {
            it.mapToUiState { data ->
                data
            }
        }
    }

    override suspend fun getEmail(): Flow<String> {
        return local.getEmail().map {
            it ?: ""
        }
    }

}