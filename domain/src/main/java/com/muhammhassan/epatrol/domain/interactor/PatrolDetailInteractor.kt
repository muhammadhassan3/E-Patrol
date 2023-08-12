package com.muhammhassan.epatrol.domain.interactor

import com.muhammhassan.epatrol.core.datasource.local.LocalDataSource
import com.muhammhassan.epatrol.core.datasource.remote.RemoteDataSource
import com.muhammhassan.epatrol.domain.model.PatrolDetailModel
import com.muhammhassan.epatrol.domain.model.PatrolEventModel
import com.muhammhassan.epatrol.domain.model.UiState
import com.muhammhassan.epatrol.domain.usecase.PatrolDetailUseCase
import com.muhammhassan.epatrol.domain.utils.Mapper.mapToUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PatrolDetailInteractor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
) : PatrolDetailUseCase {
    override suspend fun getDetailPatrol(id: Long): Flow<UiState<PatrolDetailModel>> {
        return remote.getPatrolDetail(id).map {
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
                    }, lead = data.lead)
            }
        }
    }

    override suspend fun getSavedEmail(): Flow<String?> {
        return local.getEmail()
    }

}