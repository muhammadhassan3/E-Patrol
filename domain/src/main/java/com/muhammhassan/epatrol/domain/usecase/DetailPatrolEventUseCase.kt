package com.muhammhassan.epatrol.domain.usecase

import com.muhammhassan.epatrol.domain.model.EventDetailModel
import com.muhammhassan.epatrol.domain.model.UiState
import kotlinx.coroutines.flow.Flow

interface DetailPatrolEventUseCase {
    suspend fun deleteEvent(patrolId: Long, eventId: Long): Flow<UiState<Nothing>>

    suspend fun getEmail(): Flow<String>

    suspend fun getEventDetail(eventId: Long): Flow<UiState<EventDetailModel>>
}