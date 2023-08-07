package com.muhammhassan.epatrol.domain.usecase

import com.muhammhassan.epatrol.domain.model.PatrolDetailModel
import com.muhammhassan.epatrol.domain.model.UiState
import kotlinx.coroutines.flow.Flow

interface PatrolDetailUseCase {
    suspend fun getDetailPatrol(id: Long): Flow<UiState<PatrolDetailModel>>
}