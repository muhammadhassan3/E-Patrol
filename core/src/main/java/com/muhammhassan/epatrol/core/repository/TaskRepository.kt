package com.muhammhassan.epatrol.core.repository

import com.muhammhassan.epatrol.core.model.ApiResponse
import com.muhammhassan.epatrol.core.model.PatrolDetailResponse
import com.muhammhassan.epatrol.core.model.PatrolResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface TaskRepository {
    suspend fun getTaskList(): Flow<ApiResponse<List<PatrolResponse>>>

    suspend fun verifyPatrolTask(id: Long): Flow<ApiResponse<Nothing>>

    suspend fun getDetailPatrol(id: Long): Flow<ApiResponse<PatrolDetailResponse>>

    suspend fun deletePatrolEvent(patrolId: Long, eventId: Long): Flow<ApiResponse<Nothing>>

    suspend fun addPatrolEvent(
        patrolId: Long,
        event: String,
        summary: String,
        action: String,
        image: File,
        latitude: Double,
        longitude: Double
    ): Flow<ApiResponse<Nothing>>
}