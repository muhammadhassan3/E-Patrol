package com.muhammhassan.epatrol.core.repository

import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.EventDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolEventData
import com.muhammhassan.epatrol.core.model.PatrolItemResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface TaskRepository {
    suspend fun getTaskList(): Flow<ApiResponse<List<PatrolItemResponse>>>

    suspend fun verifyPatrolTask(id: Long): Flow<ApiResponse<Nothing>>

    suspend fun getDetailPatrol(id: Long): Flow<ApiResponse<PatrolDetailResponse>>

    suspend fun getEventList(patrolId: Long): Flow<ApiResponse<List<PatrolEventData>>>

    suspend fun getEventDetail(eventId: Long): Flow<ApiResponse<EventDetailResponse>>

    suspend fun deletePatrolEvent(patrolId: Long, eventId: Long): Flow<ApiResponse<Nothing>>

    suspend fun addPatrolEvent(
        patrolId: Long,
        event: String,
        summary: String,
        action: String,
        image: File,
        latitude: Double,
        longitude: Double,
        authorName: String
    ): Flow<ApiResponse<Nothing>>

    suspend fun markAsDone(patrolId: Long): Flow<ApiResponse<Nothing>>
}